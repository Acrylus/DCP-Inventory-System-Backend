package com.example.DCP.Inventory.System.Service;

import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import com.example.DCP.Inventory.System.Repository.ConfigurationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<ConfigurationEntity> existingConfiguration = configurationRepository.findById(id);

        if (existingConfiguration.isPresent()) {
            ConfigurationEntity configurationEntity = existingConfiguration.get();
            configurationEntity.setItem(configurationDetails.getItem());
            configurationEntity.setType(configurationDetails.getType());
            configurationEntity.setQuantity(configurationDetails.getQuantity());
            return configurationRepository.save(configurationEntity); // Save and return updated entity
        } else {
            throw new RuntimeException("Configuration not found for id: " + id);
        }
    }

    public ConfigurationEntity saveConfiguration(ConfigurationEntity configurationEntity) {
        return configurationRepository.save(configurationEntity);
    }

    public void deleteConfiguration(Long id) {
        configurationRepository.deleteById(id);
    }
}
