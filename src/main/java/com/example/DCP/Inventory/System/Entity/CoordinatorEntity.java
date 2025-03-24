package com.example.DCP.Inventory.System.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "coordinator")
@Data
public class CoordinatorEntity {

    @EmbeddedId
    private CoordinatorIdEntity id;

    @ManyToOne
    @MapsId("schoolContactId")
    @JoinColumn(name = "school_contact_id", nullable = false)
    @JsonBackReference
    private SchoolContactEntity schoolContact;

    @Column(name = "name")
    private String name;

    @Column(name = "designation")
    private String designation;

    @Column(name = "email")
    private String email;

    @Column(name = "number")
    private String number;

    @Column(name = "remarks")
    private String remarks;
}
