package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.PackageEntity;
import com.example.DCP.Inventory.System.Repository.PackageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public PackageEntity updatePackage(Long id, PackageEntity packageDetails) {
        Optional<PackageEntity> existingPackageOptional = packageRepository.findById(id);

        if (existingPackageOptional.isEmpty()) {
            throw new RuntimeException("Package not found with id: " + id);
        }

        PackageEntity existingPackage = existingPackageOptional.get();

        existingPackage.setItem(packageDetails.getItem());
        existingPackage.setStatus(packageDetails.getStatus());
        existingPackage.setComponent(packageDetails.getComponent());
        existingPackage.setSerialNumber(packageDetails.getSerialNumber());
        existingPackage.setAssigned(packageDetails.getAssigned());
        existingPackage.setRemarks(packageDetails.getRemarks());
        existingPackage.setSchoolBatchList(packageDetails.getSchoolBatchList());
        existingPackage.setConfiguration(packageDetails.getConfiguration());

        return packageRepository.save(existingPackage);
    }

    public PackageEntity savePackage(PackageEntity packageEntity) {
        return packageRepository.save(packageEntity);
    }

    public void deletePackage(Long id) {
        packageRepository.deleteById(id);
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
}
