package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.BatchEntity;
import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import com.example.DCP.Inventory.System.Entity.ConfigurationIdEntity;
import com.example.DCP.Inventory.System.Exception.ResourceNotFoundException;
import com.example.DCP.Inventory.System.Repository.ConfigurationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
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
        Long batchId = configurationEntity.getBatch().getBatchId();

        // Find the maximum configurationId for this batchId
        Long maxConfigId = configurationRepository.findMaxConfigIdForBatch(batchId);

        // Set configurationId to the next available number
        Long nextConfigId = (maxConfigId == null) ? 1 : maxConfigId + 1;
        configurationEntity.getId().setConfigurationId(nextConfigId);

        // Save the configuration and return the saved entity
        return configurationRepository.save(configurationEntity);
    }

    public void deleteConfiguration(Long batchId, Long configurationId) {
        ConfigurationEntity config = configurationRepository.findById_BatchIdAndId_ConfigurationId(batchId, configurationId)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration not found"));

        configurationRepository.delete(config);
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
