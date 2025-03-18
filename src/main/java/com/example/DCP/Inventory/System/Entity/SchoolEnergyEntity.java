package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "school_energy")
@Data
public class SchoolEnergyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_energy_id")
    private Long schoolEnergyId;

    @Column(name = "energized")
    private Boolean energized;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "local_grid_supply")
    private Boolean localGridSupply;
}
