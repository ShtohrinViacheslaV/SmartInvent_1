package com.smartinvent.models;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventorySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventorySessionId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "inventory_session_status_id", nullable = false)
    private InventorySessionStatus inventorySessionStatus;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

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

    public InventorySessionStatus getInventorySessionStatus() {
        return inventorySessionStatus;
    }

    public void setInventorySessionStatus(InventorySessionStatus inventorySessionStatus) {
        this.inventorySessionStatus = inventorySessionStatus;
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
