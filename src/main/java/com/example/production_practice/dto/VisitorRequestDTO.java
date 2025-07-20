package com.example.production_practice.dto;

import com.example.production_practice.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
@Schema(description = "DTO запроса на создание/обновление посетителя")
public class VisitorRequestDTO {
    @Schema(description = "Имя посетителя")
    @Size(max = 100)
    String name;

    @NotNull
    @Min(1)
    @Schema(description = "Возраст посетителя")
    Integer age;

    @NotNull
    @Schema(description = "Пол посетителя")
    Gender gender;
}
