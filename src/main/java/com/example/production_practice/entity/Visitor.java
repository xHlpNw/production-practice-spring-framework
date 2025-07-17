package com.example.production_practice.entity;

//  Класс для посетителей ресторана с полями id (типа Long), имя, возраст, пол.
//  Имя должно быть не обязательным (отзыв о ресторане можно оставить анонимно), всё остальное - обязательно.

import com.example.production_practice.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Visitor {
    @NonNull
    private Long id;

    private String name;

    @NonNull
    private Integer age;

    @NonNull
    private Gender gender;
}
