package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import com.example.DCP.Inventory.System.Service.ConfigurationService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping("get_all")
    public ResponseEntity<Object> getAllConfigurations() {
        return Response.response(HttpStatus.OK, "Configurations found", configurationService.getAllConfigurations());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getConfigurationById(@PathVariable Long id) {
        ConfigurationEntity configurationEntity = configurationService.getConfigurationById(id);
        if (configurationEntity != null) {
            return Response.response(HttpStatus.OK, "Configuration found", configurationEntity);
        } else {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Configuration not found");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createConfiguration(@RequestBody ConfigurationEntity configurationEntity) {
        ConfigurationEntity createdConfiguration = configurationService.saveConfiguration(configurationEntity);
        return Response.response(HttpStatus.CREATED, "Configuration created successfully", createdConfiguration);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateConfiguration(@PathVariable Long id, @RequestBody ConfigurationEntity configurationDetails) {
        try {
            ConfigurationEntity updatedConfiguration = configurationService.updateConfiguration(id, configurationDetails);
            return Response.response(HttpStatus.OK, "Configuration updated successfully", updatedConfiguration);
        } catch (RuntimeException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteConfiguration(@PathVariable Long id) {
        try {
            configurationService.deleteConfiguration(id);
            return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "Configuration deleted successfully");
        } catch (RuntimeException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "Configuration not found");
        }
    }
    
    @PostMapping("/create_all")
    public ResponseEntity<Object> addConfigurations(@RequestBody List<ConfigurationEntity> configurations) {
        try {
            configurationService.saveAll(configurations);
            return ResponseEntity.status(HttpStatus.CREATED).body("Configurations added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add Configurations");
        }
    }

    @GetMapping("/batch/{batchId}")
    public ResponseEntity<List<ConfigurationEntity>> getConfigurationsByBatch(@PathVariable Long batchId) {
        List<ConfigurationEntity> configurations = configurationService.getConfigurationsByBatchId(batchId);
        return ResponseEntity.ok(configurations);
    }
}
