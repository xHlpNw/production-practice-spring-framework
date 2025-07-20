package com.example.production_practice.entity;

import com.example.production_practice.enums.CuisineType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    @NotBlank
    private String name;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private CuisineType cuisineType;

    @Column(nullable = false)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal averageCheck;

    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private BigDecimal rating;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
