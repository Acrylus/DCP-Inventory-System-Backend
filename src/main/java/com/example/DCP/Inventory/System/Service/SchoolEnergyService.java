package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.*;
import com.example.DCP.Inventory.System.Repository.SchoolEnergyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

//    public List<SchoolEnergyEntity> getAllSchoolEnergy() {
//        return schoolEnergyRepository.findAll();
//    }

    @Transactional
    public List<SchoolEnergyDTOEntity> getAllSchoolEnergy() {
        List<SchoolEnergyEntity> energies = schoolEnergyRepository.getAllWithSchool();

        return energies.stream().map(energy -> {
            SchoolEntity school = energy.getSchool();
            SchoolDTOEntity schoolDTO = (school != null)
                    ? new SchoolDTOEntity(school.getSchoolRecordId(), school.getSchoolId(), school.getName())
                    : null;

            return new SchoolEnergyDTOEntity(
                    energy.getSchoolEnergyId(),
                    energy.getEnergized(),
                    energy.getRemarks(),
                    energy.getLocalGridSupply(),
                    energy.getType(),
                    schoolDTO
            );
        }).collect(Collectors.toList());
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

    public SchoolEnergyEntity getSchoolBatchListBySchoolRecordId(Long schoolRecordId) {
        return schoolEnergyRepository.findBySchool_SchoolRecordId(schoolRecordId);
    }
}
