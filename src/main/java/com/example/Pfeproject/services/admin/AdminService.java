package com.example.Pfeproject.services.admin;

import com.example.Pfeproject.dto.*;
import com.example.Pfeproject.entity.User;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    boolean postCar(CarDto carDto);

    List<CarDto> getAllCars();

    void deleteCar(Long id );


    void deleteUser(Long id);

    CarDto getCarById(Long id);

    boolean updateCar(Long carId, CarDto carDto) throws IOException;

    List<BookAVisitDto> showAllBookings();

    List<BookAVisitDto> getBookings();


    boolean changeUserRole(Long userId);

    boolean changeBookingStatus(Long bookingId , String status);

    CarDtoListDto searchCar(SearchCarDto searchCarDto);

    CommentDTO createComment(Long carId , Long userId , String content);

    List<CommentDTO> getCommentByCarId(Long carId);

    StatCustomerResponse calculateStat();

    boolean updateUser(Long userId, UpdateCustomerResponse updateCustomerResponse) throws IOException;

    UpdateCustomerDto getUserById(Long userId);

    List<User> getAllUsers();

    byte[] generateQRCodeImage(String url);
}
