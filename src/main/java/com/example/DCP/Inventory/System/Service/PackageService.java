package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.PackageEntity;
import com.example.DCP.Inventory.System.Exception.ResourceNotFoundException;
import com.example.DCP.Inventory.System.Repository.PackageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PackageService {

    private final PackageRepository packageRepository;

    public PackageService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public List<PackageEntity> getAllPackages() {
        return packageRepository.findAll();
    }

    public PackageEntity getPackageById(Long id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));
    }

    public PackageEntity savePackage(PackageEntity packageEntity) {
        return packageRepository.save(packageEntity);
    }

    public void deletePackage(Long schoolBatchId, Long packageId) {
        PackageEntity packageEntity = packageRepository.findById_SchoolBatchListIdAndId_PackageId(schoolBatchId, packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        packageRepository.delete(packageEntity);
    }

    public void saveAll(List<PackageEntity> packages) {
        packageRepository.saveAll(packages);
    }

    public List<PackageEntity> getPackageBySchoolBatchId(Long schoolBatchId) {
        return packageRepository.findBySchoolBatchList_SchoolBatchId(schoolBatchId);
    }

    public List<PackageEntity> getPackageByConfigurationId(Long configurationId) {
        return packageRepository.findByConfiguration_ConfigurationId(configurationId);
    }

    public PackageEntity getUniquePackage(Long schoolBatchListId, Long packageId) {
        return packageRepository.findById_SchoolBatchListIdAndId_PackageId(schoolBatchListId, packageId)
                .orElseThrow(() -> new RuntimeException("Package not found for schoolBatchListId: " + schoolBatchListId + " and packageId: " + packageId));
    }

    public PackageEntity updatePackage(Long schoolBatchId, Long packageId, PackageEntity updatedPackage) {
        PackageEntity existingPackage = packageRepository.findById_SchoolBatchListIdAndId_PackageId(schoolBatchId, packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        existingPackage.setStatus(updatedPackage.getStatus());
        existingPackage.setComponent(updatedPackage.getComponent());
        existingPackage.setSerialNumber(updatedPackage.getSerialNumber());
        existingPackage.setAssigned(updatedPackage.getAssigned());
        existingPackage.setRemarks(updatedPackage.getRemarks());

        return packageRepository.save(existingPackage);
    }

    public PackageEntity patchPackage(Long schoolBatchId, Long packageId, Map<String, Object> updates) {
        PackageEntity existingPackage = packageRepository.findById_SchoolBatchListIdAndId_PackageId(schoolBatchId, packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "status":
                    existingPackage.setStatus((String) value);
                    break;
                case "component":
                    existingPackage.setComponent((String) value);
                    break;
                case "serialNumber":
                    existingPackage.setSerialNumber((String) value);
                    break;
                case "assigned":
                    existingPackage.setAssigned((String) value);
                    break;
                case "remarks":
                    existingPackage.setRemarks((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        return packageRepository.save(existingPackage);
    }
}
