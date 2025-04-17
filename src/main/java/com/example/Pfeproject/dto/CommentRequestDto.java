package com.example.Pfeproject.dto;

import lombok.Data;

@Data
public class CommentRequestDto {
    Long carId;
    Long userId;
    String content;
}
