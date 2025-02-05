package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "school")
@Data
public class SchoolEntity {

    @Id
    @Column(name = "school_record_id")
    private Long schoolRecordId;

    @ManyToOne
    @JoinColumn(name = "division_id", nullable = false)
    private DivisionEntity division;

    @ManyToMany
    @JoinTable(
            name = "district_school_list",
            joinColumns = @JoinColumn(name = "school_id"),
            inverseJoinColumns = @JoinColumn(name = "district_id")
    )
    private Set<DistrictEntity> districts;

    @Column(name = "school_id")
    private Long schoolId;

    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "ict_coordinator")
    private String ictCoordinator;

    @Column(name = "record_number")
    private String recordNumber;

    @Column(name = "classification")
    private String classification;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "number_of_package")
    private Integer numberOfPackage;

    @Column(name = "school_head")
    private String schoolHead;

    @Column(name = "designation")
    private Boolean designation;

    @Column(name = "school_head_email")
    private String schoolHeadEmail;

    @Column(name = "school_head_contact")
    private String schoolHeadContact;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "previous_station")
    private String previousStation;

    @Column(name = "energized")
    private Boolean energized;

    @Column(name = "connectivity")
    private Boolean connectivity;
}
