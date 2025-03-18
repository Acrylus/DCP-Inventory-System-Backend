package com.example.DCP.Inventory.System.Entity;

import com.example.DCP.Inventory.System.FieldConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "division_id", nullable = true)
    private DivisionEntity division;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = true)
    private DistrictEntity district;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = true)
    private SchoolEntity school;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    @Convert(converter = FieldConverter.class)
    private String password;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "user_type", nullable = false)
    private String userType;
}
