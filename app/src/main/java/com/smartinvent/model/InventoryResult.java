package com.smartinvent.model;


public class InventoryResult {

    private Long inventoryResultId;
    private Long inventorySessionId;
    private Long product;
    private Long scannedBy;
    private String statusName;
    private String description;

    public Long getInventoryResultId() {
        return inventoryResultId;
    }

    public void setInventoryResultId(Long inventoryResultId) {
        this.inventoryResultId = inventoryResultId;
    }

    public Long getInventorySessionId() {
        return inventorySessionId;
    }

    public void setInventorySessionId(Long inventorySessionId) {
        this.inventorySessionId = inventorySessionId;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public Long getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(Long scannedBy) {
        this.scannedBy = scannedBy;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
