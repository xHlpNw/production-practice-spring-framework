package com.example.production_practice.entity;

//  Класс для ресторана с полями id (типа Long), название, описание (может быть пустым),
//  тип кухни (сделать перечисление с типами “Европейская”, “Итальянская”, “Китайская”, и т.д.),
//  средний чек на человека, оценка пользователей (типа BigDecimal).


import com.example.production_practice.enums.CuisineType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String name;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;

    @Column(nullable = false)
    private BigDecimal averageCheck;

    private BigDecimal rating;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
