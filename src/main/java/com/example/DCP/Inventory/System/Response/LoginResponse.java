package com.example.DCP.Inventory.System.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String message;
    private Long userId;
    private String username;
    private String userType;
    private String token; // Field to hold the JWT
}
