package com.smartinvent.model;


import java.time.LocalDateTime;

public class InventorySession {

    private Long inventorySessionId;
    private Employee employee;
    private String name;
    private String description;
    private InventorySessionStatusEnum status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public InventorySession(Long inventorySessionId) {
        this.inventorySessionId = inventorySessionId;
    }

    public InventorySession() {

    }

    public Long getInventorySessionId() {
        return inventorySessionId;
    }

    public void setInventorySessionId(Long inventorySessionId) {
        this.inventorySessionId = inventorySessionId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InventorySessionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(InventorySessionStatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
