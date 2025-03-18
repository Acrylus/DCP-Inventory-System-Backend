package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "package")
@Data
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Long packageId;

    @ManyToOne
    @JoinColumn(name = "school_batch_list_id", nullable = false)
    private SchoolBatchListEntity schoolBatchList;

    @ManyToOne
    @JoinColumn(name = "configuration_id", nullable = false)
    private ConfigurationEntity configuration;

    @Column(name = "item", nullable = false)
    private String item;

    @Column(name = "status")
    private String status;

    @Column(name = "component")
    private String component;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "assigned")
    private String assigned;

    @Column(name = "remarks")
    private String remarks;
}
