package com.example.production_practice.entity;

//  Класс с оценками посетителей с полями id посетителя, id ресторана, оценка (int), текст отзыва (может быть пустым).

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "review")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @EmbeddedId
    private ReviewID id;

    @Column(nullable = false)
    private Integer score;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitor_id", nullable = false)
    @MapsId("visitorId")
    private Visitor visitor;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @MapsId("restaurantId")
    private Restaurant restaurant;
}
