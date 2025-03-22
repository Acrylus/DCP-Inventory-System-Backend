package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.SchoolNTCEntity;
import com.example.DCP.Inventory.System.Service.SchoolNTCService;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/school_ntc")
public class SchoolNTCController {

    private final SchoolNTCService schoolNTCService;

    public SchoolNTCController(SchoolNTCService schoolNTCService) {
        this.schoolNTCService = schoolNTCService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createSchoolNTC(@RequestBody SchoolNTCEntity schoolNTC) {
        try {
            SchoolNTCEntity createdSchoolNTC = schoolNTCService.createSchoolNTC(schoolNTC);
            return Response.response(HttpStatus.CREATED, "School NTC created successfully", createdSchoolNTC);
        } catch (Exception e) {
            e.printStackTrace();
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create School NTC");
        }
    }

    @PostMapping("/create_all")
    public ResponseEntity<Object> createAllSchoolNTC(@RequestBody List<SchoolNTCEntity> schoolNTCs) {
        try {
            List<SchoolNTCEntity> createdSchoolNTCs = schoolNTCService.createAllSchoolNTC(schoolNTCs);
            return Response.response(HttpStatus.CREATED, "School NTCs created successfully", createdSchoolNTCs);
        } catch (Exception e) {
            e.printStackTrace();
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create School NTCs");
        }
    }

    @GetMapping("/get_all")
    public ResponseEntity<Object> getAllSchoolNTCs() {
        try {
            List<SchoolNTCEntity> schoolNTCs = schoolNTCService.getAllSchoolNTCs();
            return Response.response(HttpStatus.OK, "School NTCs fetched successfully", schoolNTCs);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch School NTCs");
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getSchoolNTCById(@PathVariable Long id) {
        try {
            SchoolNTCEntity schoolNTC = schoolNTCService.getSchoolNTCById(id)
                    .orElseThrow(() -> new RuntimeException("School energy not found"));
            return Response.response(HttpStatus.OK, "School NTC fetched successfully", schoolNTC);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "School NTC not found");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateSchoolNTC(@PathVariable Long id, @RequestBody SchoolNTCEntity updatedNTC) {
        try {
            SchoolNTCEntity updatedSchoolNTC = schoolNTCService.updateSchoolNTC(id, updatedNTC);
            return Response.response(HttpStatus.OK, "School NTC updated successfully", updatedSchoolNTC);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "School NTC not found");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteSchoolNTC(@PathVariable Long id) {
        try {
            schoolNTCService.deleteSchoolNTC(id);
            return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "School NTC deleted successfully");
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete School NTC");
        }
    }

    @GetMapping("/school/{schoolRecordId}")
    public ResponseEntity<SchoolNTCEntity> getSchoolNTCBySchoolRecord(@PathVariable Long schoolRecordId) {
        SchoolNTCEntity schoolNTC = schoolNTCService.getSchoolNTCBySchoolRecordId(schoolRecordId);
        return ResponseEntity.ok(schoolNTC);
    }
}
