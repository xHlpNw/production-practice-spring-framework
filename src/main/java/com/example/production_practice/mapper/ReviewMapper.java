package com.example.production_practice.mapper;

import com.example.production_practice.dto.ReviewRequestDTO;
import com.example.production_practice.dto.ReviewResponseDTO;
import com.example.production_practice.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "visitorId", source = "visitorId")
    @Mapping(target = "restaurantId", source = "restaurantId")
    Review toEntity(ReviewRequestDTO dto, Long visitorId, Long restaurantId);

    ReviewResponseDTO toResponseDTO(Review entity);
}
