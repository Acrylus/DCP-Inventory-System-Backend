package com.example.DCP.Inventory.System.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SchoolContactDTOEntity {
    private Long schoolContactId;
    private String landline;
    private String schoolHead;
    private String schoolHeadNumber;
    private String schoolHeadEmail;
    private String designation;
    private String propertyCustodian;
    private String propertyCustodianNumber;
    private String propertyCustodianEmail;
    private SchoolDTOEntity school;
}
