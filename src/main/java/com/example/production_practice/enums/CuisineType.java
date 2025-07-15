package com.example.production_practice.enums;

public enum CuisineType {
    EUROPEAN ("европейская"),
    ITALIAN ("итальянская"),
    CHINESE ("китайская"),
    AMERICAN ("американская"),
    RUSSIAN ("русская"),
    FRENCH ("французская"),
    JAPANESE ("японская"),
    MEXICAN ("мексиканская");

    private final String title;
    CuisineType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
