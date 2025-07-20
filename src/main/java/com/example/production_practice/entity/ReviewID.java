package com.example.production_practice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class ReviewID implements Serializable {

    @Column(name = "visitor_id")
    private Long visitorId;

    @Column(name = "restaurant_id")
    private Long restaurantId;
}
