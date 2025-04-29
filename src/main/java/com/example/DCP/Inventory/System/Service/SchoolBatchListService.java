package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.*;
import com.example.DCP.Inventory.System.Repository.BatchRepository;
import com.example.DCP.Inventory.System.Repository.PackageRepository;
import com.example.DCP.Inventory.System.Repository.SchoolBatchListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolBatchListService {
    private final SchoolBatchListRepository schoolBatchListRepository;
    private final PackageRepository packageRepository;
    private final BatchRepository batchRepository;

    public SchoolBatchListService(SchoolBatchListRepository schoolBatchListRepository, PackageRepository packageRepository, BatchRepository batchRepository) {
        this.schoolBatchListRepository = schoolBatchListRepository;
        this.packageRepository = packageRepository;
        this.batchRepository = batchRepository;
    }

    public List<SchoolBatchListEntity> getAllSchoolBatchLists() {
        return schoolBatchListRepository.findAll();
    }

    public SchoolBatchListEntity getSchoolBatchListById(Long id) {
        return schoolBatchListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch School List not found"));
    }

    @Transactional
    public SchoolBatchListEntity updateSchoolBatchList(Long id, SchoolBatchListEntity updatedBatchList) {
        // Step 1: Fetch Existing SchoolBatchList
        SchoolBatchListEntity existingBatchList = schoolBatchListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School Batch List not found with id: " + id));

        // ✅ Store old number of packages BEFORE updating
        int oldNumberOfPackages = existingBatchList.getNumberOfPackage();
        int newNumberOfPackages = updatedBatchList.getNumberOfPackage();

        // Step 2: Update School Batch List Fields
        existingBatchList.setBatch(updatedBatchList.getBatch());
        existingBatchList.setSchool(updatedBatchList.getSchool());
        existingBatchList.setDeliveryDate(updatedBatchList.getDeliveryDate());
        existingBatchList.setNumberOfPackage(newNumberOfPackages);
        existingBatchList.setStatus(updatedBatchList.getStatus());
        existingBatchList.setKeyStage(updatedBatchList.getKeyStage());
        existingBatchList.setRemarks(updatedBatchList.getRemarks());
        existingBatchList.setAccountable(updatedBatchList.getAccountable());

        // Step 3: Save Updated School Batch List
        SchoolBatchListEntity savedBatchList = schoolBatchListRepository.save(existingBatchList);

        // Step 4: If the number increased, generate new packages
        if (newNumberOfPackages > oldNumberOfPackages) {
            int packagesToAddMultiplier = newNumberOfPackages - oldNumberOfPackages;

            Long batchId = savedBatchList.getBatch().getBatchId();
            BatchEntity batch = batchRepository.findById(batchId)
                    .orElseThrow(() -> new RuntimeException("Batch not found with ID: " + batchId));
            List<ConfigurationEntity> configurations = batch.getConfigurations();

            List<PackageEntity> newPackages = new ArrayList<>();
            Long maxPackageId = packageRepository.findMaxPackageIdBySchoolBatchList(savedBatchList.getSchoolBatchId());
            long packageId = (maxPackageId != null) ? maxPackageId + 1 : 1;

            for (ConfigurationEntity config : configurations) {
                int packagesForThisConfig = config.getQuantity() * packagesToAddMultiplier;

                for (int i = 0; i < packagesForThisConfig; i++) {
                    PackageIdEntity packageIdEntity = new PackageIdEntity();
                    packageIdEntity.setPackageId(packageId++);
                    packageIdEntity.setSchoolBatchListId(savedBatchList.getSchoolBatchId());

                    PackageEntity packageEntity = new PackageEntity();
                    packageEntity.setId(packageIdEntity);
                    packageEntity.setSchoolBatchList(savedBatchList);
                    packageEntity.setConfiguration(config);
                    packageEntity.setStatus("Pending");

                    newPackages.add(packageEntity);
                }
            }

            packageRepository.saveAll(newPackages);
        }

        return savedBatchList;
    }


    @Transactional
    public SchoolBatchListEntity saveSchoolBatchList(SchoolBatchListEntity schoolBatchList) {
        SchoolBatchListEntity savedSchoolBatchList = schoolBatchListRepository.save(schoolBatchList);

        Long batchId = schoolBatchList.getBatch().getBatchId();
        BatchEntity batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found with ID: " + batchId));

        List<ConfigurationEntity> configurations = batch.getConfigurations();
        List<PackageEntity> packages = new ArrayList<>();

        // Get the current max packageId for the schoolBatchList to avoid collisions
        Long maxPackageId = packageRepository.findMaxPackageIdBySchoolBatchList(savedSchoolBatchList.getSchoolBatchId());
        long packageId = (maxPackageId != null) ? maxPackageId + 1 : 1;

        for (ConfigurationEntity config : configurations) {
            for (int i = 0; i < savedSchoolBatchList.getNumberOfPackage() * config.getQuantity(); i++) {
                PackageIdEntity packageIdEntity = new PackageIdEntity();
                packageIdEntity.setPackageId(packageId++);
                packageIdEntity.setSchoolBatchListId(savedSchoolBatchList.getSchoolBatchId());

                PackageEntity packageEntity = new PackageEntity();
                packageEntity.setId(packageIdEntity);
                packageEntity.setSchoolBatchList(savedSchoolBatchList);
                packageEntity.setConfiguration(config);
                packageEntity.setStatus("Pending");

                packages.add(packageEntity);
            }
        }

        packageRepository.saveAll(packages);
        return savedSchoolBatchList;
    }


    public void deleteSchoolBatchList(Long id) {
        schoolBatchListRepository.deleteById(id);
    }

    @Transactional
    public List<SchoolBatchListEntity> saveAll(List<SchoolBatchListEntity> schoolBatchLists) {
        List<SchoolBatchListEntity> savedSchoolBatchLists = schoolBatchListRepository.saveAll(schoolBatchLists);
        List<PackageEntity> packages = new ArrayList<>();

        for (SchoolBatchListEntity schoolBatchList : savedSchoolBatchLists) {
            Long batchId = schoolBatchList.getBatch().getBatchId();
            BatchEntity batch = batchRepository.findById(batchId)
                    .orElseThrow(() -> new RuntimeException("Batch not found with ID: " + batchId));

            List<ConfigurationEntity> configurations = batch.getConfigurations();

            // Get the max packageId for the current schoolBatchListId
            Long maxPackageId = packageRepository.findMaxPackageIdBySchoolBatchList(schoolBatchList.getSchoolBatchId());
            long packageId = (maxPackageId != null) ? maxPackageId + 1 : 1;

            for (ConfigurationEntity config : configurations) {
                for (int i = 0; i < schoolBatchList.getNumberOfPackage() * config.getQuantity(); i++) {
                    PackageIdEntity packageIdEntity = new PackageIdEntity();
                    packageIdEntity.setPackageId(packageId++);
                    packageIdEntity.setSchoolBatchListId(schoolBatchList.getSchoolBatchId());

                    PackageEntity packageEntity = new PackageEntity();
                    packageEntity.setId(packageIdEntity);
                    packageEntity.setSchoolBatchList(schoolBatchList);
                    packageEntity.setConfiguration(config);
                    packageEntity.setStatus("Pending");

                    packages.add(packageEntity);
                }
            }
        }

        packageRepository.saveAll(packages);
        return savedSchoolBatchLists; // ✅ Returning the saved school batch list
    }


    public List<SchoolBatchListEntity> getSchoolBatchListByBatchId(Long batchId) {
        return schoolBatchListRepository.findByBatch_BatchId(batchId);
    }

    public List<SchoolBatchListEntity> getSchoolBatchListBySchoolRecordId(Long schoolRecordId) {
        return schoolBatchListRepository.findBySchool_SchoolRecordId(schoolRecordId);
    }
}
