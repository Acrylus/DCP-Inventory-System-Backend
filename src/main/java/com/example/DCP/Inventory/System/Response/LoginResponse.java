package com.example.DCP.Inventory.System.Response;

import com.example.DCP.Inventory.System.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private UserEntity user;
    private String token;
}
