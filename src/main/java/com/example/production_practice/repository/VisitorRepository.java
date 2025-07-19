package com.example.production_practice.repository;

//В каждом классе хранить данные в приватном финальном поле типа List, все методы работают именно с этим полем.
//●	Класс для работы с данными о посетителях, методы: save, remove, findAll.То есть класс, который сохраняет
//нового посетителя в наш список, удаляет оттуда его и находит все записи, остальные классы аналогично.

import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> { }
