package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "district")
@Data
public class DistrictEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private Long districtId;

    @ManyToOne
    @JoinColumn(name = "division_id", nullable = false)
    private DivisionEntity division;

    @Column(name = "name", nullable = false)
    private String name;
}
