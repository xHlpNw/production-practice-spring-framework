package com.example.production_practice.repository;

//В каждом классе хранить данные в приватном финальном поле типа List, все методы работают именно с этим полем.
//●	Класс для работы с данными об оценках, методы: save, remove, findAll, findById

import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.ReviewID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewID> {

    List<Review> findAllByIdRestaurantId(Long restaurantId);
    Page<Review> findAllByRestaurantId(Long restaurantId, Pageable pageable);
}
