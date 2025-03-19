package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.SchoolBatchListEntity;
import com.example.DCP.Inventory.System.Entity.SchoolEnergyEntity;
import com.example.DCP.Inventory.System.Repository.SchoolEnergyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolEnergyService {

    private final SchoolEnergyRepository schoolEnergyRepository;

    @Autowired
    public SchoolEnergyService(SchoolEnergyRepository schoolEnergyRepository) {
        this.schoolEnergyRepository = schoolEnergyRepository;
    }

    public SchoolEnergyEntity createSchoolEnergy(SchoolEnergyEntity schoolEnergy) {
        return schoolEnergyRepository.save(schoolEnergy);
    }

    public List<SchoolEnergyEntity> createAllSchoolEnergies(List<SchoolEnergyEntity> schoolEnergies) {
        return schoolEnergyRepository.saveAll(schoolEnergies);
    }

    public List<SchoolEnergyEntity> getAllSchoolEnergy() {
        return schoolEnergyRepository.findAll();
    }

    public Optional<SchoolEnergyEntity> getSchoolEnergyById(Long id) {
        return schoolEnergyRepository.findById(id);
    }

    public SchoolEnergyEntity updateSchoolEnergy(Long id, SchoolEnergyEntity updatedEnergy) {
        Optional<SchoolEnergyEntity> existingSchoolEnergyOptional = schoolEnergyRepository.findById(id);

        if (existingSchoolEnergyOptional.isEmpty()) {
            throw new RuntimeException("School Energy ID not found: " + id);
        }

        SchoolEnergyEntity existingSchoolEnergy = existingSchoolEnergyOptional.get();

        existingSchoolEnergy.setEnergized(updatedEnergy.getEnergized());
        existingSchoolEnergy.setRemarks(updatedEnergy.getRemarks());
        existingSchoolEnergy.setLocalGridSupply(updatedEnergy.getLocalGridSupply());

        return schoolEnergyRepository.save(existingSchoolEnergy);
    }


    public void deleteSchoolEnergy(Long id) {
        if (schoolEnergyRepository.existsById(id)) {
            schoolEnergyRepository.deleteById(id);
        } else {
            throw new RuntimeException("School Energy ID not found: " + id);
        }
    }

    public List<SchoolEnergyEntity> getSchoolBatchListBySchoolId(Long schoolId) {
        return schoolEnergyRepository.findBySchool_SchoolId(schoolId);
    }
}
