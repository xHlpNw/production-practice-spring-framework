package com.example.production_practice.service;

import com.example.production_practice.dto.VisitorRequestDTO;
import com.example.production_practice.dto.VisitorResponseDTO;
import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.ReviewID;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.enums.Gender;
import com.example.production_practice.mapper.VisitorMapper;
import com.example.production_practice.repository.RestaurantRepository;
import com.example.production_practice.repository.ReviewRepository;
import com.example.production_practice.repository.VisitorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class VisitorServiceTest {
    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private VisitorMapper visitorMapper;

    @InjectMocks
    private VisitorService visitorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldCallRepositorySave() {
        VisitorRequestDTO dto = new VisitorRequestDTO("John", 25, Gender.MALE);
        Visitor visitor = new Visitor();
        when(visitorMapper.toEntity(dto)).thenReturn(visitor);

        visitorService.save(dto);

        verify(visitorRepository).save(visitor);
    }

    @Test
    void findAll_shouldReturnMappedList() {
        Visitor visitor = new Visitor();
        VisitorResponseDTO dto = new VisitorResponseDTO(1L, "John", 30, Gender.MALE);
        when(visitorRepository.findAll()).thenReturn(List.of(visitor));
        when(visitorMapper.toResponseDTO(visitor)).thenReturn(dto);

        List<VisitorResponseDTO> result = visitorService.findAll();

        assertThat(result).containsExactly(dto);
    }

    @Test
    void findById_shouldReturnMappedDTO() {
        Long id = 1L;
        Visitor visitor = new Visitor();
        VisitorResponseDTO dto = new VisitorResponseDTO(id, "Ann", 22, Gender.FEMALE);
        when(visitorRepository.findById(id)).thenReturn(Optional.of(visitor));
        when(visitorMapper.toResponseDTO(visitor)).thenReturn(dto);

        VisitorResponseDTO result = visitorService.findById(id);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void update_shouldUpdateAndSaveVisitor() {
        Long id = 1L;
        Visitor visitor = new Visitor();
        visitor.setId(id);
        VisitorRequestDTO dto = new VisitorRequestDTO("NewName", 28, Gender.FEMALE);

        when(visitorRepository.findById(id)).thenReturn(Optional.of(visitor));

        visitorService.update(id, dto);

        assertEquals("NewName", visitor.getName());
        assertEquals(28, visitor.getAge());
        assertEquals(Gender.FEMALE, visitor.getGender());
        verify(visitorRepository).save(visitor);
    }

    @Test
    void remove_shouldDeleteVisitorAndUpdateRatings() {
        Long visitorId = 1L;
        Visitor visitor = new Visitor();
        Review review1 = new Review();
        Review review2 = new Review();

        review1.setId(new ReviewID(visitorId, 10L));
        review2.setId(new ReviewID(visitorId, 20L));

        visitor.setReviews(new ArrayList<>(List.of(review1, review2)));

        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId(10L);
        restaurant1.setReviews(List.of(new Review(null, 4, "", visitor, restaurant1)));

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setId(20L);
        restaurant2.setReviews(List.of(new Review(null, 5, "", visitor, restaurant2)));

        when(visitorRepository.findById(visitorId)).thenReturn(Optional.of(visitor));
        when(restaurantRepository.findAllById(List.of(10L, 20L))).thenReturn(List.of(restaurant1, restaurant2));
        when(reviewRepository.findAverageScoreByRestaurantId(10L)).thenReturn(BigDecimal.valueOf(4));
        when(reviewRepository.findAverageScoreByRestaurantId(20L)).thenReturn(BigDecimal.valueOf(5));

        visitorService.remove(visitorId);

        verify(visitorRepository).deleteById(visitorId);
        verify(restaurantRepository, times(2)).save(any(Restaurant.class));
        assertThat(restaurant1.getRating()).isEqualByComparingTo(BigDecimal.valueOf(4.0));
        assertThat(restaurant2.getRating()).isEqualByComparingTo(BigDecimal.valueOf(5.0));
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenVisitorNotFound() {
        Long id = 999L;
        when(visitorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> visitorService.findById(id));
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenVisitorNotFound() {
        Long id = 999L;
        VisitorRequestDTO dto = new VisitorRequestDTO("Name", 30, Gender.MALE);
        when(visitorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> visitorService.update(id, dto));
    }

    @Test
    void save_shouldThrowNullPointerException_whenDtoIsNull() {
        assertThrows(NullPointerException.class, () -> visitorService.save(null));
    }

}
