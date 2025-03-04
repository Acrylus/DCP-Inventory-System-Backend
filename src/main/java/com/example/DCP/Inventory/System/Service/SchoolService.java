package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import com.example.DCP.Inventory.System.Entity.UserEntity;
import com.example.DCP.Inventory.System.Repository.SchoolRepository;
import com.example.DCP.Inventory.System.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;
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

    @Transactional
    public SchoolEntity saveSchool(SchoolEntity school) {
        if (schoolRepository.existsByName(school.getName())) {
            throw new IllegalArgumentException("School name already exists. Please choose a different name.");
        }

        SchoolEntity savedSchool = schoolRepository.save(school);

        String username = savedSchool.getName();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists. Please choose a different school name.");
        }

        UserEntity schoolUser = new UserEntity();
        schoolUser.setUsername(username);
        schoolUser.setEmail(savedSchool.getSchoolHeadEmail());
        schoolUser.setPassword(passwordEncoder.encode("@Password123"));
        schoolUser.setUserType("user");
        schoolUser.setSchool(savedSchool);

        userRepository.save(schoolUser);

        return savedSchool;
    }

    public SchoolEntity updateSchool(Long id, SchoolEntity schoolDetails) {
        SchoolEntity school = getSchoolById(id);

        school.setClassification(schoolDetails.getClassification());
        school.setName(schoolDetails.getName());
        school.setAddress(schoolDetails.getAddress());
        school.setSchoolHead(schoolDetails.getSchoolHead());
        school.setDesignation(schoolDetails.getDesignation());
        school.setSchoolHeadEmail(schoolDetails.getSchoolHeadEmail());
        school.setSchoolHeadNumber(schoolDetails.getSchoolHeadNumber());
        school.setLandline(schoolDetails.getLandline());
        school.setDivision(schoolDetails.getDivision());
        school.setDistrict(schoolDetails.getDistrict());
        school.setPropertyCustodian(schoolDetails.getPropertyCustodian());
        school.setPropertyCustodianNumber(schoolDetails.getPropertyCustodianNumber());
        school.setPropertyCustodianEmail(schoolDetails.getPropertyCustodianEmail());
        school.setEnergized(schoolDetails.getEnergized());
        school.setEnergizedRemarks(schoolDetails.getEnergizedRemarks());
        school.setLocalGridSupply(schoolDetails.getLocalGridSupply());
        school.setConnectivity(schoolDetails.getConnectivity());
        school.setSmart(schoolDetails.getSmart());
        school.setGlobe(schoolDetails.getGlobe());
        school.setDigitalNetwork(schoolDetails.getDigitalNetwork());
        school.setAm(schoolDetails.getAm());
        school.setFm(schoolDetails.getFm());
        school.setTv(schoolDetails.getTv());
        school.setCable(schoolDetails.getCable());
        school.setNtcRemark(schoolDetails.getNtcRemark());
        school.setCoordinators(schoolDetails.getCoordinators());
        school.setBatchSchoolLists(schoolDetails.getBatchSchoolLists());

        return saveSchool(school);
    }

    public void deleteSchool(Long id) {
        schoolRepository.deleteById(id);
    }

    @Transactional
    public List<SchoolEntity> createAllSchools(List<SchoolEntity> schools) {
        // Iterate over each school to check for duplicate names and ensure uniqueness
        for (SchoolEntity school : schools) {
            String originalSchoolName = school.getName();
            String modifiedSchoolName = originalSchoolName;
            int suffix = 2;

            // Check and ensure unique school name in the school repository
            while (schoolRepository.existsByName(modifiedSchoolName)) {
                modifiedSchoolName = originalSchoolName + " " + suffix;
                suffix++;  // Increment the suffix for each conflict
            }

            // Set the modified unique name to the school
            school.setName(modifiedSchoolName);
            System.out.println("Unique school name assigned: " + modifiedSchoolName); // Log the unique name

            // Save the school entity first, making it persistent
            schoolRepository.save(school);

            // Ensure that the username (derived from school name) is unique
            String username = modifiedSchoolName; // Assuming username is derived from school name
            while (userRepository.existsByUsername(username)) {
                username = modifiedSchoolName + " " + suffix; // Update username with suffix if already exists
                suffix++;
            }

            // Set the unique username for the user
            System.out.println("Unique username assigned: " + username); // Log the username

            // Check if the school head's email is provided before creating the user
            if (school.getSchoolHeadEmail() != null && !school.getSchoolHeadEmail().isEmpty()) {
                // Now, create the user entity with the saved school
                UserEntity schoolUser = new UserEntity();
                schoolUser.setUsername(username);
                schoolUser.setEmail(school.getSchoolHeadEmail()); // Use school head's email
                schoolUser.setPassword(passwordEncoder.encode("@Password123")); // Default password
                schoolUser.setUserType("user"); // Set user type as "user"
                schoolUser.setSchool(school); // Link the already saved school to the user
                schoolUser.setDivision(school.getDivision());

                // Save the user after associating the school
                userRepository.save(schoolUser);
            } else {
                System.out.println("No email provided for school: " + modifiedSchoolName + ". Skipping user creation.");
            }
        }

        // Return the list of saved schools
        return schools;
    }




}
