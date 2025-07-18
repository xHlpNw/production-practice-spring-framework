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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    Long COUNT_RESTAURANT = 0L;

    public void save(RestaurantRequestDTO restaurantDTO) {
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant.setId(++COUNT_RESTAURANT);
        restaurantRepository.save(restaurant);
    }

    public void remove(Long id) {
        Restaurant restaurant = restaurantRepository.findAll()
                .stream().filter(r -> r.getId() == id).findFirst().orElse(null);
        if (restaurant != null) restaurantRepository.remove(restaurant);
    }

    public List<RestaurantResponseDTO> findAll() {
        return restaurantRepository.findAll()
                .stream().map(restaurantMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RestaurantResponseDTO findById(Long id) {
        return restaurantMapper.toResponseDTO(restaurantRepository.findById(id));
    }

    public void update(Long id, RestaurantRequestDTO restaurantDTO) {
        Restaurant oldRestaurant = restaurantRepository.findById(id);
        oldRestaurant.setName(restaurantDTO.getName());
        oldRestaurant.setDescription(restaurantDTO.getDescription());
        oldRestaurant.setAverageCheck(new BigDecimal(restaurantDTO.getAverageCheck()));
        oldRestaurant.setCuisineType(restaurantMapper.mapCuisineType(restaurantDTO.getCuisineType()));
    }
}
