package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import com.example.DCP.Inventory.System.Entity.UserEntity;
import com.example.DCP.Inventory.System.Repository.DistrictRepository;
import com.example.DCP.Inventory.System.Repository.SchoolRepository;
import com.example.DCP.Inventory.System.Repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private DistrictRepository districtRepository;
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

    public SchoolEntity createSchool(SchoolEntity school) {
        return schoolRepository.save(school);
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

    public List<SchoolEntity> createAllSchools(List<SchoolEntity> schools) {
        return schoolRepository.saveAll(schools);
    }
}
