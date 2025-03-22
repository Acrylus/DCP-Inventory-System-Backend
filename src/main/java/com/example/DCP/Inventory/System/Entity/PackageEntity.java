package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "package")
@Data
public class PackageEntity {

    @EmbeddedId
    private PackageIdEntity id;

    @ManyToOne
    @MapsId("schoolBatchListId")
    @JoinColumn(name = "school_batch_list_id", nullable = false)
    private SchoolBatchListEntity schoolBatchList;

    @ManyToOne
    @JoinColumn(name = "configuration_id", nullable = false)
    private ConfigurationEntity configuration;

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

