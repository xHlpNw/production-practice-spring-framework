package com.example.production_practice;

import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.enums.CuisineType;
import com.example.production_practice.enums.Gender;
import com.example.production_practice.mapper.RestaurantMapper;
import com.example.production_practice.mapper.ReviewMapper;
import com.example.production_practice.mapper.VisitorMapper;
import com.example.production_practice.repository.RestaurantRepository;
import com.example.production_practice.repository.ReviewRepository;
import com.example.production_practice.repository.VisitorRepository;
import com.example.production_practice.service.ReviewService;
import com.example.production_practice.service.RestaurantService;
import com.example.production_practice.service.VisitorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class Initializer {
    private final VisitorRepository visitorRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    @PostConstruct
    public void init() {
        visitorRepository.save(new Visitor(1L, "Aleksey", 18, Gender.MALE));
        visitorRepository.save(new Visitor(2L, null,20, Gender.UNKNOWN));
        visitorRepository.save(new Visitor(3L, "Ksenya", 31, Gender.FEMALE));


        restaurantRepository.save(new Restaurant(
                1L,
                "Claude Monet",
                "Ресторан авторской французской кухни",
                CuisineType.FRENCH,
                BigDecimal.valueOf(4000),
                BigDecimal.ZERO
        ));
        restaurantRepository.save(new Restaurant(
                2L,
                "Wok'n'Roll",
                null,
                CuisineType.JAPANESE,
                BigDecimal.valueOf(1450),
                BigDecimal.ZERO
        ));

        reviewRepository.save(new Review(1L, 1L, 3, "От бананов не отклеены этикетки"));
        reviewRepository.save(new Review(2L, 1L, 2, "В салат положили кожаный ремень"));
        reviewRepository.save(new Review(3L, 1L, 5, null));
        reviewRepository.save(new Review(
                1L, 2L, 1,
                "Судя по чистоте заведения едят тут кишечными палочками"
        ));
        reviewRepository.save(new Review(2L, 2L, 4, null));
    }

}
