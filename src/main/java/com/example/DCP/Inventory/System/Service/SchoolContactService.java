package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.SchoolContactEntity;
import com.example.DCP.Inventory.System.Repository.SchoolContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SchoolContactService {

    private final SchoolContactRepository schoolContactRepository;

    @Autowired
    public SchoolContactService(SchoolContactRepository schoolContactRepository) {
        this.schoolContactRepository = schoolContactRepository;
    }

    // Create a single SchoolContactEntity
    public SchoolContactEntity createSchoolContact(SchoolContactEntity schoolContact) {
        return schoolContactRepository.save(schoolContact);
    }

    // Create multiple SchoolContactEntities
    public List<SchoolContactEntity> createAllSchoolContacts(List<SchoolContactEntity> schoolContacts) {
        return schoolContactRepository.saveAll(schoolContacts);
    }

    // Retrieve all SchoolContactEntities
    public List<SchoolContactEntity> getAllSchoolContacts() {
        return schoolContactRepository.findAll();
    }

    // Retrieve a single SchoolContactEntity by ID
    public Optional<SchoolContactEntity> getSchoolContactById(Long id) {
        return schoolContactRepository.findById(id);
    }

    // Update an existing SchoolContactEntity
    public SchoolContactEntity updateSchoolContact(Long id, SchoolContactEntity updatedContact) {
        SchoolContactEntity order = new SchoolContactEntity();
        try {
            schoolContact.setLandline(updatedContact.getLandline());
            schoolContact.setSchoolHead(updatedContact.getSchoolHead());
            schoolContact.setSchoolHeadNumber(updatedContact.getSchoolHeadNumber());
            schoolContact.setSchoolHeadEmail(updatedContact.getSchoolHeadEmail());
            schoolContact.setPropertyCustodian(updatedContact.getPropertyCustodian());
            schoolContact.setPropertyCustodianNumber(updatedContact.getPropertyCustodianNumber());
            schoolContact.setPropertyCustodianEmail(updatedContact.getPropertyCustodianEmail());
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException("School Contact ID not found: " + id);
        } finally {
            return schoolContactRepository.save(schoolContactEntity);
        }
    }

    // Delete a SchoolContactEntity by ID
    public void deleteSchoolContact(Long id) {
        if (schoolContactRepository.existsById(id)) {
            schoolContactRepository.deleteById(id);
        } else {
            throw new RuntimeException("School Contact ID not found: " + id);
        }
    }
}
