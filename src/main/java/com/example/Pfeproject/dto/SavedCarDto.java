package com.example.Pfeproject.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class SavedCarDto {
    private Long id ;
    private boolean status ;


    private Long userId;

    private Long carId;
    private String brand;
    private String color;
    private String name;
    private String type;
    private String transmission;
    private String description;
    private Long price;
    private Date year;
    private MultipartFile image;
    private byte[] returnedImage;



}
