package com.example.production_practice.controller;

import com.example.production_practice.dto.ReviewRequestDTO;
import com.example.production_practice.dto.ReviewResponseDTO;
import com.example.production_practice.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/reviews - get all")
    void getAllReviews() throws Exception {
        List<ReviewResponseDTO> reviews = List.of(
                new ReviewResponseDTO(1L, 2L, 5, "Good!"),
                new ReviewResponseDTO(2L, 3L, 4, "Nice")
        );
        when(reviewService.findAll()).thenReturn(reviews);

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].score").value(5));
    }

    @Test
    @DisplayName("GET /api/reviews/{visitorId}/{restaurantId} - found")
    void getReviewById_found() throws Exception {
        ReviewResponseDTO review = new ReviewResponseDTO(1L, 2L, 5, "Nice!");
        when(reviewService.findById(1L, 2L)).thenReturn(review);

        mockMvc.perform(get("/api/reviews/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(5))
                .andExpect(jsonPath("$.visitorId").value(1));
    }

    @Test
    @DisplayName("GET /api/reviews/{visitorId}/{restaurantId} - not found")
    void getReviewById_notFound() throws Exception {
        when(reviewService.findById(99L, 100L))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Not found"));
        mockMvc.perform(get("/api/reviews/99/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/reviews - valid")
    void addReview_success() throws Exception {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 2L, 4, "Cool!");
        ReviewResponseDTO responseDTO = new ReviewResponseDTO(1L, 2L, 4, "Cool!");

        when(reviewService.save(any(ReviewRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(4))
                .andExpect(jsonPath("$.comment").value("Cool!"));
    }

    @Test
    @DisplayName("POST /api/reviews - invalid score")
    void addReview_failValidation() throws Exception {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 2L, 10, "Bad!"); // 10 вне диапазона @Max(5)
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/reviews/{visitorId}/{restaurantId} - ok")
    void updateReview_success() throws Exception {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 2L, 3, "Changed comment");
        doNothing().when(reviewService).update(any(ReviewRequestDTO.class));
        mockMvc.perform(put("/api/reviews/1/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/reviews/{visitorId}/{restaurantId} - not found")
    void updateReview_notFound() throws Exception {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 2L, 3, "Changed comment");
        Mockito.doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Not found"))
                .when(reviewService).update(any(ReviewRequestDTO.class));
        mockMvc.perform(put("/api/reviews/1/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/reviews/{visitorId}/{restaurantId} - ok")
    void deleteReview_success() throws Exception {
        doNothing().when(reviewService).remove(1L, 2L);
        mockMvc.perform(delete("/api/reviews/1/2"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/reviews/{visitorId}/{restaurantId} - not found")
    void deleteReview_notFound() throws Exception {
        Mockito.doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Not found"))
                .when(reviewService).remove(99L, 100L);
        mockMvc.perform(delete("/api/reviews/99/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/reviews/page - pageable")
    void getAllPageable() throws Exception {
        List<ReviewResponseDTO> pageContent = List.of(
                new ReviewResponseDTO(1L, 2L, 3, "Ok")
        );
        Page<ReviewResponseDTO> page = new PageImpl<>(pageContent, PageRequest.of(0, 10), 1);
        when(reviewService.findAllPageable(0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/reviews/page?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].score").value(3));
    }

    @Test
    @DisplayName("GET /api/reviews/by-restaurant/{restaurantId} - pageable and sorted")
    void getReviewsByRestaurant() throws Exception {
        List<ReviewResponseDTO> pageContent = List.of(
                new ReviewResponseDTO(1L, 2L, 3, "Ok")
        );
        Page<ReviewResponseDTO> page = new PageImpl<>(pageContent, PageRequest.of(0, 10), 1);
        when(reviewService.findAllByRestaurantId(eq(2L), any())).thenReturn(page);

        mockMvc.perform(get("/api/reviews/by-restaurant/2?page=0&size=10&sortBy=score&sortDir=desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].score").value(3));
    }

    @Test
    @DisplayName("GET /api/reviews/sorted - pageable sorted")
    void getAllPageableAndSorted() throws Exception {
        List<ReviewResponseDTO> pageContent = List.of(
                new ReviewResponseDTO(1L, 2L, 5, "Sorted!")
        );
        Page<ReviewResponseDTO> page = new PageImpl<>(pageContent, PageRequest.of(0, 10), 1);
        when(reviewService.findAllPageableAndSorting(0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/reviews/sorted?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].comment").value("Sorted!"));
    }
}
