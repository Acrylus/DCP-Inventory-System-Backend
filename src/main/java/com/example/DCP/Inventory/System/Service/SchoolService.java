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
        user.setEmail(school.getSchoolId() + "@deped.gov.ph");
        user.setPassword(passwordEncoder.encode("@Password123"));
        userRepository.save(user);

        return savedSchool;
    }

    public SchoolEntity updateSchool(Long id, SchoolEntity schoolDetails) {
        Optional<SchoolEntity> existingSchool = schoolRepository.findById(id);

        if (existingSchool.isPresent()) {
            SchoolEntity schoolEntity = existingSchool.get();
            schoolEntity.setClassification(schoolDetails.getClassification());
            schoolEntity.setName(schoolDetails.getName());
            schoolEntity.setEmail(schoolDetails.getEmail());
            schoolEntity.setAddress(schoolDetails.getAddress());
            schoolEntity.setDistrict(schoolDetails.getDistrict());

            return schoolRepository.save(schoolEntity);
        } else {
            throw new RuntimeException("School not found with ID: " + id);
        }
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

        schoolEnergyRepository.deleteBySchoolId(id);
        schoolRepository.delete(school);
    }


    @Transactional
    public List<SchoolEntity> createAllSchools(List<SchoolEntity> schools) {
        List<SchoolEntity> savedSchools = schoolRepository.saveAll(schools);

        for (SchoolEntity school : savedSchools) {
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
            user.setEmail(school.getSchoolId() + "@deped.gov.ph");
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
