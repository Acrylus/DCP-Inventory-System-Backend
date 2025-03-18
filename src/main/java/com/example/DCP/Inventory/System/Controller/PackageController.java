package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import com.example.DCP.Inventory.System.Entity.PackageEntity;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import com.example.DCP.Inventory.System.Service.PackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/package")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    // Get all packages
    @GetMapping("/get_all")
    public List<PackageEntity> getAllPackages() {
        return packageService.getAllPackages();
    }

    // Get package by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getPackageById(@PathVariable Long id) {
        PackageEntity packageEntity = packageService.getPackageById(id);
        return Response.response(HttpStatus.OK, "Package found", packageEntity);
    }

    // Create a new package
    @PostMapping("/create")
    public ResponseEntity<Object> createPackage(@RequestBody PackageEntity packageEntity) {
        PackageEntity createdPackage = packageService.savePackage(packageEntity);
        return Response.response(HttpStatus.CREATED, "Package created successfully", createdPackage);
    }

    // Update package by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updatePackage(@PathVariable Long id, @RequestBody PackageEntity packageDetails) {
        PackageEntity updatedPackage = packageService.updatePackage(id, packageDetails);
        return Response.response(HttpStatus.OK, "Package updated successfully", updatedPackage);
    }

    // Delete package by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "Package deleted successfully");
    }

    @PostMapping("/create_all")
    public ResponseEntity<Object> addPackages(@RequestBody List<PackageEntity> packages) {
        try {
            packageService.saveAll(packages);
            return ResponseEntity.status(HttpStatus.CREATED).body("Configurations added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add Configurations");
        }
    }
}
