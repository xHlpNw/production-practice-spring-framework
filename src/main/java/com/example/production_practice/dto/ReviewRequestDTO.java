package com.example.production_practice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor
@Getter
@Value
@Schema(description = "DTO запроса на создание/обновление отзыва")
public class ReviewRequestDTO {
    @NotNull
    @Schema(description = "Оценка", example = "5")
    @Min(1)
    @Max(5)
    Integer score;

    @Schema(description = "Комментарий (может быть пустым)")
    String comment;
}
