package com.example.production_practice.service;

//Слой бизнес логики (сервисы). В каждый класс внедрить соответствующий(-ие) репозиторий(-ии).
//  ●	Класс для работы с данными о посетителях, методы: save, remove, findAll

import com.example.production_practice.entity.Visitor;
import com.example.production_practice.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;

    public void save(Visitor visitor) {
        visitorRepository.save(visitor);
    }

    public void remove(Visitor visitor) {
        visitorRepository.remove(visitor);
    }

    public List<Visitor> findAll() {
        return visitorRepository.findAll();
    }
}
