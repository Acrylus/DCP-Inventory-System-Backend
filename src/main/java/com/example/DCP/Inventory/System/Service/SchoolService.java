package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.*;
import com.example.DCP.Inventory.System.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private SchoolContactRepository schoolContactRepository;
    @Autowired
    private CoordinatorRepository coordinatorRepository;
    @Autowired
    private SchoolEnergyRepository schoolEnergyRepository;
    @Autowired
    private SchoolNTCRepository schoolNTCRepository;
    @Autowired
    private ProviderRepository providerRepository;
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
    public SchoolEntity createSchool(SchoolEntity school) {
        school.setEmail(school.getSchoolId() + "@deped.gov.ph");
        SchoolEntity savedSchool = schoolRepository.save(school);

        SchoolContactEntity schoolContact = new SchoolContactEntity();
        schoolContact.setSchool(school);
        schoolContactRepository.save(schoolContact);

        SchoolEnergyEntity schoolEnergy = new SchoolEnergyEntity();
        schoolEnergy.setSchool(school);
        schoolEnergyRepository.save(schoolEnergy);

        SchoolNTCEntity schoolNTC = new SchoolNTCEntity();
        schoolNTC.setSchool(school);
        schoolNTCRepository.save(schoolNTC);

        String originalSchoolName = school.getName();
        String modifiedSchoolName = originalSchoolName;
        String suffix = school.getDistrict().getName();

        if (schoolRepository.existsByName(modifiedSchoolName)) {
            modifiedSchoolName = originalSchoolName + " " + suffix;
        }

        UserEntity user = new UserEntity();
        user.setReferenceId(school.getSchoolRecordId());
        user.setUserType("school");
        user.setUsername(modifiedSchoolName);
        user.setEmail(school.getEmail());
        user.setPassword(passwordEncoder.encode("@Password123"));
        userRepository.save(user);

        return savedSchool;
    }

    @Transactional
    public SchoolEntity updateSchool(Long id, SchoolEntity schoolDetails) {
        SchoolEntity schoolEntity = schoolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("School not found with ID: " + id));

        // Set updated school info
        String generatedEmail = schoolDetails.getSchoolId() + "@deped.gov.ph";

        schoolEntity.setClassification(schoolDetails.getClassification());
        schoolEntity.setSchoolId(schoolDetails.getSchoolId());
        schoolEntity.setName(schoolDetails.getName());
        schoolEntity.setEmail(generatedEmail);
        schoolEntity.setAddress(schoolDetails.getAddress());
        schoolEntity.setDistrict(schoolDetails.getDistrict());

        SchoolEntity updatedSchool = schoolRepository.save(schoolEntity);

        Long userId = userRepository.findUserIdBySchoolRecordId(id);
        if (userId == null) {
            throw new EntityNotFoundException("User not found for school ID: " + id);
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User entity not found with ID: " + userId));

        user.setEmail(generatedEmail);
        userRepository.save(user);

        return updatedSchool;
    }

    @Transactional
    public void deleteSchool(Long id) {
        SchoolEntity school = schoolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("School with ID " + id + " not found."));

        // Ensure related entities are managed
        SchoolNTCEntity schoolNTC = schoolNTCRepository.findBySchool_SchoolRecordId(id);
        SchoolContactEntity schoolContact = schoolContactRepository.findBySchool_SchoolRecordId(id);

        if (schoolNTC != null) {
            schoolNTC = schoolNTCRepository.findById(schoolNTC.getSchoolNTCId()).orElse(null); // Ensure managed state
            providerRepository.deleteBySchoolNTC(schoolNTC);
            schoolNTCRepository.delete(schoolNTC);
        }

        if (schoolContact != null) {
            schoolContact = schoolContactRepository.findById(schoolContact.getSchoolContactId()).orElse(null); // Ensure managed state
            coordinatorRepository.deleteBySchoolContact(schoolContact);
            schoolContactRepository.delete(schoolContact);
        }

        // 🆕 Delete associated user
        try {
            Long userId = userRepository.findUserIdBySchoolRecordId(id);
            userRepository.deleteById(userId);
        } catch (Exception e) {
            System.out.println("No user found or failed to delete user for schoolRecordId: " + id);
        }

        schoolEnergyRepository.deleteBySchoolId(id);
        schoolRepository.delete(school);
    }



    @Transactional
    public List<SchoolEntity> createAllSchools(List<SchoolEntity> schools) {
        List<SchoolEntity> savedSchools = schoolRepository.saveAll(schools);

        for (SchoolEntity school : savedSchools) {
            school.setEmail(school.getSchoolId() + "@deped.gov.ph");
            SchoolContactEntity schoolContact = new SchoolContactEntity();
            schoolContact.setSchool(school);
            schoolContactRepository.save(schoolContact);

            SchoolEnergyEntity schoolEnergy = new SchoolEnergyEntity();
            schoolEnergy.setSchool(school);
            schoolEnergyRepository.save(schoolEnergy);

            SchoolNTCEntity schoolNTC = new SchoolNTCEntity();
            schoolNTC.setSchool(school);
            schoolNTCRepository.save(schoolNTC);

            String originalSchoolName = school.getName();
            String modifiedSchoolName = originalSchoolName;
            String suffix = school.getDistrict().getName();

            if (schoolRepository.existsByName(modifiedSchoolName)) {
                modifiedSchoolName = originalSchoolName + " " + suffix;
            }


            UserEntity user = new UserEntity();
            user.setReferenceId(school.getSchoolRecordId());
            user.setUserType("school");
            user.setUsername(modifiedSchoolName);
            user.setEmail(school.getEmail());
            user.setPassword(passwordEncoder.encode("@Password123"));
            userRepository.save(user);
        }

        return savedSchools;
    }

    public Optional<Long> findSchoolRecordIdBySchoolId(String schoolId) {
        return schoolRepository.findSchoolRecordIdBySchoolId(schoolId);
    }

    public Optional<Long> findSchoolRecordIdByName(String schoolName) {
        return schoolRepository.findSchoolRecordIdByName(schoolName);
    }

    public boolean isDuplicateSchoolId(String schoolId) {
        return schoolRepository.countBySchoolId(schoolId) > 1;
    }
}
