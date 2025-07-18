package com.example.production_practice.entity;

//  Класс для посетителей ресторана с полями id (типа Long), имя, возраст, пол.
//  Имя должно быть не обязательным (отзыв о ресторане можно оставить анонимно), всё остальное - обязательно.

import com.example.production_practice.enums.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Visitor {
    @NotNull
    private Long id;

    private String name;

    @NotNull
    @Min(0)
    private Integer age;

    @NotNull
    private Gender gender;
}
