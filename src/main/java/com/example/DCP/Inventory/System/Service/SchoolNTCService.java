package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.SchoolNTCEntity;
import com.example.DCP.Inventory.System.Repository.SchoolNTCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolNTCService {

    private final SchoolNTCRepository schoolNTCRepository;

    @Autowired
    public SchoolNTCService(SchoolNTCRepository schoolNTCRepository) {
        this.schoolNTCRepository = schoolNTCRepository;
    }

    public SchoolNTCEntity createSchoolNTC(SchoolNTCEntity schoolNTC) {
        return schoolNTCRepository.save(schoolNTC);
    }

    public List<SchoolNTCEntity> createAllSchoolNTC(List<SchoolNTCEntity> schoolNTCs) {
        return schoolNTCRepository.saveAll(schoolNTCs);
    }

    public List<SchoolNTCEntity> getAllSchoolNTCs() {
        return schoolNTCRepository.findAll();
    }

    public Optional<SchoolNTCEntity> getSchoolNTCById(Long id) {
        return schoolNTCRepository.findById(id);
    }

    public SchoolNTCEntity updateSchoolNTC(Long id, SchoolNTCEntity updatedNTC) {
        Optional<SchoolNTCEntity> existingNTCOptional = schoolNTCRepository.findById(id);

        if (existingNTCOptional.isEmpty()) {
            throw new RuntimeException("School NTC ID not found: " + id);
        }

        SchoolNTCEntity existingNTC = existingNTCOptional.get();

        existingNTC.setInternet(updatedNTC.getInternet());
        existingNTC.setPldt(updatedNTC.getPldt());
        existingNTC.setGlobe(updatedNTC.getGlobe());
        existingNTC.setAm(updatedNTC.getAm());
        existingNTC.setFm(updatedNTC.getFm());
        existingNTC.setTv(updatedNTC.getTv());
        existingNTC.setCable(updatedNTC.getCable());
        existingNTC.setRemark(updatedNTC.getRemark());

        return schoolNTCRepository.save(existingNTC);
    }

    public void deleteSchoolNTC(Long id) {
        if (schoolNTCRepository.existsById(id)) {
            schoolNTCRepository.deleteById(id);
        } else {
            throw new RuntimeException("School NTC ID not found: " + id);
        }
    }

    public List<SchoolNTCEntity> getSchoolNTCBySchoolRecordId(Long schoolRecordId) {
        return schoolNTCRepository.findBySchool_SchoolRecordId(schoolRecordId);
    }
}
