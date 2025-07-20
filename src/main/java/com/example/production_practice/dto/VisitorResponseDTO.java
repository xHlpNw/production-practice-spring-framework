package com.example.production_practice.dto;

import com.example.production_practice.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "DTO ответа на запрос")
public class VisitorResponseDTO {
    Long id;
    String name;
    Integer age;
    Gender gender;
}
