package com.example.DCP.Inventory.System.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SchoolEnergyDTOEntity {
    private Long schoolEnergyId;
    private Boolean energized;
    private String remarks;
    private Boolean localGridSupply;
    private Boolean type;
    private SchoolDTOEntity school;
}
