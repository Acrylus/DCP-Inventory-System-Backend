package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.DivisionEntity;
import com.example.DCP.Inventory.System.Entity.UserEntity;
import com.example.DCP.Inventory.System.Repository.DivisionRepository;
import com.example.DCP.Inventory.System.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DivisionService {

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<DivisionEntity> getAllDivisions() {
        return divisionRepository.findAll();
    }

    public Optional<DivisionEntity> getDivisionById(Long id) {
        return divisionRepository.findById(id);
    }

    public DivisionEntity createDivision(DivisionEntity division) {
        if (divisionRepository.existsByDivision(division.getDivision())) {
            throw new IllegalArgumentException("Division already exists. Please choose a different division.");
        }

        DivisionEntity savedDivision = divisionRepository.save(division);

        String username = savedDivision.getTitle();
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists. Please choose a different division.");
        }

        UserEntity adminUser = new UserEntity();
        adminUser.setUsername(savedDivision.getDivision());
        adminUser.setEmail(savedDivision.getItoEmail());
        adminUser.setPassword(passwordEncoder.encode("@Password123"));
        adminUser.setUserType("admin");
        adminUser.setDivision(savedDivision);

        userRepository.save(adminUser);

        return savedDivision;
    }

    public DivisionEntity updateDivision(Long id, DivisionEntity divisionDetails) {
        return divisionRepository.findById(id)
                .map(division -> {
                    division.setDivision(divisionDetails.getDivision());
                    division.setTitle(divisionDetails.getTitle());
                    division.setSdsName(divisionDetails.getSdsName());
                    division.setSdsPosition(divisionDetails.getSdsPosition());
                    division.setItoName(divisionDetails.getItoName());
                    division.setItoEmail(divisionDetails.getItoEmail());

                    division.setMunicipalities(divisionDetails.getMunicipalities());
                    division.setDistricts(divisionDetails.getDistricts());

                    return divisionRepository.save(division);
                })
                .orElseThrow(() -> new RuntimeException("Division not found"));
    }

    public void deleteDivision(Long id) {
        divisionRepository.deleteById(id);
    }
}