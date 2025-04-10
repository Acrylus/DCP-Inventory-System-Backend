package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.*;
import com.example.DCP.Inventory.System.Repository.BatchRepository;
import com.example.DCP.Inventory.System.Repository.ConfigurationRepository;
import com.example.DCP.Inventory.System.Repository.PackageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatchService {

    private final BatchRepository batchRepository;
    private final PackageRepository packageRepository;
    private final ConfigurationRepository configurationRepository;

    public BatchService(BatchRepository batchRepository, PackageRepository packageRepository, ConfigurationRepository configurationRepository) {
        this.batchRepository = batchRepository;
        this.packageRepository = packageRepository;
        this.configurationRepository = configurationRepository;
    }

    public List<BatchEntity> getAllBatches() {
        List<BatchEntity> batches = batchRepository.findAll();

        for (BatchEntity batch : batches) {
            int totalPackages = batch.getSchoolBatchList().stream()
                    .mapToInt(SchoolBatchListEntity::getNumberOfPackage)
                    .sum();

            batch.setNumberOfPackage(totalPackages);
        }

        return batchRepository.saveAll(batches);
    }

    public BatchEntity getBatchById(Long id) {
        BatchEntity batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        int totalPackages = batch.getSchoolBatchList().stream()
                .mapToInt(SchoolBatchListEntity::getNumberOfPackage)
                .sum();

        batch.setNumberOfPackage(totalPackages);

        return batchRepository.save(batch);
    }

    @Transactional
    public BatchEntity updateBatch(Long id, BatchEntity batchDetails) {
        // **Step 1: Fetch Existing Batch**
        BatchEntity existingBatch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with id: " + id));

        boolean configurationsChanged = !existingBatch.getConfigurations().equals(batchDetails.getConfigurations());

        if (configurationsChanged) {
            // **Step 2: Delete All Existing Configurations**
            List<ConfigurationEntity> oldConfigs = configurationRepository.findByBatch_BatchId(existingBatch.getBatchId());
            configurationRepository.deleteAll(oldConfigs);

            // Clear in-memory reference to sync with orphanRemoval
            existingBatch.getConfigurations().clear();

            // **Step 2.5: Delete All Related Packages**
            for (SchoolBatchListEntity schoolBatchList : existingBatch.getSchoolBatchList()) {
                List<PackageEntity> packages = packageRepository.findBySchoolBatchList_SchoolBatchId(schoolBatchList.getSchoolBatchId());
                packageRepository.deleteAll(packages);
            }
        }

        // **Step 3: Update Batch Fields**
        existingBatch.setBatchName(batchDetails.getBatchName());
        existingBatch.setBudgetYear(batchDetails.getBudgetYear());
        existingBatch.setDeliveryYear(batchDetails.getDeliveryYear());
        existingBatch.setPrice(batchDetails.getPrice());
        existingBatch.setSupplier(batchDetails.getSupplier());
        existingBatch.setNumberOfPackage(batchDetails.getNumberOfPackage());
        existingBatch.setRemarks(batchDetails.getRemarks());

        // **Step 4: Handle New Configurations**
        List<ConfigurationEntity> newConfigurations = batchDetails.getConfigurations();
        if (newConfigurations != null && !newConfigurations.isEmpty()) {
            Long batchId = existingBatch.getBatchId();
            Long maxConfigId = configurationRepository.findMaxConfigIdForBatch(batchId);
            long nextConfigId = (maxConfigId != null) ? maxConfigId + 1 : 1;

            for (ConfigurationEntity config : newConfigurations) {
                ConfigurationIdEntity configId = new ConfigurationIdEntity();
                configId.setBatchId(batchId);
                configId.setConfigurationId(nextConfigId++);

                config.setId(configId);
                config.setBatch(existingBatch);
            }

            existingBatch.getConfigurations().addAll(newConfigurations);
        }

        // **Step 5: Save the Updated Batch**
        BatchEntity updatedBatch = batchRepository.save(existingBatch);

        // **Step 6: Save New Configurations (if any)**
        if (newConfigurations != null && !newConfigurations.isEmpty()) {
            configurationRepository.saveAll(newConfigurations);
        }

        // **Step 7: If Configurations Changed, Recreate Packages**
        if (configurationsChanged) {
            for (SchoolBatchListEntity schoolBatchList : updatedBatch.getSchoolBatchList()) {
                List<ConfigurationEntity> updatedConfigurations = updatedBatch.getConfigurations();
                List<PackageEntity> newPackages = new ArrayList<>();

                Long maxPackageId = packageRepository.findMaxPackageIdBySchoolBatchList(schoolBatchList.getSchoolBatchId());
                long packageId = (maxPackageId != null) ? maxPackageId + 1 : 1;

                for (ConfigurationEntity config : updatedConfigurations) {
                    for (int i = 0; i < schoolBatchList.getNumberOfPackage(); i++) {
                        PackageIdEntity packageIdEntity = new PackageIdEntity();
                        packageIdEntity.setPackageId(packageId++);
                        packageIdEntity.setSchoolBatchListId(schoolBatchList.getSchoolBatchId());

                        PackageEntity packageEntity = new PackageEntity();
                        packageEntity.setId(packageIdEntity);
                        packageEntity.setSchoolBatchList(schoolBatchList);
                        packageEntity.setConfiguration(config);
                        packageEntity.setStatus("Pending");

                        newPackages.add(packageEntity);
                    }
                }

                if (!newPackages.isEmpty()) {
                    packageRepository.saveAll(newPackages);
                }
            }
        }

        return updatedBatch;
    }

    public BatchEntity saveBatch(BatchEntity batch) {
        // Step 1: Save the Batch First without configurations
        List<ConfigurationEntity> newConfigurations = batch.getConfigurations();
        batch.setConfigurations(new ArrayList<>()); // Temporarily remove configurations
        BatchEntity savedBatch = batchRepository.save(batch);

        // Step 2: If there are configurations, associate them with the saved batch

        if (newConfigurations != null && !newConfigurations.isEmpty()) {
            Long batchId = savedBatch.getBatchId();  // Get the saved batch ID

            // Step 3: Assign Configuration IDs and associate them with the batch
            Long maxConfigId = configurationRepository.findMaxConfigIdForBatch(batchId);
            long nextConfigId = (maxConfigId != null) ? maxConfigId + 1 : 1;

            for (ConfigurationEntity config : newConfigurations) {
                // Create a ConfigurationIdEntity and assign the batchId and new configId
                ConfigurationIdEntity configId = new ConfigurationIdEntity();
                configId.setBatchId(batchId);
                configId.setConfigurationId(nextConfigId++);

                // Set the generated ID to the configuration entity
                config.setId(configId);
                config.setBatch(savedBatch);  // Associate the configuration with the batch
            }

            // Step 4: Save configurations separately
            configurationRepository.saveAll(newConfigurations);

            // Step 5: Add configurations back to the batch's list (optional)
            savedBatch.getConfigurations().addAll(newConfigurations);
            batchRepository.save(savedBatch);  // Save the batch again with the updated configurations
        }

        return savedBatch;  // Return the saved batch
    }



    public void deleteBatch(Long id) {
        batchRepository.deleteById(id);
    }

    public void saveAll(List<BatchEntity> batches) {
        List<BatchEntity> savedBatches = new ArrayList<>();
        List<ConfigurationEntity> allConfigurations = new ArrayList<>();

        for (BatchEntity batch : batches) {
            List<ConfigurationEntity> newConfigurations = batch.getConfigurations();
            batch.setConfigurations(new ArrayList<>());
            BatchEntity savedBatch = batchRepository.save(batch);
            savedBatches.add(savedBatch);

            if (newConfigurations != null && !newConfigurations.isEmpty()) {
                Long batchId = savedBatch.getBatchId();
                Long maxConfigId = configurationRepository.findMaxConfigIdForBatch(batchId);
                long nextConfigId = (maxConfigId != null) ? maxConfigId + 1 : 1;

                for (ConfigurationEntity config : newConfigurations) {
                    ConfigurationIdEntity configId = new ConfigurationIdEntity();
                    configId.setBatchId(batchId);
                    configId.setConfigurationId(nextConfigId++);
                    config.setId(configId);
                    config.setBatch(savedBatch);
                }

                allConfigurations.addAll(newConfigurations);
                savedBatch.getConfigurations().addAll(newConfigurations);
            }
        }

        if (!allConfigurations.isEmpty()) {
            configurationRepository.saveAll(allConfigurations);
        }

        batchRepository.saveAll(savedBatches);  // Final save to include configurations
    }
}
