package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.*;
import com.example.DCP.Inventory.System.Exception.ResourceNotFoundException;
import com.example.DCP.Inventory.System.Repository.ConfigurationRepository;
import com.example.DCP.Inventory.System.Repository.PackageRepository;
import com.example.DCP.Inventory.System.Repository.SchoolBatchListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final PackageRepository packageRepository;
    private final SchoolBatchListRepository schoolBatchListRepository;

    public ConfigurationService(ConfigurationRepository configurationRepository, PackageRepository packageRepository, SchoolBatchListRepository schoolBatchListRepository) {
        this.configurationRepository = configurationRepository;
        this.packageRepository = packageRepository;
        this.schoolBatchListRepository = schoolBatchListRepository;
    }

    public List<ConfigurationEntity> getAllConfigurations() {
        return configurationRepository.findAll();
    }

    public ConfigurationEntity getConfigurationById(Long id) {
        return configurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuration not found"));
    }

    public ConfigurationEntity updateConfiguration(Long batchId, Long configurationId, ConfigurationEntity configurationDetails) {
        ConfigurationEntity configurationEntity = configurationRepository.findById_BatchIdAndId_ConfigurationId(batchId, configurationId)
                .orElseThrow(() -> new RuntimeException("Configuration not found for batchId: " + batchId + " and configurationId: " + configurationId));

        configurationEntity.setItem(configurationDetails.getItem());
        configurationEntity.setType(configurationDetails.getType());
        configurationEntity.setQuantity(configurationDetails.getQuantity());
        configurationEntity.setBatch(configurationDetails.getBatch());

        return configurationRepository.save(configurationEntity);
    }

    @Transactional
    public ConfigurationEntity saveConfiguration(ConfigurationEntity configurationEntity) {
        Long batchId = configurationEntity.getId().getBatchId();

        // Step 1: Find the maximum configurationId for this batchId
        Long maxConfigId = configurationRepository.findMaxConfigIdForBatch(batchId);
        Long nextConfigId = (maxConfigId == null) ? 1 : maxConfigId + 1;
        configurationEntity.getId().setConfigurationId(nextConfigId);

        // Step 2: Create and set the batch
        BatchEntity batch = new BatchEntity();
        batch.setBatchId(batchId);
        configurationEntity.setBatch(batch);

        // Step 3: Save the configuration
        ConfigurationEntity savedConfiguration = configurationRepository.save(configurationEntity);

        // Step 4: Update existing packages that don't have a configuration
        List<PackageEntity> relatedPackages = packageRepository.findByConfiguration_Id_ConfigurationIdAndSchoolBatchList_Batch_BatchId(
                configurationEntity.getId().getConfigurationId(), batchId);
        for (PackageEntity packageEntity : relatedPackages) {
            if (packageEntity.getConfiguration() == null) {
                packageEntity.setConfiguration(savedConfiguration);
            }
        }

        // Step 5: Add new packages according to numberOfPackage for each SchoolBatch
        List<SchoolBatchListEntity> schoolBatchLists = schoolBatchListRepository.findByBatch_BatchId(batchId);
        for (SchoolBatchListEntity schoolBatchList : schoolBatchLists) {
            int numberOfPackagesToAdd = schoolBatchList.getNumberOfPackage();

            // Get the maximum existing packageId for this SchoolBatchList to start from the next ID
            Long maxPackageId = packageRepository.findMaxPackageIdBySchoolBatchList(schoolBatchList.getSchoolBatchId());
            long packageId = (maxPackageId != null) ? maxPackageId + 1 : 1;

            for (int i = 0; i < numberOfPackagesToAdd; i++) {
                PackageEntity newPackage = new PackageEntity();
                newPackage.setSchoolBatchList(schoolBatchList);
                newPackage.setConfiguration(savedConfiguration);

                // Set the unique packageId for this new package
                PackageIdEntity packageIdEntity = new PackageIdEntity();
                packageIdEntity.setPackageId(packageId++);
                newPackage.setId(packageIdEntity);

                // Save the new package
                packageRepository.save(newPackage);
            }
        }

        // Step 6: Return the saved configuration
        return savedConfiguration;
    }

    @Transactional
    public void deleteConfiguration(Long batchId, Long configurationId) {
        // Step 1: Find the configuration to delete
        ConfigurationEntity config = configurationRepository.findById_BatchIdAndId_ConfigurationId(batchId, configurationId)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration not found"));

        // Step 2: Fetch the related packages linked to the configuration and batch
        List<PackageEntity> relatedPackages = packageRepository.findByConfiguration_Id_ConfigurationIdAndSchoolBatchList_Batch_BatchId(configurationId, batchId);

        // Step 3: Delete the related packages
        packageRepository.deleteAll(relatedPackages);

        // Step 4: Delete the configuration itself
        configurationRepository.delete(config);

        // Step 5: Fetch remaining configurations for this batch, ordered by config ID
        List<ConfigurationEntity> remainingConfigs = configurationRepository.findById_BatchIdOrderById_ConfigurationId(batchId);

        // Step 6: Reassign configuration IDs sequentially
        for (int i = 0; i < remainingConfigs.size(); i++) {
            ConfigurationEntity cfg = remainingConfigs.get(i);
            cfg.getId().setConfigurationId((long) (i + 1)); // Reassign configuration ID sequentially
        }
        configurationRepository.saveAll(remainingConfigs);

        List<SchoolBatchListEntity> schoolBatches = schoolBatchListRepository.findByBatch_BatchId(batchId);

        for (SchoolBatchListEntity schoolBatch : schoolBatches) {
            Long schoolBatchId = schoolBatch.getSchoolBatchId(); // or getId() depending on your entity

            List<PackageEntity> packages = packageRepository.findBySchoolBatchList_SchoolBatchId(schoolBatchId);

            // Sort packages by current ID if needed
            packages.sort(Comparator.comparing(pkg -> pkg.getId().getPackageId()));

            for (int i = 0; i < packages.size(); i++) {
                packages.get(i).getId().setPackageId((long) (i + 1));
            }

            packageRepository.saveAll(packages);
        }
    }


    @Transactional
    public void saveAll(List<ConfigurationEntity> configurations) {
        if (configurations == null || configurations.isEmpty()) {
            return; // No configurations to save
        }

        // Step 1: Initialize a variable to track the current batchId
        Long currentBatchId = null;
        long nextConfigId = 1;

        // Step 2: Iterate through configurations
        for (ConfigurationEntity configuration : configurations) {
            // If this is a new batch, reset the nextConfigId counter
            if (configuration.getId() != null && !configuration.getId().getBatchId().equals(currentBatchId)) {
                currentBatchId = configuration.getId().getBatchId();
                nextConfigId = 1; // Restart counter for each batch
            }

            // If the configuration ID is null, assign a new one based on the batchId
            if (configuration.getId() == null) {
                ConfigurationIdEntity newId = new ConfigurationIdEntity();
                newId.setBatchId(currentBatchId);
                configuration.setId(newId);
            }

            // Assign the next available configurationId for the current configuration
            configuration.getId().setConfigurationId(nextConfigId++);

            // Ensure the batch is set on the configuration, if it's not already
            if (configuration.getBatch() == null) {
                BatchEntity batchEntity = new BatchEntity();
                batchEntity.setBatchId(currentBatchId);
                configuration.setBatch(batchEntity);
            }
        }

        // Step 3: Save all configurations in one go
        configurationRepository.saveAll(configurations);
    }


    public List<ConfigurationEntity> getConfigurationsByBatchId(Long batchId) {
        return configurationRepository.findByBatch_BatchId(batchId);
    }

    public ConfigurationEntity getUniqueConfiguration(Long batchId, Long configurationId) {
        return configurationRepository.findById_BatchIdAndId_ConfigurationId(batchId, configurationId)
                .orElseThrow(() -> new RuntimeException("Configuration not found for batchId: " + batchId + " and configurationId: " + configurationId));
    }

    public ConfigurationEntity patchConfiguration(Long batchId, Long configurationId, Map<String, Object> updates) {
        ConfigurationEntity existingConfig = configurationRepository.findById_BatchIdAndId_ConfigurationId(batchId, configurationId)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "item":
                    existingConfig.setItem((String) value);
                    break;
                case "type":
                    existingConfig.setType((String) value);
                    break;
                case "quantity":
                    existingConfig.setQuantity((Integer) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        return configurationRepository.save(existingConfig);
    }

    public List<ConfigurationEntity> updateConfigurationsByBatchId(Long batchId, List<ConfigurationEntity> updatedConfigurations) {
        List<ConfigurationEntity> existingConfigurations = configurationRepository.findByBatch_BatchId(batchId);

        if (existingConfigurations.isEmpty()) {
            throw new ResourceNotFoundException("No configurations found for the given Batch ID");
        }

        if (existingConfigurations.size() != updatedConfigurations.size()) {
            throw new IllegalArgumentException("Mismatch between existing and updated configuration count.");
        }

        for (int i = 0; i < existingConfigurations.size(); i++) {
            ConfigurationEntity existing = existingConfigurations.get(i);
            ConfigurationEntity updated = updatedConfigurations.get(i);

            existing.setItem(updated.getItem());
            existing.setType(updated.getType());
            existing.setQuantity(updated.getQuantity());
        }

        return configurationRepository.saveAll(existingConfigurations);
    }

}
