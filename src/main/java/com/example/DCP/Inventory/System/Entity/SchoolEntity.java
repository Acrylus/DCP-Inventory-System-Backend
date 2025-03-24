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
    @JoinColumn(name = "district_id", nullable = false)
    private DistrictEntity district;

    @Column(name = "classification")
    private String classification;

    @Column(name = "school_id")
    private String schoolId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "previous_station")
    private String previousStation;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference("school_batch_list")
    private List<SchoolBatchListEntity> schoolBatchList;
}
