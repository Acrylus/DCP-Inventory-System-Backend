package com.example.DCP.Inventory.System.Entity;

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

    @Column(name = "internet")
    private Boolean internet;

    @Column(name = "smart")
    private Boolean smart;

    @Column(name = "globe")
    private Boolean globe;

    @Column(name = "digitel_network")
    private Boolean digitelNetwork;

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
