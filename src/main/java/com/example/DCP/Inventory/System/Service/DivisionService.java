package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.DivisionEntity;
import com.example.DCP.Inventory.System.Repository.DivisionRepository;
import org.springframework. beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DivisionService {

    @Autowired
    private DivisionRepository divisionRepository;

    public List<DivisionEntity> getAllDivisions() {
        return divisionRepository.findAll();
    }

    public Optional<DivisionEntity> getDivisionById(Long id) {
        return divisionRepository.findById(id);
    }

    public DivisionEntity saveDivision(DivisionEntity division) {
        return divisionRepository.save(division);
    }

    public DivisionEntity updateDivision(Long id, DivisionEntity divisionDetails) {
        return divisionRepository.findById(id)
                .map(division -> {
                    division.setOfficeName(divisionDetails.getOfficeName());
                    division.setHeadOfOffice(divisionDetails.getHeadOfOffice());
                    division.setPosition(divisionDetails.getPosition());
                    division.setItoName(divisionDetails.getItoName());
                    division.setEmailAddress(divisionDetails.getEmailAddress());
                    return divisionRepository.save(division);
                })
                .orElseThrow(() -> new RuntimeException("Division not found"));
    }

    public void deleteDivision(Long id) {
        divisionRepository.deleteById(id);
    }
}
