package com.example.production_practice.entity;

//  Класс с оценками посетителей с полями id посетителя, id ресторана, оценка (int), текст отзыва (может быть пустым).

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Review {
    @NotNull
    private Long visitorId;

    @NotNull
    private Long restaurantId;

    @NotNull
    private Integer score;

    private String comment;
}
