package com.example.Pfeproject.services.customer;

import com.example.Pfeproject.dto.*;

import java.io.IOException;
import java.util.List;

public interface CustomerService {
    List<CarDto> getAllCars();

    boolean bookAVisit(BookAVisitDto bookAVisitDto);

    CarDto getCarById(Long carId);

    UpdateCustomerDto getUserById(Long userId);

    List<BookAVisitDto> getBookingsByUserId(Long userId);

    List<SavedCarDto> getMyCarsByUserId(Long userId);

    CarDtoListDto searchCar(SearchCarDto searchCarDto);

    boolean saveACar(SavedCarDto savedCarDto);

    void deleteCar(Long id);

    boolean updateUser(Long userId,UpdateCustomerResponse updateCustomerResponse) throws IOException;

    CommentDTO createComment(Long carId, Long userId, String content);

    List<CommentDTO> getCommentByCarId(Long carId);

    StatCustomerResponse calculateStat(Long userId);
}
