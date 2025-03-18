package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import com.example.DCP.Inventory.System.Repository.ConfigurationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ConfigurationEntity updateConfiguration(Long id, ConfigurationEntity configurationDetails) {
        ConfigurationEntity configurationEntity = configurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuration not found for id: " + id));

        configurationEntity.setItem(configurationDetails.getItem());
        configurationEntity.setType(configurationDetails.getType());
        configurationEntity.setQuantity(configurationDetails.getQuantity());
        configurationEntity.setBatch(configurationDetails.getBatch());

        return configurationRepository.save(configurationEntity);
    }

    public ConfigurationEntity saveConfiguration(ConfigurationEntity configurationEntity) {
        return configurationRepository.save(configurationEntity);
    }

    public void deleteConfiguration(Long id) {
        configurationRepository.deleteById(id);
    }
    
    public void saveAll(List<ConfigurationEntity> configurations) {
        configurationRepository.saveAll(configurations);
    }

    public List<ConfigurationEntity> getConfigurationsByBatchId(Long batchId) {
        return configurationRepository.findByBatch_BatchId(batchId);
    }
}
