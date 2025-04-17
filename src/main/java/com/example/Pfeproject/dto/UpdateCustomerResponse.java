package com.example.Pfeproject.dto;

import lombok.Data;

@Data
public class UpdateCustomerResponse {
    Long id;
    String name;
    String email;
    String oldPass;
    String newPass;
}
