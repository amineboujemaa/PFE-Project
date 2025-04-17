package com.example.Pfeproject.controller;

import com.example.Pfeproject.dto.*;
import com.example.Pfeproject.entity.User;
import com.example.Pfeproject.services.admin.AdminService;
import com.example.Pfeproject.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class AdminController {
    private final AdminService adminService ;
    private final UserService userService;
    


    @PostMapping("/car")
    public ResponseEntity<?> postCar(@ModelAttribute CarDto carDto) throws IOException {
        boolean success = adminService.postCar(carDto);
            System.out.println(success);
        if (success){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/car/comment")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentRequestDto commentRequestDto){
        System.out.println(commentRequestDto.getCarId());
        System.out.println(commentRequestDto.getContent());
        System.out.println(commentRequestDto.getUserId());
        CommentDTO createdCommentDto = adminService.createComment(commentRequestDto.getCarId(),commentRequestDto.getUserId(),commentRequestDto.getContent());
        if (createdCommentDto == null)return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDto);
    }

    @GetMapping("/cars")
    public ResponseEntity<?> getAllCars(){
        return ResponseEntity.ok(adminService.getAllCars());
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id){
        adminService.deleteCar(id);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        adminService.deleteUser(id);
        return ResponseEntity.ok(null);
    }
    @GetMapping("/car/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long id){
        CarDto carDto = adminService.getCarById(id);
        return ResponseEntity.ok(carDto);
    }

    @PutMapping("/car/{carId}")
    public ResponseEntity<Void> updateCar(@PathVariable Long carId, @ModelAttribute CarDto carDto) throws IOException {
        try {
            boolean success = adminService.updateCar(carId, carDto);
            if (success) return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return null;
    }

    @GetMapping("/comments/{carId}")
    public ResponseEntity<List<CommentDTO>> getCommentByCarId(@PathVariable Long carId){
        return ResponseEntity.ok(adminService.getCommentByCarId(carId));
    }

    @GetMapping("/car/bookings/")
    public ResponseEntity<List<BookAVisitDto>> getBookings(){
        return ResponseEntity.ok(adminService.getBookings());
    }


    @GetMapping("/users/")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/car/all-bookings/")
    public ResponseEntity<List<BookAVisitDto>> showAllBookings(){
        return ResponseEntity.ok(adminService.showAllBookings());
    }

    @GetMapping("/car/booking/{bookingId}/{status}")
    public ResponseEntity<?> changeBookingStatus(@PathVariable Long bookingId, @PathVariable String status){
        boolean success = adminService.changeBookingStatus(bookingId,status);
        if (success)return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/userRole/{userId}")
    public ResponseEntity<Object> changeUserRole(@PathVariable Long userId) {
        try {
            boolean success = adminService.changeUserRole(userId);
            if (success) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/car/search")
    public ResponseEntity<?> searchCar(@RequestBody SearchCarDto searchCarDto){
        return ResponseEntity.ok(adminService.searchCar(searchCarDto));
    }

    @GetMapping("/car/stat")
    public ResponseEntity<StatCustomerResponse> getStat(){
        return ResponseEntity.ok(adminService.calculateStat());
    }

    @GetMapping("/userUpdate/{userId}")
    public ResponseEntity<UpdateCustomerDto> getUserById(@PathVariable Long userId) {
        UpdateCustomerDto updateCustomerDto = adminService.getUserById(userId);
        if (updateCustomerDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updateCustomerDto);
    }

    @PutMapping("/userUpdate/confirm/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId, @ModelAttribute UpdateCustomerResponse updateCustomerResponse) throws IOException {
        try {
            boolean success = adminService.updateUser(userId, updateCustomerResponse);
            if (success) return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return null;
    }

    @PostMapping("/QrCode/{carId}")
    public ResponseEntity<byte[]> generateQRCode(@PathVariable Long carId) {
        try {
            String url = "http://localhost:4200/customer/car-details/" + carId;
            byte[] qrCode = adminService.generateQRCodeImage(url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
