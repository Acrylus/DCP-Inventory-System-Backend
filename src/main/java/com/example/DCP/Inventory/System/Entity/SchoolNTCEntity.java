package com.example.DCP.Inventory.System.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "school_ntc")
@Data
public class SchoolNTCEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_ntc_id")
    private Long schoolNTCId;

    @OneToOne(mappedBy = "schoolNTC")
    @JsonBackReference
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
}
