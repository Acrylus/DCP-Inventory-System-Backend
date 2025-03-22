package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ProviderIdEntity implements Serializable {
    private Long providerId;
    private Long schoolNTCId;
}
