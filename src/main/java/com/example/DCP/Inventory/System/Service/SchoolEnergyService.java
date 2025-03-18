package com.example.DCP.Inventory.System.Service;

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
        return schoolEnergyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School Energy not found"));
    }

    public Optional<SchoolEnergyEntity> getSchoolEnergyById(Long id) {
        return schoolEnergyRepository.findById(id);
    }

    public SchoolEnergyEntity updateSchoolEnergy(Long id, SchoolEnergyEntity updatedEnergy) {
        Optional<SchoolEnergyEntity> existingSchool = schoolEnergyRepository.findById(id);

        if (existingSchool.isPresent()) {
            schoolEnergy.setEnergized(updatedEnergy.getEnergized());
            schoolEnergy.setRemarks(updatedEnergy.getRemarks());
            schoolEnergy.setLocalGridSupply(updatedEnergy.getLocalGridSupply());

            return schoolEnergyRepository.save(schoolEnergyEntity);
        } else {
            throw new RuntimeException("School Energy ID not found: " + id);
        }
    }

    public void deleteSchoolEnergy(Long id) {
        if (schoolEnergyRepository.existsById(id)) {
            schoolEnergyRepository.deleteById(id);
        } else {
            throw new RuntimeException("School Energy ID not found: " + id);
        }
    }

    // Method to count all records
    public long countSchoolEnergy() {
        return schoolEnergyRepository.count();
    }

    // Method to get records by Local Grid Supply
    public List<SchoolEnergyEntity> findByLocalGridSupply(String localGridSupply) {
        return schoolEnergyRepository.findByLocalGridSupply(localGridSupply);
    }

    // Method to check if a record exists by ID
    public boolean existsById(Long id) {
        return schoolEnergyRepository.existsById(id);
    }
}
