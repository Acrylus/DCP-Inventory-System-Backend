package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "batch")
@Data
public class BatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "batch_name", nullable = false)
    private String batchName;

    @Column(name = "budget_year")
    private Integer budgetYear;

    @Column(name = "delivery_year")
    private Integer deliveryYear;

    @Column(name = "price")
    private Double price;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "number_of_package")
    private Integer numberOfPackage;

    @Column(name = "remarks")
    private String remarks;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SchoolBatchListEntity> batchSchoolLists;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConfigurationEntity> configurations;
}
