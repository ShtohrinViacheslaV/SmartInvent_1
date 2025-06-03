package com.smartinvent.model;


import java.time.LocalDateTime;

public class InventoryResult {

    private Long inventoryResultId;
    private InventorySession session;
    private Product product;
    private Employee scannedBy;
    private InventoryProductStatusEnum statusName;
    private LocalDateTime scanTime;
    private String description;

    public LocalDateTime getScanTime() {
        return scanTime;
    }

    public void setScanTime(LocalDateTime scanTime) {
        this.scanTime = scanTime;
    }

    public Long getInventoryResultId() {
        return inventoryResultId;
    }

    public void setInventoryResultId(Long inventoryResultId) {
        this.inventoryResultId = inventoryResultId;
    }

    public InventorySession getSession() {
        return session;
    }

    public void setSession(InventorySession session) {
        this.session = session;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Employee getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(Employee scannedBy) {
        this.scannedBy = scannedBy;
    }

    public InventoryProductStatusEnum getStatusName() {
        return statusName;
    }

    public void setStatusName(InventoryProductStatusEnum statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
