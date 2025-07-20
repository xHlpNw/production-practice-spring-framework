package com.example.production_practice.dto;

import com.example.production_practice.enums.CuisineType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Schema(description = "DTO ответа на запрос")
public class RestaurantResponseDTO {
    Long id;
    String name;
    String description;
    CuisineType cuisineType;
    BigDecimal averageCheck;
    BigDecimal rating;
}
