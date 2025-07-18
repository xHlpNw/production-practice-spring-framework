package com.example.production_practice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

@AllArgsConstructor
@Getter
@Value
@Schema(description = "DTO ответа на запрос")
public class ReviewResponseDTO {
    Long visitorId;
    Long restaurantId;
    Integer score;
    String comment;
}
