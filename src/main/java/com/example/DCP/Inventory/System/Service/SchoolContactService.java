package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.SchoolContactEntity;
import com.example.DCP.Inventory.System.Repository.SchoolContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolContactService {

    private final SchoolContactRepository schoolContactRepository;

    @Autowired
    public SchoolContactService(SchoolContactRepository schoolContactRepository) {
        this.schoolContactRepository = schoolContactRepository;
    }

    public SchoolContactEntity createSchoolContact(SchoolContactEntity schoolContact) {
        return schoolContactRepository.save(schoolContact);
    }

    public List<SchoolContactEntity> createAllSchoolContacts(List<SchoolContactEntity> schoolContacts) {
        return schoolContactRepository.saveAll(schoolContacts);
    }

    public List<SchoolContactEntity> getAllSchoolContacts() {
        return schoolContactRepository.findAll();
    }

    public Optional<SchoolContactEntity> getSchoolContactById(Long id) {
        return schoolContactRepository.findById(id);
    }

    public SchoolContactEntity updateSchoolContact(Long id, SchoolContactEntity updatedContact) {
        Optional<SchoolContactEntity> existingContactOptional = schoolContactRepository.findById(id);

        if (existingContactOptional.isEmpty()) {
            throw new RuntimeException("School Contact not found with ID: " + id);
        }

        SchoolContactEntity existingContact = existingContactOptional.get();

        existingContact.setLandline(updatedContact.getLandline());
        existingContact.setSchoolHead(updatedContact.getSchoolHead());
        existingContact.setSchoolHeadNumber(updatedContact.getSchoolHeadNumber());
        existingContact.setSchoolHeadEmail(updatedContact.getSchoolHeadEmail());
        existingContact.setDesignation(updatedContact.getDesignation());
        existingContact.setPropertyCustodian(updatedContact.getPropertyCustodian());
        existingContact.setPropertyCustodianNumber(updatedContact.getPropertyCustodianNumber());
        existingContact.setPropertyCustodianEmail(updatedContact.getPropertyCustodianEmail());

        return schoolContactRepository.save(existingContact);
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
