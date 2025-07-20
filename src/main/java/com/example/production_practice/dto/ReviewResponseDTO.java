package com.example.production_practice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "DTO ответа на запрос")
public class ReviewResponseDTO {
    Long visitorId;
    Long restaurantId;
    Integer score;
    String comment;
}
