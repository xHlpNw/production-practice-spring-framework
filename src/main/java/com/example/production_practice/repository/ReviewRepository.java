package com.example.production_practice.repository;

import com.example.production_practice.entity.Review;
import com.example.production_practice.entity.ReviewID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewID> {
    @Query("SELECT AVG(r.score) FROM Review r WHERE r.restaurant.id = :restaurantId")
    BigDecimal findAverageScoreByRestaurantId(Long restaurantId);

    List<Review> findAllByIdRestaurantId(Long restaurantId);
    Page<Review> findAllByRestaurantId(Long restaurantId, Pageable pageable);
}
