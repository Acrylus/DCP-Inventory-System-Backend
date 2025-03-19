package com.example.DCP.Inventory.System.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "school_contact")
@Data
public class SchoolContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_contact_id")
    private Long schoolContactId;

    @OneToOne
    @JoinColumn(name = "school_record_id", nullable = false)
    private SchoolEntity school;

    @Column(name = "landline")
    private String landline;

    @Column(name = "school_head")
    private String schoolHead;

    @Column(name = "school_head_number")
    private String schoolHeadNumber;

    @Column(name = "school_head_email")
    private String schoolHeadEmail;

    @Column(name = "property_custodian")
    private String propertyCustodian;

    @Column(name = "property_custodian_number")
    private String propertyCustodianNumber;

    @Column(name = "property_custodian_email")
    private String propertyCustodianEmail;

}
