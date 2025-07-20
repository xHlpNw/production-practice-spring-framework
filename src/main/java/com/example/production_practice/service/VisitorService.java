package com.example.production_practice.service;

import com.example.production_practice.dto.VisitorRequestDTO;
import com.example.production_practice.dto.VisitorResponseDTO;
import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.mapper.VisitorMapper;
import com.example.production_practice.repository.RestaurantRepository;
import com.example.production_practice.repository.ReviewRepository;
import com.example.production_practice.repository.VisitorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public VisitorResponseDTO save(VisitorRequestDTO visitorDTO) {
        if (visitorDTO == null) {
            throw new NullPointerException("VisitorRequestDTO is null");
        }
        Visitor visitor = visitorMapper.toEntity(visitorDTO);
        visitorRepository.save(visitor);
        return visitorMapper.toResponseDTO(visitor);
    }

    @Transactional
    public void remove(Long id) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Посетитель не найден"));
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
        return visitorMapper.toResponseDTO(visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Посетитель не найден")));
    }

    @Transactional
    public void update(Long id, VisitorRequestDTO visitorDTO) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Посетитель не найден"));
        visitor.setAge(visitorDTO.getAge());
        visitor.setName(visitorDTO.getName());
        visitor.setGender(visitorDTO.getGender());
        visitorRepository.save(visitor);
    }

    private void recalculateRestaurantRating(Restaurant restaurant) {
        BigDecimal averageScore = reviewRepository.findAverageScoreByRestaurantId(restaurant.getId());
        if (averageScore == null) {
            averageScore = BigDecimal.ZERO;
        } else {
            averageScore = averageScore.setScale(1, RoundingMode.HALF_UP);
        }
        restaurant.setRating(averageScore);
        restaurantRepository.save(restaurant);
    }
}
