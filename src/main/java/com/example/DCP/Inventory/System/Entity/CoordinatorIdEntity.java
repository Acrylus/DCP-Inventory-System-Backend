package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class CoordinatorIdEntity implements Serializable {
    private Long coordinatorId;
    private Long schoolContactId;
}
