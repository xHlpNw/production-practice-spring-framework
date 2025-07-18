package com.example.production_practice.dto;

import com.example.production_practice.enums.CuisineType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Value
@Schema(description = "DTO ответа на запрос")
public class RestaurantResponseDTO {
    Integer id;
    String name;
    String description;
    CuisineType cuisineType;
    BigDecimal averageCheck;
    BigDecimal rating;
}
