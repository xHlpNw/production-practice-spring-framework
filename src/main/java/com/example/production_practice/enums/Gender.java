package com.example.production_practice.enums;

public enum Gender {
    MALE ("мужской"),
    FEMALE ("женский"),
    UNKNOWN ("не указано");

    private final String title;

    Gender(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
