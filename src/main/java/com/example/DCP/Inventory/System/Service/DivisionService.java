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

    public DivisionEntity getDivisionById(Long id) {
        return divisionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Division not found"));
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
        adminUser.setUserType("division");
        adminUser.setReferenceId(savedDivision.getDivisionId());

        userRepository.save(adminUser);

        return savedDivision;
    }

    public DivisionEntity updateDivision(Long id, DivisionEntity divisionDetails) {
        DivisionEntity divisionEntity = getDivisionById(id);

        divisionEntity.setDivision(divisionDetails.getDivision());
        divisionEntity.setTitle(divisionDetails.getTitle());
        divisionEntity.setSdsName(divisionDetails.getSdsName());
        divisionEntity.setSdsPosition(divisionDetails.getSdsPosition());
        divisionEntity.setItoName(divisionDetails.getItoName());
        divisionEntity.setItoEmail(divisionDetails.getItoEmail());

        return divisionRepository.save(divisionEntity);
    }

    public void deleteDivision(Long id) {
        divisionRepository.deleteById(id);
    }
}