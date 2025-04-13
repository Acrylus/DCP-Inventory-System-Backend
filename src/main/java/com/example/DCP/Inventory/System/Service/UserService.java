package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.*;
import com.example.DCP.Inventory.System.Exception.IncorrectPasswordException;
import com.example.DCP.Inventory.System.Exception.LoggedOutException;
import com.example.DCP.Inventory.System.Exception.UserIdNotFoundException;
import com.example.DCP.Inventory.System.Exception.UsernameNotFoundException;
import com.example.DCP.Inventory.System.Repository.*;
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

    private final SchoolRepository schoolRepository;

    private final SchoolContactRepository schoolContactRepository;

    private final SchoolEnergyRepository schoolEnergyRepository;

    private final SchoolNTCRepository schoolNTCRepository;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, SchoolRepository schoolRepository, SchoolContactRepository schoolContactRepository, SchoolEnergyRepository schoolEnergyRepository, SchoolNTCRepository schoolNTCRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.schoolContactRepository = schoolContactRepository;
        this.schoolEnergyRepository = schoolEnergyRepository;
        this.schoolNTCRepository = schoolNTCRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserIdNotFoundException("User with ID " + id + " not found"));
    }

    public UserEntity saveUser(UserEntity userEntity) {
        Optional<UserEntity> existingUser = userRepository.findByUsername(userEntity.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        return userRepository.save(userEntity);
    }

    public UserEntity updateUser(Long id, UserEntity userDetails) {
        UserEntity userEntity = getUserById(id);
        userEntity.setUsername(userDetails.getUsername());
        userEntity.setEmail(userDetails.getEmail());
        return userRepository.save(userEntity);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserIdNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Password is incorrect");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getUserId(), user.getUserType());

        return new LoginResponse(user, token);
    }

    @Transactional
    public void logout(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userRepository.save(user);
    }


    @Transactional
    public String register(UserEntity user) {
        try {
            if (userRepository.findOneByUsername(user.getUsername()) != null) {
                throw new IllegalArgumentException("Username already exists");
            }

            if (!isValidUsername(user.getUsername())) {
                throw new IllegalArgumentException("Username must be at least 3 characters long and may optionally contain a dot (.) or underscore (_) followed by one or more lowercase letters.");
            }

            if (!isValidPassword(user.getPassword())) {
                throw new IllegalArgumentException("Password must be at least 8 characters and have at least one lowercase letter, one uppercase letter, one digit, and one special character.");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);

            return "Registration Successful";

        } catch (IllegalArgumentException ex) {
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
