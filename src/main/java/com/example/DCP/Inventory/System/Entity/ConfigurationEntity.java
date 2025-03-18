package com.example.DCP.Inventory.System.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "configuration")
@Data
public class ConfigurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "configuration_id")
    private Long configurationId;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    @JsonBackReference
    private BatchEntity batch;

    @Column(name = "item", nullable = false)
    private String item;

    @Column(name = "type")
    private String type;

    @Column(name = "quantity")
    private Integer quantity;


}
