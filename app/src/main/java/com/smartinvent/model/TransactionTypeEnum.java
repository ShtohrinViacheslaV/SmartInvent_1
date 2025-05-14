package com.smartinvent.model;

public enum TransactionTypeEnum {
    ARRIVAL("Надходження товару"),
    DEPARTURE("Вибуття товару"),
    UPDATE("Оновлення інформації про товар");

    private final String description;

    TransactionTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
