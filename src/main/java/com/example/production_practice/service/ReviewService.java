package com.example.production_practice.service;

//Слой бизнес логики (сервисы). В каждый класс внедрить соответствующий(-ие) репозиторий(-ии).
//  ●	Класс для работы с данными об оценках, методы: save, remove, findAll
//        Не забудьте, что после добавления оценки и сохранения её необходимо пересчитать среднюю оценку ресторана.

import com.example.production_practice.dto.ReviewRequestDTO;
import com.example.production_practice.dto.ReviewResponseDTO;
import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.mapper.ReviewMapper;
import com.example.production_practice.repository.RestaurantRepository;
import com.example.production_practice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewMapper reviewMapper;

    public void save(Long visitorId, Long restaurantId, ReviewRequestDTO reviewDTO) {
        Review review = reviewMapper.toEntity(reviewDTO, visitorId, restaurantId);
        reviewRepository.save(review);
        recalculateRating(restaurantId);
    }

    public void remove(Long visitorId, Long restaurantId) {
        reviewRepository.remove(reviewRepository.findById(visitorId, restaurantId));
        recalculateRating(restaurantId);
    }

    public List<ReviewResponseDTO> findAll() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ReviewResponseDTO findById(Long visitorId, Long restaurantId) {
        return reviewMapper.toResponseDTO(reviewRepository.findById(visitorId, restaurantId));
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

    public void update(Long visitorId, Long restaurantId, ReviewRequestDTO review) {
        Review oldReview = reviewRepository.findById(visitorId, restaurantId);
        oldReview.setComment(review.getComment());
        oldReview.setScore(review.getScore());
    }
}
