package com.example.DCP.Inventory.System.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

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
    private DivisionEntity division;

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

    @Column(name = "landline")
    private String landline;

    @Column(name = "school_head")
    private String schoolHead;

    @Column(name = "school_head_number")
    private String schoolHeadNumber;

    @Column(name = "school_head_email")
    private String schoolHeadEmail;

    @Column(name = "property_custodian")
    private String propertyCustodian;

    @Column(name = "property_custodian_number")
    private String propertyCustodianNumber;

    @Column(name = "property_custodian_email")
    private String propertyCustodianEmail;

    @Column(name = "energized")
    private Boolean energized;

    @Column(name = "energized_remarks")
    private Boolean energizedRemarks;

    @Column(name = "local_grid_supply")
    private Boolean localGridSupply;

    @Column(name = "connectivity")
    private Boolean connectivity;

    @Column(name = "smart")
    private Boolean smart;

    @Column(name = "globe")
    private Boolean globe;

    @Column(name = "digital_network")
    private Boolean digitalNetwork;

    @Column(name = "am")
    private Boolean am;

    @Column(name = "fm")
    private Boolean fm;

    @Column(name = "tv")
    private Boolean tv;

    @Column(name = "cable")
    private Boolean cable;

    @Column(name = "ntc_remark")
    private String ntcRemark;

    @Column(name = "designation")
    private String designation;

    @Column(name = "previous_station")
    private String previousStation;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoordinatorEntity> coordinators;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SchoolBatchListEntity> batchSchoolLists;
}
