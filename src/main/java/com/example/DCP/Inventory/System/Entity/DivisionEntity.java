package com.example.DCP.Inventory.System.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "division", uniqueConstraints = {
        @UniqueConstraint(columnNames = "officeName") // Ensure uniqueness at the database level
})
@Entity
public class DivisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long divisionId;

    @Column(name = "division",nullable = false, unique = true)
    private String division;

    @Column(name = "title",nullable = false, unique = true)
    private String title;

    @Column(name = "sds_name")
    private String sdsName;

    @Column(name = "sds_position")
    private String sdsPosition;

    @Column(name = "ito_name")
    private String itoName;

    @Column(name = "ito_email")
    private String itoEmail;

    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MunicipalityEntity> municipalities;

    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DistrictEntity> districts;
}
