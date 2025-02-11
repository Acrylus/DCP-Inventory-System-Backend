package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.DistrictEntity;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import com.example.DCP.Inventory.System.Service.DistrictService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/district")
public class DistrictController {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    // Get all districts
    @GetMapping("/get_all")
    public ResponseEntity<Object> getAllDistricts() {
        List<DistrictEntity> districts = districtService.getAllDistricts();
        return Response.response(HttpStatus.OK, "Districts found", districts);
    }

    // Get district by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getDistrictById(@PathVariable Long id) {
        DistrictEntity district = districtService.getDistrictById(id);
        if (district != null) {
            return Response.response(HttpStatus.OK, "District found", district);
        } else {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "District not found");
        }
    }

    // Create a new district
    @PostMapping("/create")
    public ResponseEntity<Object> createDistrict(@RequestBody DistrictEntity district) {
        DistrictEntity createdDistrict = districtService.saveDistrict(district);
        return Response.response(HttpStatus.CREATED, "District created successfully", createdDistrict);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateDistrict(
            @PathVariable Long id,
            @RequestBody DistrictEntity districtDetails) {
        try {
            DistrictEntity updatedDistrict = districtService.updateDistrict(id, districtDetails);
            return Response.response(HttpStatus.OK, "District updated successfully", updatedDistrict);
        } catch (RuntimeException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "District not found");
        }
    }

    // Delete district by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteDistrict(@PathVariable Long id) {
        try {
            districtService.deleteDistrict(id);
            return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "District deleted successfully");
        } catch (RuntimeException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "District not found");
        }
    }

    // Save multiple districts at once
    @PostMapping("/create_all")
    public ResponseEntity<String> addDistricts(@RequestBody List<DistrictEntity> districts) {
        try {
            districtService.saveAll(districts);
            return ResponseEntity.status(HttpStatus.CREATED).body("Districts added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add districts");
        }
    }
}
