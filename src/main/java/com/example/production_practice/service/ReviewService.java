package com.example.production_practice.service;

//Слой бизнес логики (сервисы). В каждый класс внедрить соответствующий(-ие) репозиторий(-ии).
//  ●	Класс для работы с данными об оценках, методы: save, remove, findAll
//        Не забудьте, что после добавления оценки и сохранения её необходимо пересчитать среднюю оценку ресторана.

import com.example.production_practice.entity.Review;
import com.example.production_practice.repository.RestaurantRepository;
import com.example.production_practice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    public void save(Review review) {
        reviewRepository.save(review);
        recalculateRating(review.getRestaurantId());
    }

    public void remove(Review review) {
        reviewRepository.remove(review);
        recalculateRating(review.getRestaurantId());
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Optional<Review> findById(Long visitorId, Long restaurantId) {
        return reviewRepository.findById(visitorId, restaurantId);
    }

    private void recalculateRating(Long restaurantId) {
        List<Review> reviews = reviewRepository.findAll().stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .toList();
        BigDecimal average;
        if (!reviews.isEmpty()) {
            long sum = reviews.stream().mapToLong(Review::getScore).sum();
            average = BigDecimal.valueOf(sum)
                    .divide(BigDecimal.valueOf(reviews.size()), 1, RoundingMode.HALF_UP);
        } else {
            average = BigDecimal.ZERO;
        }
        restaurantRepository.findAll().stream()
                .filter(r -> r.getId().equals(restaurantId))
                .findFirst()
                .ifPresent(r -> r.setRating(average));
    }
}
