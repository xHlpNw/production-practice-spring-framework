package com.example.production_practice.mapper;

import com.example.production_practice.dto.RestaurantRequestDTO;
import com.example.production_practice.dto.RestaurantResponseDTO;
import com.example.production_practice.entity.Restaurant;
import com.example.production_practice.enums.CuisineType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {CuisineType.class})
public interface RestaurantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cuisineType", expression = "java(mapCuisineType(dto.getCuisineType()))")
    @Mapping(target = "averageCheck", expression = "java(new BigDecimal(dto.getAverageCheck()))")
    @Mapping(target = "rating", constant = "0") // начальный рейтинг
    Restaurant toEntity(RestaurantRequestDTO dto);

    RestaurantResponseDTO toResponseDTO(Restaurant entity);

    default CuisineType mapCuisineType(String type) {
        return CuisineType.valueOf(type.toUpperCase());
    }
}
