package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.UserEntity;
import com.example.DCP.Inventory.System.Exception.IncorrectPasswordException;
import com.example.DCP.Inventory.System.Exception.LoggedOutException;
import com.example.DCP.Inventory.System.Exception.UserIdNotFoundException;
import com.example.DCP.Inventory.System.Exception.UsernameNotFoundException;
import com.example.DCP.Inventory.System.Repository.UserRepository;
import com.example.DCP.Inventory.System.Response.LoginResponse;
import com.example.DCP.Inventory.System.Response.LoginRequest;
import com.example.DCP.Inventory.System.Util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil; // FIX: Inject JwtUtil correctly
    }

    // Fetch all users
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Fetch a user by ID
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserIdNotFoundException("User with ID " + id + " not found"));
    }

    // Save a new user
    public UserEntity saveUser(UserEntity userEntity) {
        Optional<UserEntity> existingUser = userRepository.findByUsername(userEntity.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        return userRepository.save(userEntity);
    }

    // Update an existing user
    public UserEntity updateUser(Long id, UserEntity userDetails) {
        UserEntity userEntity = getUserById(id);
        userEntity.setUsername(userDetails.getUsername());
        userEntity.setPassword(userDetails.getPassword());
        userEntity.setEmail(userDetails.getEmail());
        userEntity.setUserType(userDetails.getUserType());
        userEntity.setDivision(userDetails.getDivision());
        userEntity.setDistrict(userDetails.getDistrict());
        userEntity.setSchool(userDetails.getSchool());
        return userRepository.save(userEntity);
    }

    // Delete a user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserIdNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        // 🔥 FIX: Compare hashed password using passwordEncoder.matches()
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Password is incorrect");
        }

        // Generate JWT using JwtUtil
        String token = jwtUtil.generateToken(user.getUsername(), user.getUserId(), user.getUserType());

        // Return response with JWT
        return new LoginResponse("Login successful", user, token);
    }

    @Transactional
    public void logout(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userRepository.save(user);
    }


    @Transactional
    public String registerUser(UserEntity user) {
        try {
            if (userRepository.findOneByUsername(user.getUsername()) != null) {
                throw new IllegalArgumentException("Username already exists");
            } else if (!isValidUsername(user.getUsername())) {
                throw new IllegalArgumentException("Username must be at least 3 characters long and may optionally contain a dot (.) or underscore (_) followed by one or more lowercase letters.");
            } else if (!isValidPassword(user.getPassword())) {
                throw new IllegalArgumentException("Password must be at least 8 characters and have at least one lowercase letter, one uppercase letter, one digit, and one special character");
            }

            // ✅ Save the user
            userRepository.save(user);

            return "Registration Successful";

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }


    private boolean isValidUsername(String username) {
        return username.matches("^[a-z]{3,}(?:[._][a-z]{1,})*$");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d\\s]).+$");
    }

    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        UserEntity user;

        try {
            user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

            if (passwordEncoder.matches(oldPassword, user.getPassword())) {

                if (!isValidPassword(newPassword)) {
                    throw new IncorrectPasswordException("Password must be at least 8 characters and have at least one lowercase letter, one uppercase letter, one digit, and one special character");
                }

                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            } else {
                System.out.println("Old password does not match.");
                return false;
            }

        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("User not found with ID: " + userId);
        }
    }
}
