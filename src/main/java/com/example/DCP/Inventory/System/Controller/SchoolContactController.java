package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.SchoolContactDTOEntity;
import com.example.DCP.Inventory.System.Entity.SchoolContactEntity;
import com.example.DCP.Inventory.System.Service.SchoolContactService;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/school_contact")
public class SchoolContactController {

    private final SchoolContactService schoolContactService;

    public SchoolContactController(SchoolContactService schoolContactService) {
        this.schoolContactService = schoolContactService;
    }

    @GetMapping("/get_all")
    public ResponseEntity<Object> getAllSchoolContacts() {
        try {
            List<SchoolContactDTOEntity> contacts = schoolContactService.getAllSchoolContacts();
            return Response.response(HttpStatus.OK, "School contacts fetched successfully", contacts);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch school contacts");
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getSchoolContactById(@PathVariable Long id) {
        try {
            Optional<SchoolContactEntity> contact = schoolContactService.getSchoolContactById(id);
            if (contact.isPresent()) {
                return Response.response(HttpStatus.OK, "School contact fetched successfully", contact.get());
            } else {
                return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "School contact not found");
            }
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch school contact");
        }
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createSchoolContact(@RequestBody SchoolContactEntity schoolContact) {
        try {
            SchoolContactEntity createdContact = schoolContactService.createSchoolContact(schoolContact);
            return Response.response(HttpStatus.CREATED, "School contact created successfully", createdContact);
        } catch (Exception e) {
            e.printStackTrace();
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create school contact");
        }
    }

    @PostMapping("/create_all")
    public ResponseEntity<Object> createAllSchoolContacts(@RequestBody List<SchoolContactEntity> schoolContacts) {
        try {
            List<SchoolContactEntity> createdContacts = schoolContactService.createAllSchoolContacts(schoolContacts);
            return Response.response(HttpStatus.CREATED, "School contacts created successfully", createdContacts);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create school contacts");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateSchoolContact(@PathVariable Long id, @RequestBody SchoolContactEntity updatedContact) {
        try {
            SchoolContactEntity updated = schoolContactService.updateSchoolContact(id, updatedContact);
            return Response.response(HttpStatus.OK, "School contact updated successfully", updated);
        } catch (Exception e) {
            e.printStackTrace();
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "School contact not found");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteSchoolContact(@PathVariable Long id) {
        try {
            schoolContactService.deleteSchoolContact(id);
            return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "School contact deleted successfully");
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete school contact");
        }
    }

    @GetMapping("/school/{schoolRecordId}")
    public ResponseEntity<SchoolContactEntity> getSchoolBatchListBySchool(@PathVariable Long schoolRecordId) {
        SchoolContactEntity schoolContacts = schoolContactService.getSchoolBatchListBySchoolRecordId(schoolRecordId);
        return ResponseEntity.ok(schoolContacts);
    }
}
