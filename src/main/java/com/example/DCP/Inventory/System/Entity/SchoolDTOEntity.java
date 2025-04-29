package com.example.DCP.Inventory.System.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SchoolDTOEntity {
    private Long schoolRecordId;
    private String schoolId;
    private DistrictEntity district;
    private String name;
}
