package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.PackageEntity;
import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import com.example.DCP.Inventory.System.Entity.UserEntity;
import com.example.DCP.Inventory.System.Repository.SchoolRepository;
import com.example.DCP.Inventory.System.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        String originalSchoolName = school.getName();
        String modifiedSchoolName = originalSchoolName;
        String suffix = school.getDistrict().getName();

        while (schoolRepository.existsByName(modifiedSchoolName)) {
            modifiedSchoolName = originalSchoolName + " " + suffix;
        }

        school.setName(modifiedSchoolName);

        System.out.println("Unique school name assigned: " + modifiedSchoolName);

        SchoolEntity savedSchool = schoolRepository.save(school);

        String username = modifiedSchoolName;

        System.out.println("Unique username assigned: " + username);

        UserEntity schoolUser = new UserEntity();
        schoolUser.setUsername(username);
        schoolUser.setEmail(school.getSchoolContact().getSchoolHeadEmail());
        schoolUser.setPassword(passwordEncoder.encode("@Password123"));
        schoolUser.setUserType("user");
        schoolUser.setSchool(school);
        schoolUser.setDivision(school.getDivision());
        schoolUser.setDistrict((school.getDistrict()));

        userRepository.save(schoolUser);

        return savedSchool;
    }

    public SchoolEntity updateSchool(Long id, SchoolEntity schoolDetails) {
        Optional<SchoolEntity> existingSchool = schoolRepository.findById(id);

        if (existingSchool.isPresent()) {
            SchoolEntity schoolEntity = existingSchool.get();
            schoolEntity.setClassification(schoolDetails.getClassification());
            schoolEntity.setName(schoolDetails.getName());
            schoolEntity.setAddress(schoolDetails.getAddress());
            schoolEntity.setDesignation(schoolDetails.getDesignation());
            schoolEntity.setDivision(schoolDetails.getDivision());
            schoolEntity.setDistrict(schoolDetails.getDistrict());

            return schoolRepository.save(schoolEntity);
        } else {
            throw new RuntimeException("School not found with ID: " + id);
        }
    }

    public void deleteSchool(Long id) {
        schoolRepository.deleteById(id);
    }

    @Transactional
    public List<SchoolEntity> createAllSchools(List<SchoolEntity> schools) {
        for (SchoolEntity school : schools) {
            String originalSchoolName = school.getName();
            String modifiedSchoolName = originalSchoolName;
            String suffix = school.getDistrict().getName();

            while (schoolRepository.existsByName(modifiedSchoolName)) {
                modifiedSchoolName = originalSchoolName + " " + suffix;
            }

            school.setName(modifiedSchoolName);

            System.out.println("Unique school name assigned: " + modifiedSchoolName);

            schoolRepository.save(school);

            String username = modifiedSchoolName;

            System.out.println("Unique username assigned: " + username);

            UserEntity schoolUser = new UserEntity();
            schoolUser.setUsername(username);
            schoolUser.setEmail(school.getSchoolContact().getSchoolHeadEmail());
            schoolUser.setPassword(passwordEncoder.encode("@Password123"));
            schoolUser.setUserType("user");
            schoolUser.setSchool(school);
            schoolUser.setDivision(school.getDivision());
            schoolUser.setDistrict((school.getDistrict()));

            userRepository.save(schoolUser);
        }
        return schools;
    }
}
