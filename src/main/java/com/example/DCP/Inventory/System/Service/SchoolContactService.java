package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.*;
import com.example.DCP.Inventory.System.Repository.CoordinatorRepository;
import com.example.DCP.Inventory.System.Repository.SchoolContactRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SchoolContactService {

    private final SchoolContactRepository schoolContactRepository;
    private final CoordinatorRepository coordinatorRepository;

    @Autowired
    public SchoolContactService(SchoolContactRepository schoolContactRepository, CoordinatorRepository coordinatorRepository) {
        this.schoolContactRepository = schoolContactRepository;
        this.coordinatorRepository = coordinatorRepository;
    }

    public SchoolContactEntity createSchoolContact(SchoolContactEntity schoolContact) {
        return schoolContactRepository.save(schoolContact);
    }

    public List<SchoolContactEntity> createAllSchoolContacts(List<SchoolContactEntity> schoolContacts) {
        return schoolContactRepository.saveAll(schoolContacts);
    }

    @Transactional
    public List<SchoolContactDTOEntity> getAllSchoolContacts() {
        List<SchoolContactEntity> contacts = schoolContactRepository.getAllWithSchool();

        return contacts.stream().map(contact -> {
            SchoolEntity school = contact.getSchool();
            SchoolDTOEntity schoolDTO = (school != null)
                    ? new SchoolDTOEntity(school.getSchoolRecordId(), school.getSchoolId(), school.getName())
                    : null;

            return new SchoolContactDTOEntity(
                    contact.getSchoolContactId(),
                    contact.getLandline(),
                    contact.getSchoolHead(),
                    contact.getSchoolHeadNumber(),
                    contact.getSchoolHeadEmail(),
                    contact.getDesignation(),
                    contact.getPropertyCustodian(),
                    contact.getPropertyCustodianNumber(),
                    contact.getPropertyCustodianEmail(),
                    schoolDTO
            );
        }).collect(Collectors.toList());
    }


    public Optional<SchoolContactEntity> getSchoolContactById(Long id) {
        return schoolContactRepository.findById(id);
    }

    @Transactional
    public SchoolContactEntity updateSchoolContact(Long id, SchoolContactEntity updatedContact) {
        Optional<SchoolContactEntity> existingContactOptional = schoolContactRepository.findById(id);

        if (existingContactOptional.isEmpty()) {
            throw new RuntimeException("School Contact not found with ID: " + id);
        }

        SchoolContactEntity existingContact = existingContactOptional.get();

        // **Step 1: Remove all existing coordinators before updating**
        coordinatorRepository.deleteBySchoolContact(existingContact);

        // **Step 2: Update Contact Fields**
        existingContact.setLandline(updatedContact.getLandline());
        existingContact.setSchoolHead(updatedContact.getSchoolHead());
        existingContact.setSchoolHeadNumber(updatedContact.getSchoolHeadNumber());
        existingContact.setSchoolHeadEmail(updatedContact.getSchoolHeadEmail());
        existingContact.setDesignation(updatedContact.getDesignation());
        existingContact.setPropertyCustodian(updatedContact.getPropertyCustodian());
        existingContact.setPropertyCustodianNumber(updatedContact.getPropertyCustodianNumber());
        existingContact.setPropertyCustodianEmail(updatedContact.getPropertyCustodianEmail());

        // **Step 3: Save Updated Contact**
        SchoolContactEntity savedContact = schoolContactRepository.save(existingContact);

        List<CoordinatorEntity> coordinators = updatedContact.getCoordinators();
        if (coordinators != null && !coordinators.isEmpty()) {
            // Step 5: Get the next available coordinatorId
            Long maxCoordinatorId = coordinatorRepository.findMaxCoordinatorId(savedContact.getSchoolContactId());
            Long nextCoordinatorId = (maxCoordinatorId == null) ? 1 : maxCoordinatorId + 1;

            List<CoordinatorEntity> newCoordinators = new ArrayList<>();
            for (CoordinatorEntity coordinator : coordinators) {
                CoordinatorIdEntity coordinatorIdEntity = new CoordinatorIdEntity();
                coordinatorIdEntity.setCoordinatorId(nextCoordinatorId++);
                coordinatorIdEntity.setSchoolContactId(savedContact.getSchoolContactId());

                CoordinatorEntity newCoordinator = new CoordinatorEntity();
                newCoordinator.setId(coordinatorIdEntity);
                newCoordinator.setSchoolContact(savedContact);
                newCoordinator.setName(coordinator.getName());
                newCoordinator.setDesignation(coordinator.getDesignation());
                newCoordinator.setNumber(coordinator.getNumber());
                newCoordinator.setEmail(coordinator.getEmail());
                newCoordinator.setRemarks(coordinator.getRemarks());

                newCoordinators.add(newCoordinator);
            }

            // Step 6: Save All New Coordinators
            coordinatorRepository.saveAll(newCoordinators);
        }

        return savedContact;
    }


    public void deleteSchoolContact(Long id) {
        if (schoolContactRepository.existsById(id)) {
            schoolContactRepository.deleteById(id);
        } else {
            throw new RuntimeException("School Contact ID not found: " + id);
        }
    }

    public SchoolContactEntity getSchoolBatchListBySchoolRecordId(Long schoolRecordId) {
        return schoolContactRepository.findBySchool_SchoolRecordId(schoolRecordId);
    }
}
