package com.example.DCP.Inventory.System.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "provider")
@Data
public class ProviderEntity {
    @EmbeddedId
    private ProviderIdEntity id;

    @ManyToOne
    @MapsId("schoolNTCId")
    @JoinColumn(name = "school_ntc_id", nullable = false)
    @JsonBackReference
    private SchoolNTCEntity schoolNTC;

    @Column(name = "name")
    private String name;

    @Column(name = "speed")
    private Integer speed;

    @Column(name = "unit")
    private String unit;
}
