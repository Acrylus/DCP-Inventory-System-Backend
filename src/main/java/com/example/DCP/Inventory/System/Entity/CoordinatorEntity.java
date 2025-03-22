package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "coordinator")
@Data
public class CoordinatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coordinator_id")
    private Long coordinatorId;

    @ManyToOne
    @JoinColumn(name = "school_contact_id", nullable = false)
    private SchoolContactEntity schoolContact;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "remarks", nullable = false)
    private String remarks;
}
