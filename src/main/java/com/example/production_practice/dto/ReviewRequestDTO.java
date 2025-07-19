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
    @Schema(description = "ID посетителя", example = "1")
    @NotNull
    Long visitorId;

    @NotNull
    @Schema(description = "ID ресторана", example = "1")
    Long restaurantId;

    @NotNull
    @Schema(description = "Оценка", example = "5")
    @Min(1)
    @Max(5)
    Integer score;

    @Schema(description = "Комментарий (может быть пустым)")
    String comment;
}
