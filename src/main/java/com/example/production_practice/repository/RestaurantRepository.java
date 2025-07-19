package com.example.production_practice.repository;

import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.entity.Visitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r WHERE r.rating >= :minRating")
    Page<Restaurant> findByRating(BigDecimal minRating, Pageable pageable);

    Page<Restaurant> findByRatingGreaterThan(BigDecimal minRating, Pageable pageable);
}
