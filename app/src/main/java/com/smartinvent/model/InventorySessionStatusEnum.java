package com.smartinvent.model;

public enum InventorySessionStatusEnum {
    ACTIVE("Інвентаризація активна"),
    COMPLETED("Інвентаризація завершена"),
    CANCELLED("Інвентаризація скасована");

    private final String description;

    InventorySessionStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
