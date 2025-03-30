package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.PackageEntity;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import com.example.DCP.Inventory.System.Service.PackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @DeleteMapping("/delete/{packageId}/school_batch/{schoolBatchId}")
    public ResponseEntity<String> deletePackage(
            @PathVariable Long packageId,
            @PathVariable Long schoolBatchId) {
        packageService.deletePackage(schoolBatchId, packageId);
        return ResponseEntity.ok("Package deleted successfully.");
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

    @GetMapping("/school_batch_list/{schoolBatchId}")
    public ResponseEntity<List<PackageEntity>> getPackageBySchoolBatchList(@PathVariable Long schoolBatchId) {
        List<PackageEntity> packageSchoolBatchList = packageService.getPackageBySchoolBatchId(schoolBatchId);
        return ResponseEntity.ok(packageSchoolBatchList);
    }

    @GetMapping("/configuration/{configurationId}")
    public ResponseEntity<List<PackageEntity>> getPackageByConfiguration(@PathVariable Long configurationId) {
        List<PackageEntity> packageConfiguration = packageService.getPackageByConfigurationId(configurationId);
        return ResponseEntity.ok(packageConfiguration);
    }

    @GetMapping("/get/{packageId}/school_batch/{schoolBatchId}")
    public PackageEntity getUniquePackage(@PathVariable Long packageId, @PathVariable Long schoolBatchId) {
        return packageService.getUniquePackage(schoolBatchId, packageId);
    }

    @PutMapping("/update/{packageId}/school_batch/{schoolBatchId}")
    public ResponseEntity<PackageEntity> updatePackage(@PathVariable Long packageId, @PathVariable Long schoolBatchId, @RequestBody PackageEntity updatedPackage) {

        PackageEntity updated = packageService.updatePackage(schoolBatchId, packageId, updatedPackage);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/patch/{packageId}/school_batch/{schoolBatchId}")
    public ResponseEntity<PackageEntity> patchPackage(
            @PathVariable Long packageId,
            @PathVariable Long schoolBatchId,
            @RequestBody Map<String, Object> updates) {
        PackageEntity updatedPackage = packageService.patchPackage(schoolBatchId, packageId, updates);
        return ResponseEntity.ok(updatedPackage);
    }

    @PutMapping("/update/school_batch/{schoolBatchId}")
    public ResponseEntity<Object> updatePackagesBySchoolBatchId(
            @PathVariable Long schoolBatchId,
            @RequestBody List<PackageEntity> updatedPackages) {

        try {
            List<PackageEntity> result = packageService.updatePackagesBySchoolBatchId(schoolBatchId, updatedPackages);

            if (result.isEmpty()) {
                return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "No packages found for the given school batch ID");
            }

            return Response.response(HttpStatus.OK, "Packages updated successfully", result);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating packages");
        }
    }


}
