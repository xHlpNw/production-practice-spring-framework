package com.example.production_practice.service;

//Слой бизнес логики (сервисы). В каждый класс внедрить соответствующий(-ие) репозиторий(-ии).
//  ●	Класс для работы с данными о ресторанах, методы: save, remove, findAll

import com.example.production_practice.dto.RestaurantRequestDTO;
import com.example.production_practice.dto.RestaurantResponseDTO;
import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.entity.Visitor;
import com.example.production_practice.mapper.RestaurantMapper;
import com.example.production_practice.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public void save(RestaurantRequestDTO restaurantDTO) {
        restaurantRepository.save(restaurantMapper.toEntity(restaurantDTO));
    }

    public void remove(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        restaurant.getReviews().clear();
        restaurantRepository.deleteById(id);
    }

    public List<RestaurantResponseDTO> findAll() {
        return restaurantRepository.findAll()
                .stream().map(restaurantMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RestaurantResponseDTO findById(Long id) {
        return restaurantMapper.toResponseDTO(restaurantRepository.findById(id).orElse(null));
    }

    public void update(Long id, RestaurantRequestDTO restaurantDTO) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        restaurant.setName(restaurantDTO.getName());
        restaurant.setDescription(restaurantDTO.getDescription());
        restaurant.setAverageCheck(new BigDecimal(restaurantDTO.getAverageCheck()));
        restaurant.setCuisineType(restaurantMapper.mapCuisineType(restaurantDTO.getCuisineType()));
        restaurantRepository.save(restaurant);
    }

    public Page<RestaurantResponseDTO> findByRating(BigDecimal rating, Pageable page) {
        return restaurantRepository.findByRating(rating, page).map(restaurantMapper::toResponseDTO);
    }
}
