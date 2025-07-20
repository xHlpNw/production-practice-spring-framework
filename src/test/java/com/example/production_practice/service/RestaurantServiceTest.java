package com.example.production_practice.service;

import com.example.production_practice.dto.RestaurantRequestDTO;
import com.example.production_practice.dto.RestaurantResponseDTO;
import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.enums.CuisineType;
import com.example.production_practice.mapper.RestaurantMapper;
import com.example.production_practice.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RestaurantServiceTest {
    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldCallRepositorySave() {
        RestaurantRequestDTO dto = new RestaurantRequestDTO("Name", "Desc", CuisineType.ITALIAN, new BigDecimal("1000"));
        Restaurant entity = new Restaurant();

        when(restaurantMapper.toEntity(dto)).thenReturn(entity);
        restaurantService.save(dto);
        verify(restaurantRepository).save(entity);
    }

    @Test
    void remove_shouldDeleteRestaurant() {
        Long id = 1L;
        when(restaurantRepository.existsById(id)).thenReturn(true);
        restaurantService.remove(id);
        verify(restaurantRepository).deleteById(id);
    }


    @Test
    void findAll_shouldReturnMappedList() {
        Restaurant restaurant = new Restaurant();
        RestaurantResponseDTO dto = new RestaurantResponseDTO(1L, "Name", "Desc", CuisineType.ITALIAN, BigDecimal.TEN, BigDecimal.ONE);
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));
        when(restaurantMapper.toResponseDTO(restaurant)).thenReturn(dto);

        List<RestaurantResponseDTO> result = restaurantService.findAll();

        assertThat(result).containsExactly(dto);
    }

    @Test
    void findById_shouldReturnMappedDto() {
        Long id = 1L;
        Restaurant restaurant = new Restaurant();
        RestaurantResponseDTO dto = new RestaurantResponseDTO(id, "Name", "Desc", CuisineType.ITALIAN, BigDecimal.TEN, BigDecimal.ONE);

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.toResponseDTO(restaurant)).thenReturn(dto);

        RestaurantResponseDTO result = restaurantService.findById(id);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void update_shouldModifyAndSave() {
        Long id = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        RestaurantRequestDTO dto = new RestaurantRequestDTO("NewName", "NewDesc", CuisineType.JAPANESE, new BigDecimal("2500"));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        restaurantService.update(id, dto);

        assertEquals("NewName", restaurant.getName());
        assertEquals("NewDesc", restaurant.getDescription());
        assertEquals(CuisineType.JAPANESE, restaurant.getCuisineType());
        assertEquals(new BigDecimal("2500"), restaurant.getAverageCheck());

        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void findByRating_shouldReturnPagedMappedResult() {
        BigDecimal rating = BigDecimal.valueOf(4.5);
        Pageable pageable = PageRequest.of(0, 10);
        Restaurant restaurant = new Restaurant();
        RestaurantResponseDTO dto = new RestaurantResponseDTO(1L, "Name", "Desc", CuisineType.ITALIAN, BigDecimal.TEN, rating);

        Page<Restaurant> page = new PageImpl<>(List.of(restaurant));
        when(restaurantRepository.findByRating(rating, pageable)).thenReturn(page);
        when(restaurantMapper.toResponseDTO(restaurant)).thenReturn(dto);

        Page<RestaurantResponseDTO> result = restaurantService.findByRating(rating, pageable);

        assertThat(result.getContent()).containsExactly(dto);
    }

    @Test
    void remove_shouldThrowExceptionWhenRestaurantNotFound() {
        Long nonExistentId = 99L;
        when(restaurantRepository.existsById(nonExistentId)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            restaurantService.remove(nonExistentId);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo("Ресторан не найден");

        verify(restaurantRepository, never()).deleteById(nonExistentId);
    }
}
