package com.smartinvent.models;


public enum InventorySessionStatusEnum {
    ACTIVE("Інвентаризація активна"),
    COMPLETED("Інвентаризація завершена"),
    CANCELLED("Інвентаризація скасована"),
    PLANNED("Інвентаризація запланована");

    private final String description;

    InventorySessionStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

