package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.DivisionEntity;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import com.example.DCP.Inventory.System.Service.DivisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/division")
public class DivisionController {

    @Autowired
    private DivisionService divisionService;

    // Get all divisions
    @GetMapping("/get_all")
    public ResponseEntity<Object> getAllDivisions() {
        List<DivisionEntity> divisions = divisionService.getAllDivisions();
        return Response.response(HttpStatus.OK, "Divisions found", divisions);
    }

    // Get division by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getDivisionById(@PathVariable Long id) {
        DivisionEntity division = divisionService.getDivisionById(id);
        if (division != null) {
            return Response.response(HttpStatus.OK, "Division found", division);
        } else {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Division not found");
        }
    }

    // Create a new division
    @PostMapping("/create")
    public ResponseEntity<Object> createDivision(@RequestBody DivisionEntity division) {
        DivisionEntity createdDivision = divisionService.createDivision(division);
        return Response.response(HttpStatus.CREATED, "Division created successfully", createdDivision);
    }

    // Update division by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateDivision(@PathVariable Long id, @RequestBody DivisionEntity divisionDetails) {
        try {
            DivisionEntity updatedDivision = divisionService.updateDivision(id, divisionDetails);
            return Response.response(HttpStatus.OK, "Division updated successfully", updatedDivision);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Division not found");
        }
    }

    // Delete division by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteDivision(@PathVariable Long id) {
        try {
            divisionService.deleteDivision(id);
            return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "Division deleted successfully");
        } catch (RuntimeException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Division not found");
        }
    }
}
