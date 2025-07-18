package com.example.production_practice.repository;

//В каждом классе хранить данные в приватном финальном поле типа List, все методы работают именно с этим полем.
//●	Класс для работы с данными о посетителях, методы: save, remove, findAll.То есть класс, который сохраняет
//нового посетителя в наш список, удаляет оттуда его и находит все записи, остальные классы аналогично.

import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.Visitor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitorRepository {
    private final List<Visitor> visitors = new ArrayList<>();

    public void save(Visitor visitor) {
        visitors.add(visitor);
    }

    public void remove(Visitor visitor) {
        visitors.remove(visitor);
    }

    public List<Visitor> findAll() {
        return List.copyOf(visitors);
    }

    public Visitor findById(Long id) {
        return visitors.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst().orElse(null);
    }
}
