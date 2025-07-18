package com.example.production_practice.dto;

import com.example.production_practice.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor
@Getter
@Value
@Schema(description = "DTO запроса на создание/обновление посетителя")
public class VisitorRequestDTO {
    @Schema(description = "Имя посетителя")
    String name;

    @NotNull
    @Min(0)
    @Schema(description = "Возраст посетителя")
    Integer age;

    @NotNull
    @Pattern(regexp = "MALE|FEMALE|UNKNOWN")
    @Schema(description = "Пол посетителя")
    Gender gender;
}
