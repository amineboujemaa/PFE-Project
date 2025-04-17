package com.example.Pfeproject.services.auth;

import com.example.Pfeproject.dto.CarDto;
import com.example.Pfeproject.dto.SignupRequest;
import com.example.Pfeproject.dto.UserDto;
import com.example.Pfeproject.entity.Car;
import com.example.Pfeproject.entity.User;
import com.example.Pfeproject.enums.UserRole;
import com.example.Pfeproject.repository.CarRepository;
import com.example.Pfeproject.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final CarRepository carRepository;


    @PostConstruct
    public void createAdminAccount() {
        User adminAccount = userRepository.findFirstByUserRole(UserRole.ADMIN);
        if (adminAccount == null) {
            User newAdminAccount = new User();
            newAdminAccount.setName("admin");
            newAdminAccount.setEmail("admin@admin.com");
            newAdminAccount.setPassword(new BCryptPasswordEncoder().encode("AdminAdminAdmin"));
            newAdminAccount.setUserRole(UserRole.ADMIN);
            userRepository.save(newAdminAccount);
            System.out.println("admin created");
        }
    }

    @Override
    public UserDto createCustomer(SignupRequest signupRequest) {
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        return userDto;
    }

    @Override
    public boolean CustomerEmailExist(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }



}