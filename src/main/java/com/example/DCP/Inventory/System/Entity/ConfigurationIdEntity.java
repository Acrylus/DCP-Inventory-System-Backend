package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ConfigurationIdEntity implements Serializable {
    private Long configurationId;
    private Long batchId;
}
