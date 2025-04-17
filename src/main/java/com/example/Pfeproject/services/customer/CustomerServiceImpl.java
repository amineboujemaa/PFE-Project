package com.example.Pfeproject.services.customer;

import com.example.Pfeproject.dto.*;
import com.example.Pfeproject.entity.*;
import com.example.Pfeproject.enums.BookAVisitEnum;
import com.example.Pfeproject.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CarRepository carRepository;

    private  final UserRepository userRepository;

    private final BookAVisitRepository bookAVisitRepository;

    private final SavedCarRepository savedCarRepository;
    private final CommentRepository commentRepository;
    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public boolean bookAVisit(BookAVisitDto bookAVisitDto) {
        Optional<Car> optionalCar = carRepository.findById(bookAVisitDto.getCarId());
        Optional<User> optionalUser = userRepository.findById(bookAVisitDto.getUserId());
        if (optionalCar.isPresent() && optionalUser.isPresent()){
            Car existingCar = optionalCar.get();
            BookAVisit bookAVisit = new BookAVisit();
            bookAVisit.setUser(optionalUser.get());
            bookAVisit.setCar(existingCar);
            bookAVisit.setBookAVisitEnum(BookAVisitEnum.PENDING);
            bookAVisit.setBrand(existingCar.getBrand());
            bookAVisit.setColor(existingCar.getColor());
            bookAVisit.setName(existingCar.getName());
            bookAVisit.setType(existingCar.getType());
            bookAVisit.setTransmission(existingCar.getTransmission());
            bookAVisit.setDescription(existingCar.getDescription());
            bookAVisit.setPrice(existingCar.getPrice());
            bookAVisit.setYear(existingCar.getYear());
            bookAVisit.setVisitDate(bookAVisitDto.getVisitDate());
            bookAVisit.setVisitTime(bookAVisitDto.getVisitTime());
            bookAVisitRepository.save(bookAVisit);
            return true;

        }


        return false;
    }

    @Override
    public CarDto getCarById(Long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        return optionalCar.map(Car::getCarDto).orElse(null);
    }
    @Override
    public UpdateCustomerDto getUserById(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.map(User::getUpdateCustomerDto).orElse(null);
    }

    @Override
    public List<BookAVisitDto> getBookingsByUserId(Long userId) {
        return bookAVisitRepository.findAllByUserId(userId).stream().map(BookAVisit::getBookAVisitDto).collect(Collectors.toList());
    }

    @Override
    public void deleteCar(Long id) {
        savedCarRepository.deleteById(id);
    }

    @Override
    public boolean updateUser(Long userId,UpdateCustomerResponse updateCustomerResponse) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()){
            User existingUser = optionalUser.get();
            if(updateCustomerResponse.getOldPass()!=null){
                String oldPass =new BCryptPasswordEncoder().encode(updateCustomerResponse.getOldPass());
                if (oldPass==existingUser.getPassword()){
                    existingUser.setPassword(new BCryptPasswordEncoder().encode(updateCustomerResponse.getNewPass()));
                }
            }
            existingUser.setEmail(updateCustomerResponse.getEmail());
            existingUser.setName(updateCustomerResponse.getName());
            userRepository.save(existingUser);
            return true;
        }
        return false;
    }


    @Override
    public List<SavedCarDto> getMyCarsByUserId(Long userId) {
        return savedCarRepository.findAllByUserId(userId).stream().map(SavedCar::getSavedCarDto).collect(Collectors.toList());
    }

    @Override
    public CarDtoListDto searchCar(SearchCarDto searchCarDto) {
        Car car = new Car();
        car.setBrand(searchCarDto.getBrand());
        car.setType(searchCarDto.getType());
        car.setTransmission(searchCarDto.getTransmission());
        car.setColor(searchCarDto.getColor());
        ExampleMatcher exampleMatcher =
                ExampleMatcher.matchingAll()
                        .withMatcher("brand",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("type",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("transmission",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("color",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Car> carExample = Example.of(car,exampleMatcher);
        List<Car> carList = carRepository.findAll(carExample);
        CarDtoListDto carDtoListDto = new CarDtoListDto();
        carDtoListDto.setCarDtoList(carList.stream().map(Car::getCarDto).collect(Collectors.toList()));
        return carDtoListDto;
    }

    @Override
    public boolean saveACar(SavedCarDto savedCarDto){
        Optional<Car> optionalCar = carRepository.findById(savedCarDto.getCarId());
        Optional<User> optionalUser = userRepository.findById(savedCarDto.getUserId());
        if (optionalCar.isPresent() && optionalUser.isPresent()){
            Car existingCar = optionalCar.get();
            SavedCar savedCar=new SavedCar();
            savedCar.setUser(optionalUser.get());
            savedCar.setCar(existingCar);
            savedCar.setStatus(true);
            savedCarRepository.save(savedCar);
            return true;
        }

        return false;
    }

    @Override
    public CommentDTO createComment(Long carId, Long userId, String content) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if ((optionalCar.isPresent()) && (optionalUser.isPresent())){
            Comment comment = new Comment();
            comment.setCreatedAt(new Date());
            comment.setContent(content);
            comment.setCar(optionalCar.get());
            comment.setUser(optionalUser.get());
            return commentRepository.save(comment).getCommentDTO();

        }
        throw new EntityNotFoundException("user or car not found");

    }

    @Override
    public List<CommentDTO> getCommentByCarId(Long carId) {
        List<CommentDTO> comments = commentRepository.findAllByCarId(carId).stream().map(Comment::getCommentDTO).collect(Collectors.toList());
        return comments;
    }


    @Override
    public StatCustomerResponse calculateStat(Long userId) {
        Long cars = carRepository.count();
        Long savedCars = savedCarRepository.countByUserId(userId);
        Long bookings = bookAVisitRepository.countByUserId(userId);
        Long approvedBookings = bookAVisitRepository.countByBookAVisitEnumAndUserId(BookAVisitEnum.APPROVED, userId);
        Long rejectedBookings = bookAVisitRepository.countByBookAVisitEnumAndUserId(BookAVisitEnum.REJECTED, userId);
        Long pendingBookings = bookAVisitRepository.countByBookAVisitEnumAndUserId(BookAVisitEnum.PENDING, userId);
        return new StatCustomerResponse(cars, savedCars, bookings, approvedBookings, rejectedBookings, pendingBookings);
    }


}
