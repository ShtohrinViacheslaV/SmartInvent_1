package com.smartinvent.model;

import com.google.gson.annotations.SerializedName;

public enum InventorySessionStatusEnum {
    @SerializedName("ACTIVE")
    ACTIVE("Інвентаризація активна"),
    @SerializedName("COMPLETED")
    COMPLETED("Інвентаризація завершена"),
    @SerializedName("CANCELLED")
    CANCELLED("Інвентаризація скасована"),
    @SerializedName("PLANNED")
    PLANNED("Інвентаризація запланована");

    private final String description;

    InventorySessionStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
