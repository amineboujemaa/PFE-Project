package com.example.Pfeproject.controller;


import com.example.Pfeproject.dto.*;
import com.example.Pfeproject.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final AuthController authController;

    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getAllCars(){
        List<CarDto> carDtoList = customerService.getAllCars();
        return ResponseEntity.ok(carDtoList);
    }
    @PostMapping("/car/book")
    public ResponseEntity<Void> bookAVisit(@RequestBody BookAVisitDto bookAVisitDto) {
        boolean success = customerService.bookAVisit(bookAVisitDto);
        if (success) return ResponseEntity.status(HttpStatus.CREATED).build();
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long carId){
        CarDto carDto = customerService.getCarById(carId);
        if (carDto == null)return ResponseEntity.notFound().build();
        return ResponseEntity.ok(carDto);
    }

    @GetMapping("/userUpdate/{userId}")
    public ResponseEntity<UpdateCustomerDto> getUserById(@PathVariable Long userId){
        UpdateCustomerDto updateCustomerDto = customerService.getUserById(userId);
        if (updateCustomerDto == null)return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updateCustomerDto);
    }

    @PutMapping("/userUpdate/confirm/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @ModelAttribute UpdateCustomerResponse updateCustomerResponse) throws IOException {
        try {
            boolean success = customerService.updateUser(userId ,updateCustomerResponse);
            if (success)return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return null;
    }

    @GetMapping("/car/bookings/{userId}")
    public ResponseEntity<List<BookAVisitDto>> getBookingsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(customerService.getBookingsByUserId(userId));
    }

    @GetMapping("/car/myCars/{userId}")
    public ResponseEntity<List<SavedCarDto>> getMyCarsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(customerService.getMyCarsByUserId(userId));
    }

    @DeleteMapping("/car/myCars/delete/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id){
        customerService.deleteCar(id);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/car/search")
    public ResponseEntity<?> searchCar(@RequestBody SearchCarDto searchCarDto){
        return ResponseEntity.ok(customerService.searchCar(searchCarDto));
    }

    @PostMapping("/car/save")
    public ResponseEntity<Void> saveACar(@RequestBody SavedCarDto savedCarDto){
        boolean success = customerService.saveACar(savedCarDto);
        if (success)return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/car/comment")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentRequestDto commentRequestDto){
        CommentDTO createdCommentDto = customerService.createComment(commentRequestDto.getCarId(),commentRequestDto.getUserId(),commentRequestDto.getContent());
        if (createdCommentDto == null)return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDto);
    }
    @GetMapping("/comments/{carId}")
    public ResponseEntity<List<CommentDTO>> getCommentByCarId(@PathVariable Long carId){
        return ResponseEntity.ok(customerService.getCommentByCarId(carId));
    }

    @GetMapping("/car/stat/{userId}")
    public ResponseEntity<StatCustomerResponse> getStat(@PathVariable Long userId){
        return ResponseEntity.ok(customerService.calculateStat(userId));
    }
}
