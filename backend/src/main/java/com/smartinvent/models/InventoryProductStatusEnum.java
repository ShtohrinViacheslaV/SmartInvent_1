package com.smartinvent.models;


public enum InventoryProductStatusEnum {
    CONFIRMED("Підтверджений товар"),
    MODIFIED("Змінений товар"),
    NOT_FOUND("Не знайдений в списку товарів"),
    UNCHECKED("Не перевірений товар"),
    ADDED("Новий товар");

    private final String description;

    InventoryProductStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
