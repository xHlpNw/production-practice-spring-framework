package com.example.production_practice.entity;

import com.example.production_practice.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visitor")
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer age;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    @OneToMany(mappedBy = "visitor", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
