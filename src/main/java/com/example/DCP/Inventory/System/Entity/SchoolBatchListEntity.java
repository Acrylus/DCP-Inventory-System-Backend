package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "school_batch_list")
@Data
public class SchoolBatchListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_batch_id")
    private Long schoolBatchId;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private BatchEntity batch;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private SchoolEntity school;

    @Column(name = "delivery_date")
    private Integer deliveryDate;

    @Column(name = "number_of_package")
    private Integer numberOfPackage;

    @Column(name = "status")
    private String status;

    @Column(name = "key_stage")
    private String keyStage;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "accountable")
    private String accountable;

    @OneToMany(mappedBy = "schoolBatchList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageEntity> packages;
}
