package com.example.Pfeproject.services.admin;

import com.example.Pfeproject.Utils.JWTUtil;
import com.example.Pfeproject.dto.*;
import com.example.Pfeproject.entity.BookAVisit;
import com.example.Pfeproject.entity.Car;
import com.example.Pfeproject.entity.Comment;
import com.example.Pfeproject.entity.User;
import com.example.Pfeproject.enums.BookAVisitEnum;
import com.example.Pfeproject.enums.UserRole;
import com.example.Pfeproject.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.System.out;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final CarRepository carRepository;
    private final BookAVisitRepository bookAVisitRepository;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final SavedCarRepository savedCarRepository;
    private final JWTUtil jwtUtil;
    private final CommentRepository commentRepository;

    @Override
    public boolean postCar(CarDto carDto)  {

        try{
            Car car = new Car();
            car.setName(carDto.getName());
            car.setBrand(carDto.getBrand());
            car.setColor(carDto.getColor());
            car.setPrice(carDto.getPrice());
            car.setYear(carDto.getYear());
            car.setType(carDto.getType());
            car.setDescription(carDto.getDescription());
            car.setTransmission(carDto.getTransmission());
            car.setImage(carDto.getImage().getBytes());
            carRepository.save(car);

            sendCarPostedEmailToUsers(car);

            return true;
        }catch (Exception e){
            out.println("car not created");
            return false;

        }


    }

    private void sendCarPostedEmailToUsers(Car car) {


        List<User> users = userRepository.findAll();
        out.println(users.size());
        String emailSubject = "New Car Posted";
        String emailTemplate = loadEmailTemplateFromFile("email_car.html");

        for (User user : users) {
            String userEmail = user.getEmail();
            String emailContent = emailTemplate
                        .replace("{{carName}}", car.getName())
                        .replace("{{carBrand}}", car.getBrand())
                        .replace("{{carColor}}", car.getColor())
                        .replace("{{carType}}", car.getType())
                        .replace("{{carTransmission}}", car.getTransmission())
                        .replace("{{carDescription}}", car.getDescription())
                        .replace("{{carPrice}}", String.valueOf(car.getPrice()));

                sendHtmlEmail(userEmail, emailSubject, emailContent);

        }
    }

    private void sendHtmlEmail(String userEmail, String emailSubject, String emailContent) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        try {
            helper.setTo(userEmail);
            helper.setSubject(emailSubject);
            helper.setText(emailContent, true);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public byte[] generateQRCodeImage(String text) {
        ByteArrayOutputStream stream = QRCode
                .from(text)
                .withSize(250, 250)
                .to(ImageType.PNG)
                .stream();
        return stream.toByteArray();
    }

    private String loadEmailTemplateFromFile(String fileName) {

        try {
            ClassPathResource resource = new ClassPathResource("templates/" + fileName);
            if (resource.exists()) {
                try (InputStream inputStream = resource.getInputStream()) {
                    byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
                    return new String(bytes, StandardCharsets.UTF_8);
                }
            } else {
                throw new IOException("Email template file not found: " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public CarDto getCarById(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.map(Car::getCarDto).orElse(null);
    }

    @Override
    public boolean updateCar(Long carId, CarDto carDto) throws IOException {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            Car existingCar = optionalCar.get();
            if (carDto.getImage() != null)
                existingCar.setImage(carDto.getImage().getBytes());
            existingCar.setPrice(carDto.getPrice());
            existingCar.setYear(carDto.getYear());
            existingCar.setType(carDto.getType());
            existingCar.setDescription(carDto.getDescription());
            existingCar.setTransmission(carDto.getTransmission());
            existingCar.setColor(carDto.getColor());
            existingCar.setName(carDto.getName());
            existingCar.setBrand(carDto.getBrand());
            carRepository.save(existingCar);
            return true;
        } else {
            return false;
        }
    }
    @Override
    public List<BookAVisitDto> showAllBookings() {
        List<BookAVisitDto> list= bookAVisitRepository.findAll().stream().map(BookAVisit::getBookAVisitDto).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<BookAVisitDto> getBookings() {
        LocalDate currentDate = LocalDate.now().minusDays(4);
        Date currentDateMinus4Days = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<BookAVisit> bookings = bookAVisitRepository.findAllByOrderByVisitDateAsc();

        List<BookAVisitDto> filteredBookings = bookings.stream()
                .filter(visit -> visit.getVisitDate().after(currentDateMinus4Days))
                .map(BookAVisit::getBookAVisitDto)
                .collect(Collectors.toList());
        out.println("test");
        return filteredBookings;
    }

    @Override
    public boolean changeUserRole(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            if (existingUser.getUserRole() == UserRole.ADMIN) {
                existingUser.setUserRole(UserRole.CUSTOMER);
            } else {
                existingUser.setUserRole(UserRole.ADMIN);
            }
            userRepository.save(existingUser);
            return true;
        }
        return false;
}



    @Override
    public boolean changeBookingStatus(Long bookingId, String status) {
        Optional<BookAVisit> optionalBookAVisit = bookAVisitRepository.findById(bookingId);
        if (optionalBookAVisit.isPresent()){
            BookAVisit existingBookAVisit = optionalBookAVisit.get();
                if (Objects.equals(status, "APPROVED")){
                    existingBookAVisit.setBookAVisitEnum(BookAVisitEnum.APPROVED);
                }else
                    existingBookAVisit.setBookAVisitEnum(BookAVisitEnum.REJECTED);
                bookAVisitRepository.save(existingBookAVisit);
                return true;
        }
        return false;
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
    public CommentDTO createComment(Long carId , Long userId ,String content) {
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
        System.out.println(comments.get(0));
        return comments;
    }


    @Override
    public StatCustomerResponse calculateStat() {
        Long cars = carRepository.count();
        Long savedCars = savedCarRepository.count();
        Long bookings = bookAVisitRepository.count();
        Long approvedBookings = bookAVisitRepository.countByBookAVisitEnum(BookAVisitEnum.APPROVED);
        Long rejectedBookings = bookAVisitRepository.countByBookAVisitEnum(BookAVisitEnum.REJECTED);
        Long pendingBookings = bookAVisitRepository.countByBookAVisitEnum(BookAVisitEnum.PENDING);
        return new StatCustomerResponse(cars, savedCars, bookings, approvedBookings, rejectedBookings, pendingBookings);
    }

    @Override
    public boolean updateUser(Long userId, UpdateCustomerResponse updateCustomerResponse) throws IOException {
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
    public UpdateCustomerDto getUserById(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.map(User::getUpdateCustomerDto).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

}
