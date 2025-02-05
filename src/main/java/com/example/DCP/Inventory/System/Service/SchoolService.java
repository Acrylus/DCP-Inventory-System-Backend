package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import com.example.DCP.Inventory.System.Repository.SchoolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;

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

    public SchoolEntity saveSchool(SchoolEntity school) {
        return schoolRepository.save(school);
    }

    public void deleteSchool(Long id) {
        schoolRepository.deleteById(id);
    }

    public List<SchoolEntity> createAllSchools(List<SchoolEntity> schools) {
        return schoolRepository.saveAll(schools);  // Save all schools in the list
    }
}
