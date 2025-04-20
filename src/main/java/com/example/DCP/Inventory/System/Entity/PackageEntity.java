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

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "configuration_id", referencedColumnName = "configurationId"),
            @JoinColumn(name = "batch_id", referencedColumnName = "batch_id")
    })
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

