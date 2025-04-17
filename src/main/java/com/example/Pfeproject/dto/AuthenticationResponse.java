package com.example.Pfeproject.dto;


import com.example.Pfeproject.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;

    private UserRole userRole;

    private Long userId;
}
