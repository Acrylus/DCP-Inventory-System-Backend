package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class PackageIdEntity implements Serializable {
    private Long packageId;
    private Long schoolBatchListId;
}
