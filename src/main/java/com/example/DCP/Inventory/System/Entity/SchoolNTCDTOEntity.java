package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SchoolNTCDTOEntity {
    private Long schoolNTCId;
    private Boolean internet;
    private Boolean pldt;
    private Boolean globe;
    private Boolean am;
    private Boolean fm;
    private Boolean tv;
    private Boolean cable;
    private String remark;
    private SchoolDTOEntity school;
}
