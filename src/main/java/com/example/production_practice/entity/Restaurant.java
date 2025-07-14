package com.example.production_practice.entity;

//  Класс для ресторана с полями id (типа Long), название, описание (может быть пустым),
//  тип кухни (сделать перечисление с типами “Европейская”, “Итальянская”, “Китайская”, и т.д.),
//  средний чек на человека, оценка пользователей (типа BigDecimal).


import com.example.production_practice.enums.CuisineType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Restaurant {
    @NonNull
    private Long id;

    @NonNull
    private String name;

    private String description;

    @NonNull
    private CuisineType cuisineType;

    @NonNull
    private BigDecimal averageCheck;

    @NonNull
    private BigDecimal rating;
}
