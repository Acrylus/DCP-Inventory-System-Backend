package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import com.example.DCP.Inventory.System.Entity.UserEntity;
import com.example.DCP.Inventory.System.Repository.SchoolRepository;
import com.example.DCP.Inventory.System.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public List<SchoolEntity> getAllSchools() {
        return schoolRepository.findAll();
    }

    public SchoolEntity getSchoolById(Long id) {
        return schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found"));
    }

    @Transactional
    public SchoolEntity saveSchool(SchoolEntity school) {
        if (schoolRepository.existsByName(school.getName())) {
            throw new IllegalArgumentException("School name already exists. Please choose a different name.");
        }

        SchoolEntity savedSchool = schoolRepository.save(school);

        String username = savedSchool.getName();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists. Please choose a different school name.");
        }

        UserEntity schoolUser = new UserEntity();
        schoolUser.setUsername(username);
        schoolUser.setEmail(savedSchool.getSchoolHeadEmail());
        schoolUser.setPassword(passwordEncoder.encode("@Password123"));
        schoolUser.setUserType("user");
        schoolUser.setSchool(savedSchool);

        userRepository.save(schoolUser);

        return savedSchool;
    }

    public SchoolEntity updateSchool(Long id, SchoolEntity schoolDetails) {
        SchoolEntity school = getSchoolById(id);

        school.setIctCoordinator(schoolDetails.getIctCoordinator());
        school.setRecordNumber(schoolDetails.getRecordNumber());
        school.setClassification(schoolDetails.getClassification());
        school.setName(schoolDetails.getName());
        school.setAddress(schoolDetails.getAddress());
        school.setNumberOfPackage(schoolDetails.getNumberOfPackage());
        school.setSchoolHead(schoolDetails.getSchoolHead());
        school.setDesignation(schoolDetails.getDesignation());
        school.setSchoolHeadEmail(schoolDetails.getSchoolHeadEmail());
        school.setSchoolHeadContact(schoolDetails.getSchoolHeadContact());
        school.setTelephone(schoolDetails.getTelephone());
        school.setPreviousStation(schoolDetails.getPreviousStation());
        school.setEnergized(schoolDetails.getEnergized());
        school.setConnectivity(schoolDetails.getConnectivity());

        return saveSchool(school);  // Save updated school
    }

    public void deleteSchool(Long id) {
        schoolRepository.deleteById(id);
    }

    @Transactional
    public List<SchoolEntity> createAllSchools(List<SchoolEntity> schools) {
        // Check for duplicate school names before saving
        for (SchoolEntity school : schools) {
            if (schoolRepository.existsByName(school.getName())) {
                throw new IllegalArgumentException("School name '" + school.getName() + "' already exists. Please choose a different name.");
            }
        }

        // Save all school entities
        List<SchoolEntity> savedSchools = schoolRepository.saveAll(schools);

        // Create a user for each saved school
        List<UserEntity> schoolUsers = savedSchools.stream().map(savedSchool -> {
            String username = savedSchool.getName(); // Use school name as username

            // Ensure username is unique
            if (userRepository.existsByUsername(username)) {
                throw new IllegalArgumentException("Username '" + username + "' already exists. Please choose a different school name.");
            }

            UserEntity schoolUser = new UserEntity();
            schoolUser.setUsername(username);
            schoolUser.setEmail(savedSchool.getSchoolHeadEmail()); // Use school head's email
            schoolUser.setPassword(passwordEncoder.encode("@Password123")); // Default password
            schoolUser.setUserType("user"); // Set user type as "user"
            schoolUser.setSchool(savedSchool); // Link user to school

            return schoolUser;
        }).collect(Collectors.toList());

        // Save all users
        userRepository.saveAll(schoolUsers);

        return savedSchools;
    }
}
