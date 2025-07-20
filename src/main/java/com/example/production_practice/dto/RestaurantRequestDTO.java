package com.example.production_practice.dto;

import com.example.production_practice.enums.CuisineType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Value
@Schema(description = "DTO запроса на создание/обновление ресторана")
public class RestaurantRequestDTO {
    @NotBlank
    @Schema(description = "Название ресторана")
    String name;

    @Schema(description = "Описание ресторана (может быть пустым)")
    String description;

    @NotNull
    @Schema(description = "Тип кухни", example = "ITALIAN")
    CuisineType cuisineType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Schema(description = "Средний чек на человека", example = "1500.00")
    BigDecimal averageCheck;
}
