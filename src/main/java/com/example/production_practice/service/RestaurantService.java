package com.example.production_practice.service;

//Слой бизнес логики (сервисы). В каждый класс внедрить соответствующий(-ие) репозиторий(-ии).
//  ●	Класс для работы с данными о ресторанах, методы: save, remove, findAll

import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public void save(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public void remove(Restaurant restaurant) {
        restaurantRepository.remove(restaurant);
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }
}
