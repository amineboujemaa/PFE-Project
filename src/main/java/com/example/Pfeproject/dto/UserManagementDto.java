package com.example.Pfeproject.dto;

import com.example.Pfeproject.enums.UserRole;
import lombok.Data;

@Data
public class UserManagementDto {
    private Long id;
    private  String name;
    private String email;
    private UserRole userRole;
}
