package com.example.production_practice.repository;

//В каждом классе хранить данные в приватном финальном поле типа List, все методы работают именно с этим полем.
//●	Класс для работы с данными о ресторанах, методы: save, remove, findAll

import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.entity.Visitor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants = new ArrayList<>();

    public void save(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public void remove(Restaurant restaurant) {
        restaurants.remove(restaurant);
    }

    public List<Restaurant> findAll() {
        return List.copyOf(restaurants);
    }

    public Restaurant findById(Long id) {
        return restaurants.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst().orElse(null);
    }
}
