package com.example.production_practice.service;

//Слой бизнес логики (сервисы). В каждый класс внедрить соответствующий(-ие) репозиторий(-ии).
//  ●	Класс для работы с данными о посетителях, методы: save, remove, findAll

import com.example.production_practice.dto.VisitorRequestDTO;
import com.example.production_practice.dto.VisitorResponseDTO;
import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.mapper.VisitorMapper;
import com.example.production_practice.repository.RestaurantRepository;
import com.example.production_practice.repository.ReviewRepository;
import com.example.production_practice.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final VisitorMapper visitorMapper;

    public void save(VisitorRequestDTO visitorDTO) {
        visitorRepository.save(visitorMapper.toEntity(visitorDTO));
    }

    public void remove(Long id) {
        Visitor visitor = visitorRepository.findById(id).orElseThrow();
        List<Long> restaurantIdList = visitor.getReviews().stream()
                .map(r -> r.getId().getRestaurantId()).toList();
        visitor.getReviews().clear();
        visitorRepository.deleteById(id);
        List<Restaurant> restaurants = restaurantRepository.findAllById(restaurantIdList);
        for (Restaurant restaurant : restaurants) {
            recalculateRestaurantRating(restaurant);
        }
    }

    public List<VisitorResponseDTO> findAll() {
        return visitorRepository.findAll().stream()
                .map(visitorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VisitorResponseDTO findById(Long id) {
        return visitorMapper.toResponseDTO(visitorRepository.findById(id).orElse(null));
    }

    public void update(Long id, VisitorRequestDTO visitorDTO) {
        Visitor visitor = visitorRepository.findById(id).orElseThrow();
        visitor.setAge(visitorDTO.getAge());
        visitor.setName(visitorDTO.getName());
        visitor.setGender(visitorDTO.getGender());
        visitorRepository.save(visitor);
    }

    private void recalculateRestaurantRating(Restaurant restaurant){
        BigDecimal score = BigDecimal.ZERO;
        if (!restaurant.getReviews().isEmpty()) {
            score = restaurant.getReviews().stream()
                    .map(r -> BigDecimal.valueOf(r.getScore()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(restaurant.getReviews().size()), 1, RoundingMode.HALF_UP);
        }
        restaurant.setRating(score);
        restaurantRepository.save(restaurant);
    }
}
