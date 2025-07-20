package com.example.production_practice.mapper;

import com.example.production_practice.dto.RestaurantRequestDTO;
import com.example.production_practice.dto.RestaurantResponseDTO;
import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.enums.CuisineType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", imports = {CuisineType.class, BigDecimal.class})
public interface RestaurantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "averageCheck", source = "averageCheck")
    @Mapping(target = "rating", constant = "0")
    @Mapping(target = "reviews", ignore = true)
    Restaurant toEntity(RestaurantRequestDTO dto);

    RestaurantResponseDTO toResponseDTO(Restaurant entity);

}
