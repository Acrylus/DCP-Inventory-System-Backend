package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.SchoolEnergyEntity;
import com.example.DCP.Inventory.System.Service.SchoolEnergyService;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/school-energy")
public class SchoolEnergyController {

    private final SchoolEnergyService schoolEnergyService;

    public SchoolEnergyController(SchoolEnergyService schoolEnergyService) {
        this.schoolEnergyService = schoolEnergyService;
    }

    @GetMapping("/get_all")
    public ResponseEntity<Object> getAllSchoolEnergy() {
        try {
            List<SchoolEnergyEntity> schoolEnergies = schoolEnergyService.getAllSchoolEnergy();
            return Response.response(HttpStatus.OK, "School energies fetched successfully", schoolEnergies);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch school energies");
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getSchoolEnergyById(@PathVariable Long id) {
        try {
            SchoolEnergyEntity schoolEnergy = schoolEnergyService.getSchoolEnergyById(id)
                    .orElseThrow(() -> new RuntimeException("School energy not found"));
            return Response.response(HttpStatus.OK, "School energy fetched successfully", schoolEnergy);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "School energy not found");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createSchoolEnergy(@RequestBody SchoolEnergyEntity schoolEnergy) {
        try {
            SchoolEnergyEntity createdSchoolEnergy = schoolEnergyService.createSchoolEnergy(schoolEnergy);
            return Response.response(HttpStatus.CREATED, "School energy created successfully", createdSchoolEnergy);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create school energy");
        }
    }

    @PostMapping("/create_all")
    public ResponseEntity<Object> createAllSchoolEnergies(@RequestBody List<SchoolEnergyEntity> schoolEnergies) {
        try {
            List<SchoolEnergyEntity> createdSchoolEnergies = schoolEnergyService.createAllSchoolEnergies(schoolEnergies);
            return Response.response(HttpStatus.CREATED, "School energies created successfully", createdSchoolEnergies);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create school energies");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateSchoolEnergy(@PathVariable Long id, @RequestBody SchoolEnergyEntity updatedEnergy) {
        try {
            SchoolEnergyEntity updatedSchoolEnergy = schoolEnergyService.updateSchoolEnergy(id, updatedEnergy);
            return Response.response(HttpStatus.OK, "School energy updated successfully", updatedSchoolEnergy);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "School energy not found");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteSchoolEnergy(@PathVariable Long id) {
        try {
            schoolEnergyService.deleteSchoolEnergy(id);
            return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "School energy deleted successfully");
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete school energy");
        }
    }

    // New: Get total count of records
    @GetMapping("/count")
    public ResponseEntity<Object> countSchoolEnergy() {
        try {
            long count = schoolEnergyService.countSchoolEnergy();
            return Response.response(HttpStatus.OK, "Total school energy records", count);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch count");
        }
    }

    // New: Get records by localGridSupply
    @GetMapping("/by-grid-supply/{localGridSupply}")
    public ResponseEntity<Object> getByLocalGridSupply(@PathVariable String localGridSupply) {
        try {
            List<SchoolEnergyEntity> schoolEnergies = schoolEnergyService.findByLocalGridSupply(localGridSupply);
            return Response.response(HttpStatus.OK, "School energies fetched successfully", schoolEnergies);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "No records found for the given localGridSupply");
        }
    }

    // New: Check if an ID exists
    @GetMapping("/exists/{id}")
    public ResponseEntity<Object> existsById(@PathVariable Long id) {
        try {
            boolean exists = schoolEnergyService.existsById(id);
            return Response.response(HttpStatus.OK, "Existence check", exists);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to check existence");
        }
    }
}
