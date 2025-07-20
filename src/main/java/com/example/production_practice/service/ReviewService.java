package com.example.production_practice.service;

import com.example.production_practice.dto.ReviewRequestDTO;
import com.example.production_practice.dto.ReviewResponseDTO;
import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.ReviewID;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.mapper.ReviewMapper;
import com.example.production_practice.repository.RestaurantRepository;
import com.example.production_practice.repository.ReviewRepository;
import com.example.production_practice.repository.VisitorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final VisitorRepository visitorRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponseDTO save(ReviewRequestDTO reviewDTO) {
        if (reviewRepository.existsById(new ReviewID(reviewDTO.getVisitorId(), reviewDTO.getRestaurantId()))) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Отзыв уже существует для посетителя " + reviewDTO.getVisitorId() + " и ресторана " + reviewDTO.getRestaurantId()
            );
        }
        Visitor visitor = visitorRepository.findById(reviewDTO.getVisitorId())
                .orElseThrow(() -> new EntityNotFoundException("Visitor not found"));
        Restaurant restaurant = restaurantRepository.findById(reviewDTO.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        var review = new Review();
        review.setId(new ReviewID(reviewDTO.getVisitorId(), reviewDTO.getRestaurantId()));
        review.setScore(reviewDTO.getScore());
        review.setComment(reviewDTO.getComment());
        review.setVisitor(visitor);
        review.setRestaurant(restaurant);

        reviewRepository.save(review);
        recalculateRestaurantRating(review.getRestaurant());
        return reviewMapper.toResponseDTO(review);
    }

    @Transactional
    public void remove(Long visitorId, Long restaurantId) {
        if (!reviewRepository.existsById(new ReviewID(visitorId, restaurantId))) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Отзыв не найден для посетителя " + visitorId + " и ресторана " + restaurantId
            );
        }
        reviewRepository.deleteById(new ReviewID(visitorId, restaurantId));
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        recalculateRestaurantRating(restaurant);
    }

    public List<ReviewResponseDTO> findAll() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ReviewResponseDTO findById(Long visitorId, Long restaurantId) {
        return reviewMapper.toResponseDTO(reviewRepository.findById(new ReviewID(visitorId, restaurantId)).orElseThrow());
    }

    private void recalculateRestaurantRating(Restaurant restaurant) {
        BigDecimal averageScore = reviewRepository.findAverageScoreByRestaurantId(restaurant.getId());
        if (averageScore == null) {
            averageScore = BigDecimal.ZERO;
        } else {
            averageScore = averageScore.setScale(1, RoundingMode.HALF_UP);
        }
        restaurant.setRating(averageScore);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    public void update(ReviewRequestDTO reviewDTO) {
        ReviewID reviewId = new ReviewID(reviewDTO.getVisitorId(), reviewDTO.getRestaurantId());

        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        existingReview.setComment(reviewDTO.getComment());
        existingReview.setScore(reviewDTO.getScore());
        reviewRepository.save(existingReview);
        recalculateRestaurantRating(existingReview.getRestaurant());
    }

    public List<ReviewResponseDTO> findAllSorting(){
        List<Review> reviews = reviewRepository.findAll(Sort.by("score").descending());
        return reviews.stream()
                .map(reviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<ReviewResponseDTO> findAllPageable(int page, int size) {
        Page<Review> reviews = reviewRepository.findAll(PageRequest.of(page, size));
        return reviews.map(reviewMapper::toResponseDTO);
    }

    public Page<ReviewResponseDTO> findAllPageableAndSorting(int page, int size) {
        Page<Review> reviews = reviewRepository.findAll(PageRequest.of(page, size, Sort.by("score").descending()));
        return reviews.map(reviewMapper::toResponseDTO);
    }

    public Page<ReviewResponseDTO> findAllByRestaurantId(Long restaurantId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByRestaurantId(restaurantId, pageable);
        return reviews.map(reviewMapper::toResponseDTO);
    }

}
