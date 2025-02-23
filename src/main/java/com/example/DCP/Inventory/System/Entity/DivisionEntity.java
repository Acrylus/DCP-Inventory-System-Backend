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

    @Column(nullable = false, unique = true) // Unique constraint for officeName
    private String officeName;
    private String headOfOffice;
    private String position;
    private String itoName;
    private String emailAddress;

    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MunicipalityEntity> municipalities;

    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DistrictEntity> districts;
}
