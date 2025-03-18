package com.example.DCP.Inventory.System.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "school")
@Data
public class SchoolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_record_id")
    private Long schoolRecordId;

    @ManyToOne
    @JoinColumn(name = "division_id", nullable = false)
    @JsonIgnore
    private DivisionEntity division;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    @JsonIgnore
    private DistrictEntity district;

    @Column(name = "classification")
    private String classification;

    @Column(name = "school_id")
    private String schoolId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "designation")
    private String designation;

    @Column(name = "previous_station")
    private String previousStation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "school_contact_id", referencedColumnName = "school_contact_id")
    @JsonManagedReference
    private SchoolContactEntity schoolContact;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "school_energy_id", referencedColumnName = "school_energy_id")
    @JsonManagedReference
    private SchoolEnergyEntity schoolEnergy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "school_ntc_id", referencedColumnName = "school_ntc_id")
    @JsonManagedReference
    private SchoolNTCEntity schoolNTC;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<CoordinatorEntity> coordinators;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<SchoolBatchListEntity> schoolBatchList;
}
