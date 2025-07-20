package com.example.production_practice.service;

import com.example.production_practice.dto.RestaurantRequestDTO;
import com.example.production_practice.dto.RestaurantResponseDTO;
import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.mapper.RestaurantMapper;
import com.example.production_practice.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public RestaurantResponseDTO save(RestaurantRequestDTO restaurantDTO) {
        Restaurant saved = restaurantRepository.save(restaurantMapper.toEntity(restaurantDTO));
        return restaurantMapper.toResponseDTO(saved);
    }

    @Transactional
    public void remove(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ресторан не найден");
        }
        restaurantRepository.deleteById(id);
    }

    public List<RestaurantResponseDTO> findAll() {
        return restaurantRepository.findAll()
                .stream().map(restaurantMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RestaurantResponseDTO findById(Long id) {
        return restaurantMapper.toResponseDTO(restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ресторан не найден")));
    }

    @Transactional
    public void update(Long id, RestaurantRequestDTO restaurantDTO) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ресторан не найден"));
        restaurant.setName(restaurantDTO.getName());
        restaurant.setDescription(restaurantDTO.getDescription());
        restaurant.setAverageCheck(restaurantDTO.getAverageCheck());
        restaurant.setCuisineType(restaurantDTO.getCuisineType());
        restaurantRepository.save(restaurant);
    }

    public Page<RestaurantResponseDTO> findByRating(BigDecimal rating, Pageable page) {
        return restaurantRepository.findByRating(rating, page).map(restaurantMapper::toResponseDTO);
    }
}
