package com.example.Pfeproject.entity;

import com.example.Pfeproject.dto.BookAVisitDto;
import com.example.Pfeproject.enums.BookAVisitEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class BookAVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date visitDate;
    private String visitTime;
    private String brand;
    private String color;
    private String name;
    private String type;
    private String transmission;
    private String description;
    private Long price;
    private Date year;
    private BookAVisitEnum bookAVisitEnum;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "car_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Car car;



    public BookAVisitDto getBookAVisitDto() {
        BookAVisitDto bookAVisitDto = new BookAVisitDto();
        bookAVisitDto.setId(id);
        bookAVisitDto.setVisitDate(visitDate);
        bookAVisitDto.setVisitTime(visitTime);
        bookAVisitDto.setBrand(brand);
        bookAVisitDto.setColor(color);
        bookAVisitDto.setName(name);
        bookAVisitDto.setType(type);
        bookAVisitDto.setTransmission(transmission);
        bookAVisitDto.setDescription(description);
        bookAVisitDto.setPrice(price);
        bookAVisitDto.setYear(year);
        bookAVisitDto.setCarId(car.getId());
        bookAVisitDto.setEmail(user.getEmail());
        bookAVisitDto.setUsername(user.getName());
        bookAVisitDto.setUserId(user.getId());
        bookAVisitDto.setBookAVisitEnum(bookAVisitEnum);
        return bookAVisitDto;
    }


}
