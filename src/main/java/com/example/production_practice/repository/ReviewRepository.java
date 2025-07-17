package com.example.production_practice.repository;

//В каждом классе хранить данные в приватном финальном поле типа List, все методы работают именно с этим полем.
//●	Класс для работы с данными об оценках, методы: save, remove, findAll, findById

import com.example.production_practice.entity.Review;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepository {
    private final List<Review> reviews = new ArrayList<>();

    public void save(Review review) {
        reviews.add(review);
    }

    public void remove(Review review) {
        reviews.remove(review);
    }

    public List<Review> findAll() {
        return List.copyOf(reviews);
    }

    public Optional<Review> findById(Long visitorId, Long restaurantId) {
        return reviews.stream()
                .filter(r -> r.getVisitorId().equals(visitorId) && r.getRestaurantId().equals(restaurantId))
                .findFirst();
    }
}
