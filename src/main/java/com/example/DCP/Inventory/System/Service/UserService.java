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
        userEntity.setPassword(userDetails.getPassword());
        userEntity.setEmail(userDetails.getEmail());
        userEntity.setUserType(userDetails.getUserType());
        userEntity.setDivision(userDetails.getDivision());
        userEntity.setDistrict(userDetails.getDistrict());
        userEntity.setSchool(userDetails.getSchool());
        return userRepository.save(userEntity);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserIdNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Password is incorrect");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getUserId(), user.getUserType());

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
            if (user.getSchool() == null || user.getSchool().getSchoolRecordId() == null) {
                throw new IllegalArgumentException("School ID must not be null");
            }

            SchoolEntity school = schoolRepository.findById(user.getSchool().getSchoolRecordId())
                    .orElseThrow(() -> new IllegalArgumentException("School not found with ID: " + user.getSchool().getSchoolRecordId()));

            SchoolContactEntity schoolContact = schoolContactRepository.findBySchool(school)
                    .orElseThrow(() -> new IllegalArgumentException("SchoolContact must not be null for school ID: " + school.getSchoolRecordId()));

            String originalSchoolName = school.getName();
            String modifiedSchoolName = originalSchoolName;
            String suffix = school.getDistrict().getName();

            if (schoolRepository.existsByName(modifiedSchoolName)) {
                modifiedSchoolName = originalSchoolName + " " + suffix;
            }
            school.setName(modifiedSchoolName);

            System.out.println("Unique school name assigned: " + modifiedSchoolName);

            String username = modifiedSchoolName;
            System.out.println("Unique username assigned: " + username);

            UserEntity schoolUser = new UserEntity();
            schoolUser.setUsername(username);

            schoolUser.setEmail(
                    schoolContact.getSchoolHeadEmail() != null ? schoolContact.getSchoolHeadEmail() : "default@example.com"
            );
            schoolUser.setPassword(passwordEncoder.encode("@Password123"));
            schoolUser.setUserType("school");
            schoolUser.setSchool(school);
            schoolUser.setDivision(school.getDivision());
            schoolUser.setDistrict(school.getDistrict());

            if (userRepository.findOneByUsername(username) != null) {
                throw new IllegalArgumentException("Username already exists");
            }

            if (!isValidPassword(user.getPassword())) {
                throw new IllegalArgumentException("Password must be at least 8 characters and have at least one lowercase letter, one uppercase letter, one digit, and one special character");
            }

            userRepository.save(schoolUser);
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
