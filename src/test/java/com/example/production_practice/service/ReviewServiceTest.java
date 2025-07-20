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
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldThrowIfReviewExists() {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 2L, 5, "Good");
        ReviewID id = new ReviewID(dto.getVisitorId(), dto.getRestaurantId());

        when(reviewRepository.existsById(id)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> reviewService.save(dto));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());

    }

    @Test
    void save_shouldSaveReviewAndRecalculateRating() {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 2L, 5, "Good");
        ReviewID id = new ReviewID(dto.getVisitorId(), dto.getRestaurantId());

        Visitor visitor = new Visitor();
        Restaurant restaurant = new Restaurant();
        restaurant.setId(dto.getRestaurantId());

        when(reviewRepository.existsById(id)).thenReturn(false);
        when(visitorRepository.findById(dto.getVisitorId())).thenReturn(Optional.of(visitor));
        when(restaurantRepository.findById(dto.getRestaurantId())).thenReturn(Optional.of(restaurant));

        reviewService.save(dto);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        Review savedReview = reviewCaptor.getValue();

        assertEquals(dto.getScore(), savedReview.getScore());
        assertEquals(dto.getComment(), savedReview.getComment());
        assertEquals(visitor, savedReview.getVisitor());
        assertEquals(restaurant, savedReview.getRestaurant());

        verify(reviewRepository).save(any(Review.class));
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void findAll_shouldReturnMappedList() {
        Review review = new Review();
        ReviewResponseDTO dto = new ReviewResponseDTO(1L, 2L, 5, "Nice");

        when(reviewRepository.findAll()).thenReturn(List.of(review));
        when(reviewMapper.toResponseDTO(review)).thenReturn(dto);

        List<ReviewResponseDTO> result = reviewService.findAll();

        assertThat(result).containsExactly(dto);
    }

    @Test
    void findById_shouldReturnMappedDTO() {
        Long visitorId = 1L;
        Long restaurantId = 2L;
        ReviewID id = new ReviewID(visitorId, restaurantId);
        Review review = new Review();
        ReviewResponseDTO dto = new ReviewResponseDTO(visitorId, restaurantId, 5, "Good");

        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(reviewMapper.toResponseDTO(review)).thenReturn(dto);

        ReviewResponseDTO result = reviewService.findById(visitorId, restaurantId);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void update_shouldModifyReviewAndRecalculate() {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 2L, 4, "Updated comment");
        ReviewID id = new ReviewID(dto.getVisitorId(), dto.getRestaurantId());
        Review review = new Review();
        Restaurant restaurant = new Restaurant();
        review.setRestaurant(restaurant);

        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));

        reviewService.update(dto);

        assertEquals(dto.getScore(), review.getScore());
        assertEquals(dto.getComment(), review.getComment());

        verify(reviewRepository).save(review);
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void findAllSorting_shouldReturnSortedList() {
        Review review = new Review();
        ReviewResponseDTO dto = new ReviewResponseDTO(1L, 2L, 5, "Nice");

        when(reviewRepository.findAll(Sort.by("score").descending())).thenReturn(List.of(review));
        when(reviewMapper.toResponseDTO(review)).thenReturn(dto);

        List<ReviewResponseDTO> result = reviewService.findAllSorting();

        assertThat(result).containsExactly(dto);
    }

    @Test
    void findAllPageable_shouldReturnPagedMappedResult() {
        Review review = new Review();
        ReviewResponseDTO dto = new ReviewResponseDTO(1L, 2L, 5, "Nice");
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> page = new PageImpl<>(List.of(review));

        when(reviewRepository.findAll(pageable)).thenReturn(page);
        when(reviewMapper.toResponseDTO(review)).thenReturn(dto);

        Page<ReviewResponseDTO> result = reviewService.findAllPageable(0, 10);

        assertThat(result.getContent()).containsExactly(dto);
    }

    @Test
    void findAllPageableAndSorting_shouldReturnSortedPagedMappedResult() {
        Review review = new Review();
        ReviewResponseDTO dto = new ReviewResponseDTO(1L, 2L, 5, "Nice");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("score").descending());
        Page<Review> page = new PageImpl<>(List.of(review));

        when(reviewRepository.findAll(pageable)).thenReturn(page);
        when(reviewMapper.toResponseDTO(review)).thenReturn(dto);

        Page<ReviewResponseDTO> result = reviewService.findAllPageableAndSorting(0, 10);

        assertThat(result.getContent()).containsExactly(dto);
    }

    @Test
    void findAllByRestaurantId_shouldReturnPagedMappedResult() {
        Review review = new Review();
        ReviewResponseDTO dto = new ReviewResponseDTO(1L, 2L, 5, "Nice");
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> page = new PageImpl<>(List.of(review));
        Long restaurantId = 2L;

        when(reviewRepository.findAllByRestaurantId(restaurantId, pageable)).thenReturn(page);
        when(reviewMapper.toResponseDTO(review)).thenReturn(dto);

        Page<ReviewResponseDTO> result = reviewService.findAllByRestaurantId(restaurantId, pageable);

        assertThat(result.getContent()).containsExactly(dto);
    }
}
