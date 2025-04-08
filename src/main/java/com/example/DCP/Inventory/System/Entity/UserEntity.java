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

    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    @Convert(converter = FieldConverter.class)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_type", nullable = false)
    private String userType;
}
