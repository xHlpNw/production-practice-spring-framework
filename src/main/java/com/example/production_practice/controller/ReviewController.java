package com.example.production_practice.controller;

import com.example.production_practice.dto.RestaurantRequestDTO;
import com.example.production_practice.dto.ReviewRequestDTO;
import com.example.production_practice.dto.ReviewResponseDTO;
import com.example.production_practice.dto.VisitorResponseDTO;
import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.service.ReviewService;
import com.example.production_practice.service.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Получить список всех отзывов")
    @ApiResponse(
            responseCode = "200",
            description = "Успешно получен список отзывов",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewResponseDTO.class)))
    )
    @GetMapping
    public List<ReviewResponseDTO> getAllReviews() {
        return reviewService.findAll();
    }

    @Operation(summary = "Получить отзыв по ID посетителя и ID ресторана")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Отзыв найден",
                    content = @Content(schema = @Schema(implementation = VisitorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    @GetMapping("/{visitorId}/{restaurantId}")
    public ReviewResponseDTO getReview(
            @Parameter(description = "ID посетителя", example = "1")
            @PathVariable Long visitorId,

            @Parameter(description = "ID ресторана", example = "1")
            @PathVariable Long restaurantId) {
        return reviewService.findById(visitorId, restaurantId);
    }

    @Operation(summary = "Добавить отзыв")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Отзыв добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
    })
    @PostMapping
    public void addReview(
            @Parameter(description = "ID посетителя", example = "1")
            @Valid @RequestBody Long visitorId,

            @Parameter(description = "ID ресторана", example = "1")
            @Valid @RequestBody Long restaurantId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные нового ресторана",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RestaurantRequestDTO.class))
            )
            @Valid @RequestBody ReviewRequestDTO reviewDTO) {
        reviewService.save(visitorId, restaurantId, reviewDTO);
    }

    @Operation(
            summary = "Обновить информацию об отзыве",
            description = "Обновляет данные отзыва по ID посетителя и ID ресторана. Поле оценки - обязательное."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Отзыв успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    @PutMapping("/{visitorId}/{restaurantId}")
    public void updateReview(
            @Parameter(description = "ID посетителя", example = "1")
            @PathVariable Long visitorId,

            @Parameter(description = "ID ресторана", example = "1")
            @PathVariable Long restaurantId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления отзыва",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReviewRequestDTO.class))
            )
            @Valid @RequestBody ReviewRequestDTO review) {
        reviewService.update(visitorId, restaurantId, review);
    }

    @Operation(summary = "Удалить отзыв по id посетителя и id ресторана")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Отзыв успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    @DeleteMapping("/{visitorId}/{restaurantId}")
    public void deleteReview(
            @Parameter(description = "id посетителя", example = "1")
            @PathVariable Long visitorId,

            @Parameter(description = "id ресторана", example = "1")
            @PathVariable Long restaurantId) {
        reviewService.remove(visitorId, restaurantId);
    }
}
