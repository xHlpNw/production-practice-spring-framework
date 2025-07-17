package com.example.production_practice.entity;

//  Класс с оценками посетителей с полями id посетителя, id ресторана, оценка (int), текст отзыва (может быть пустым).

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Review {
    @NonNull
    private Long visitorId;

    @NonNull
    private Long restaurantId;

    @NonNull
    private int score;

    private String comment;
}
