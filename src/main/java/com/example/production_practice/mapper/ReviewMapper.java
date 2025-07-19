package com.example.production_practice.mapper;

import com.example.production_practice.dto.ReviewRequestDTO;
import com.example.production_practice.dto.ReviewResponseDTO;
import com.example.production_practice.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "id", expression = "java(new ReviewID(dto.getVisitorId(), dto.getRestaurantId()))")
    @Mapping(target = "visitor", ignore = true)  // или можешь настроить, если нужно
    @Mapping(target = "restaurant", ignore = true)
    Review toEntity(ReviewRequestDTO dto);

    @Mapping(source = "id.visitorId", target = "visitorId")
    @Mapping(source = "id.restaurantId", target = "restaurantId")
    ReviewResponseDTO toResponseDTO(Review entity);
}
