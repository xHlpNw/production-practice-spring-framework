package com.example.production_practice;

import com.example.production_practice.dto.RestaurantRequestDTO;
import com.example.production_practice.dto.ReviewRequestDTO;
import com.example.production_practice.dto.VisitorRequestDTO;
import com.example.production_practice.enums.CuisineType;
import com.example.production_practice.enums.Gender;
import com.example.production_practice.service.RestaurantService;
import com.example.production_practice.service.ReviewService;
import com.example.production_practice.service.VisitorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer {
    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @PostConstruct
    public void init() {
        // Добавляем посетителей
        visitorService.save(new VisitorRequestDTO("Aleksey", 18, Gender.MALE));
        visitorService.save(new VisitorRequestDTO(null, 20, Gender.UNKNOWN));
        visitorService.save(new VisitorRequestDTO("Ksenya", 31, Gender.FEMALE));

        // Добавляем рестораны
        restaurantService.save(new RestaurantRequestDTO(
                "Claude Monet",
                "Ресторан авторской французской кухни",
                CuisineType.FRENCH.name(),
                "4000"
        ));
        restaurantService.save(new RestaurantRequestDTO(
                "Wok'n'Roll",
                null,
                CuisineType.JAPANESE.name(),
                "1450"
        ));

        // Добавляем отзывы через сервис, чтобы рейтинг пересчиталcя
        reviewService.save(new ReviewRequestDTO(1L, 1L, 3, "От бананов не отклеены этикетки"));
        reviewService.save(new ReviewRequestDTO(2L, 1L, 2, "В салат положили кожаный ремень"));
        reviewService.save(new ReviewRequestDTO(3L, 1L, 5, null));
        reviewService.save(new ReviewRequestDTO(1L, 2L, 1, "Судя по чистоте заведения едят тут кишечными палочками"));
        reviewService.save(new ReviewRequestDTO(2L, 2L, 4, null));
    }
}
