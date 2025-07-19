package com.example.production_practice.service;

//Слой бизнес логики (сервисы). В каждый класс внедрить соответствующий(-ие) репозиторий(-ии).
//  ●	Класс для работы с данными об оценках, методы: save, remove, findAll
//        Не забудьте, что после добавления оценки и сохранения её необходимо пересчитать среднюю оценку ресторана.

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
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public void save(ReviewRequestDTO reviewDTO) {
        if (reviewRepository.existsById(new ReviewID(reviewDTO.getVisitorId(), reviewDTO.getRestaurantId()))) {
            throw new EntityExistsException("Review already exists for visitor " + reviewDTO.getVisitorId() + " and restaurant " + reviewDTO.getRestaurantId());
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
    }

    public void remove(Long visitorId, Long restaurantId) {
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
        List<Review> reviews = reviewRepository.findAllByIdRestaurantId(restaurant.getId());
        BigDecimal score = BigDecimal.ZERO;
        if (!reviews.isEmpty()) {
            score = reviews.stream()
                    .map(review -> BigDecimal.valueOf(review.getScore()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(reviews.size()), 1, RoundingMode.HALF_UP);
        }
        restaurant.setRating(score);
        restaurantRepository.save(restaurant);
    }

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
        return reviews.stream().map(reviewMapper::toResponseDTO).toList();
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
