package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.DivisionEntity;
import com.example.DCP.Inventory.System.Entity.UserEntity;
import com.example.DCP.Inventory.System.Repository.DivisionRepository;
import com.example.DCP.Inventory.System.Repository.UserRepository;
import org.springframework. beans.factory.annotation.Autowired;
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
        if (divisionRepository.existsByOfficeName(division.getOfficeName())) {
            throw new IllegalArgumentException("Office name already exists. Please choose a different name.");
        }

        DivisionEntity savedDivision = divisionRepository.save(division);

        String username = savedDivision.getOfficeName();
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists. Please choose a different office name.");
        }

        UserEntity adminUser = new UserEntity();
        adminUser.setUsername(savedDivision.getOfficeName());
        adminUser.setEmail(savedDivision.getEmailAddress());
        adminUser.setPassword(passwordEncoder.encode("@Password123"));
        adminUser.setUserType("admin");
        adminUser.setDivision(savedDivision);

        userRepository.save(adminUser);

        return savedDivision;
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
