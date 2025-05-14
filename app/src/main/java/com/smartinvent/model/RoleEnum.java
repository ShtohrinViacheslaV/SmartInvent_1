package com.smartinvent.model;

public enum RoleEnum {
    ADMIN("Має всі права"),
    USER("Має обмежені права");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
