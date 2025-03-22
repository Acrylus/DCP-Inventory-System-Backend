package com.example.DCP.Inventory.System.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "school_ntc")
@Data
public class SchoolNTCEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_ntc_id")
    private Long schoolNTCId;

    @OneToOne
    @JoinColumn(name = "school_record_id", nullable = false)
    private SchoolEntity school;

    @Column(name = "internet")
    private Boolean internet;

    @Column(name = "pldt")
    private Boolean pldt;

    @Column(name = "globe")
    private Boolean globe;

    @Column(name = "am")
    private Boolean am;

    @Column(name = "fm")
    private Boolean fm;

    @Column(name = "tv")
    private Boolean tv;

    @Column(name = "cable")
    private Boolean cable;

    @Column(name = "remark")
    private String remark;

    @OneToMany(mappedBy = "schoolNTC", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProviderEntity> providers;

}
