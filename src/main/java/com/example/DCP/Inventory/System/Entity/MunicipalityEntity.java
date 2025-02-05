package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "municipality")
@Data
public class MunicipalityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "municipality_id")
    private Long municipalityId;

    @ManyToOne
    @JoinColumn(name = "division_id", nullable = false)
    private DivisionEntity division;

    @Column(name = "name", nullable = false)
    private String name;
}
