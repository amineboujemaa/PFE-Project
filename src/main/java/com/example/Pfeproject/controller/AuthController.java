package com.example.Pfeproject.controller;

import com.example.Pfeproject.Utils.JWTUtil;
import com.example.Pfeproject.dto.*;
import com.example.Pfeproject.entity.User;
import com.example.Pfeproject.repository.UserRepository;
import com.example.Pfeproject.services.auth.AuthService;
import com.example.Pfeproject.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;


    @PostMapping("/signup")
    public ResponseEntity<?> signupCustomer(@RequestBody SignupRequest signupRequest){
        if(authService.CustomerEmailExist(signupRequest.getEmail()))
            return new ResponseEntity<>("Customer is already exist with this email",HttpStatus.NOT_ACCEPTABLE);
        UserDto createdCustomerDto = authService.createCustomer(signupRequest);
        if(createdCustomerDto == null) return new ResponseEntity<>
                ("Customer not created", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(createdCustomerDto,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws
            BadCredentialsException,
            DisabledException,
            UsernameNotFoundException{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e ){
                throw new BadCredentialsException("Incorrect Email or password");
        }
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final  String jwt = jwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (optionalUser.isPresent()){
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;

    }
    @GetMapping("/cars")
    public ResponseEntity<?> getAllCars(){
        return ResponseEntity.ok(authService.getAllCars());
    }
/*
    @PostMapping("/contact-us")
    public ResponseEntity<Void> sendMessage(@RequestBody ContactUsDto contactUsDto) {
        boolean success = authService.sendMessage(contactUsDto);
        if (success) return ResponseEntity.status(HttpStatus.CREATED).build();
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }*/
}

