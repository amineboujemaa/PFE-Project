package com.example.Pfeproject.dto;

import com.example.Pfeproject.enums.BookAVisitEnum;
import lombok.Data;

import java.util.Date;

@Data
public class BookAVisitDto {

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
    private Long carId;
    private Long userId;
    private String username;
    private String email;


}
