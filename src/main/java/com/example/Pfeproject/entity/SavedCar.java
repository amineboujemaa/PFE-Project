package com.example.Pfeproject.entity;

import com.example.Pfeproject.dto.SavedCarDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Data
public class SavedCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private boolean status ;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn ( name = "user_id" ,nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn ( name = "car_id" ,nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Car car;

    public SavedCarDto getSavedCarDto(){
        SavedCarDto savedCarDto = new SavedCarDto();
        savedCarDto.setId(id);
        savedCarDto.setStatus(status);
        savedCarDto.setCarId(car.getId());
        savedCarDto.setUserId(user.getId());
        savedCarDto.setName(car.getName());
        savedCarDto.setBrand(car.getBrand());
        savedCarDto.setColor(car.getColor());
        savedCarDto.setPrice(car.getPrice());
        savedCarDto.setDescription(car.getDescription());
        savedCarDto.setType(car.getType());
        savedCarDto.setTransmission(car.getTransmission());
        savedCarDto.setYear(car.getYear());
        savedCarDto.setReturnedImage(car.getImage());
        return savedCarDto;
    }


}

