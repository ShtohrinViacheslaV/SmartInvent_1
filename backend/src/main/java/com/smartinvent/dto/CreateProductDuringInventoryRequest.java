package com.smartinvent.dto;

public class CreateProductDuringInventoryRequest {
    private String name;
    private String description;
    private String productWorkId;
    private Long categoryId;
    private Long storageId;
    private Long inventorySessionId;

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

    public String getProductWorkId() {
        return productWorkId;
    }

    public void setProductWorkId(String productWorkId) {
        this.productWorkId = productWorkId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public Long getInventorySessionId() {
        return inventorySessionId;
    }

    public void setInventorySessionId(Long inventorySessionId) {
        this.inventorySessionId = inventorySessionId;
    }
}
