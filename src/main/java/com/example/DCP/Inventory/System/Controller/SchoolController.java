package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import com.example.DCP.Inventory.System.Service.SchoolService;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/school")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping("/get_all")
    public ResponseEntity<Object> getAllSchools() {
        try {
            List<SchoolEntity> schools = schoolService.getAllSchools();
            return Response.response(HttpStatus.OK, "Schools fetched successfully", schools);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch schools");
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getSchoolById(@PathVariable Long id) {
        try {
            SchoolEntity school = schoolService.getSchoolById(id);
            return Response.response(HttpStatus.OK, "School fetched successfully", school);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "School not found");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createSchool(@RequestBody SchoolEntity school) {
        try {
            SchoolEntity createdSchool = schoolService.saveSchool(school);
            return Response.response(HttpStatus.CREATED, "School created successfully", createdSchool);
        } catch (Exception e) {
            e.printStackTrace();
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create school");
        }
    }

    @PostMapping("/create_all")
    public ResponseEntity<Object> createAllSchools(@RequestBody List<SchoolEntity> schools) {
        try {
            List<SchoolEntity> createdSchools = schoolService.createAllSchools(schools);
            return Response.response(HttpStatus.CREATED, "Schools created successfully", createdSchools);
        } catch (Exception e) {
            e.printStackTrace();
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create schools");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateSchool(@PathVariable Long id, @RequestBody SchoolEntity schoolDetails) {
        try {
            SchoolEntity updatedSchool = schoolService.updateSchool(id, schoolDetails);
            return Response.response(HttpStatus.OK, "School updated successfully", updatedSchool);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "School not found");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteSchool(@PathVariable Long id) {
        try {
            schoolService.deleteSchool(id);
            return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "School deleted successfully");
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete school");
        }
    }
}
