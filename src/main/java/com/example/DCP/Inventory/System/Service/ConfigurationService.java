package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
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
        // Iterate over each configuration to assign the next configurationId based on the batchId
        for (ConfigurationEntity configuration : configurations) {
            Long batchId = configuration.getId().getBatchId();

            // Find the maximum configurationId for this batchId
            Long maxConfigId = configurationRepository.findMaxConfigIdForBatch(batchId);

            // Set configurationId to the next available number
            Long nextConfigId = (maxConfigId == null) ? 1 : maxConfigId + 1;
            configuration.getId().setConfigurationId(nextConfigId);
        }

        // Save all configurations
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
