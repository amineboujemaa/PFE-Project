package com.example.Pfeproject.services.auth;

import com.example.Pfeproject.dto.CarDto;
import com.example.Pfeproject.dto.SignupRequest;
import com.example.Pfeproject.dto.UserDto;

import java.util.List;

public interface AuthService {
    UserDto createCustomer(SignupRequest signupRequest);

    boolean CustomerEmailExist(String email);

    List<CarDto> getAllCars();

}
