package com.example.production_practice.controller;// RestaurantControllerTest.java

import com.example.production_practice.dto.RestaurantRequestDTO;
import com.example.production_practice.dto.RestaurantResponseDTO;
import com.example.production_practice.enums.CuisineType;
import com.example.production_practice.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/restaurants - success")
    void getAllRestaurants_success() throws Exception {
        List<RestaurantResponseDTO> list = List.of(
                new RestaurantResponseDTO(1L, "KFC", "Fried chicken", CuisineType.AMERICAN, new BigDecimal("1000.00"), new BigDecimal("4.2"))
        );
        when(restaurantService.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("KFC"));

        verify(restaurantService).findAll();
    }

    @Test
    @DisplayName("GET /api/restaurants/{id} - found")
    void getRestaurantById_found() throws Exception {
        RestaurantResponseDTO dto = new RestaurantResponseDTO(2L, "SushiBoom", "Sushi", CuisineType.JAPANESE, new BigDecimal("2000.00"), new BigDecimal("4.7"));
        when(restaurantService.findById(2L)).thenReturn(dto);

        mockMvc.perform(get("/api/restaurants/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.cuisineType").value("JAPANESE"))
                .andExpect(jsonPath("$.rating").value("4.7"));

        verify(restaurantService).findById(2L);
    }

    @Test
    @DisplayName("GET /api/restaurants/{id} - not found")
    void getRestaurantById_notFound() throws Exception {
        when(restaurantService.findById(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        mockMvc.perform(get("/api/restaurants/99"))
                .andExpect(status().isNotFound()); // ожидаем HTTP 404
    }

    @Test
    @DisplayName("POST /api/restaurants - success")
    void addRestaurant_success() throws Exception {
        RestaurantRequestDTO dto = new RestaurantRequestDTO("PizzaHut", "Best pizza", CuisineType.ITALIAN, new BigDecimal("1500.00"));

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(restaurantService).save(any(RestaurantRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/restaurants - invalid request")
    void addRestaurant_invalid() throws Exception {
        // отсутствует обязательное поле name
        RestaurantRequestDTO dto = new RestaurantRequestDTO("", "Best pizza", CuisineType.ITALIAN, new BigDecimal("1500.00"));

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/restaurants/{id} - success")
    void updateRestaurant_success() throws Exception {
        RestaurantRequestDTO dto = new RestaurantRequestDTO("Pizza", "Tasty", CuisineType.ITALIAN, new BigDecimal("1300.00"));

        mockMvc.perform(put("/api/restaurants/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(restaurantService).update(eq(10L), any(RestaurantRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/restaurants/{id} - success")
    void deleteRestaurant_success() throws Exception {
        mockMvc.perform(delete("/api/restaurants/5"))
                .andExpect(status().isOk());
        verify(restaurantService).remove(5L);
    }

    @Test
    @DisplayName("GET /api/restaurants/by-rating - pageable")
    void getRestaurantsByRating() throws Exception {
        List<RestaurantResponseDTO> content = List.of(
                new RestaurantResponseDTO(1L, "HighRated", "Nice", CuisineType.FRENCH, new BigDecimal("4500.00"), new BigDecimal("4.9"))
        );
        Page<RestaurantResponseDTO> page = new PageImpl<>(content);

        when(restaurantService.findByRating(eq(new BigDecimal("4.5")), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/restaurants/by-rating")
                        .param("minRating", "4.5")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "rating")
                        .param("sortDir", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("HighRated"));
    }
}
