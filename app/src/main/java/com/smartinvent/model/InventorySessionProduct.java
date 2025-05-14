package com.smartinvent.model;


import java.math.BigDecimal;
import java.time.LocalDate;

public class InventorySessionProduct {
    private Long productId;
    private String name;
    private String description;
    private String productWorkId;
    private Integer count;
    private String categoryName;
    private String storageName;
    private String status; // confirmed, modified, not_found, added, unchecked
    private boolean locked;

    private BigDecimal price;
    private String manufacturer;
    private LocalDate expirationDate;
    private BigDecimal weight;
    private String dimensions;
    // Конструктор
    public InventorySessionProduct() {}

    public InventorySessionProduct(Long productId, String name, String description,
                                   String productWorkId, Integer count, String categoryName, String storageName,
                                   String status, boolean locked, BigDecimal price, String manufacturer,
                                   LocalDate expirationDate, BigDecimal weight, String dimensions) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.productWorkId = productWorkId;
        this.count = count;
        this.categoryName = categoryName;
        this.storageName = storageName;
        this.status = status;
        this.locked = locked;
        this.price = price;
        this.manufacturer = manufacturer;
        this.expirationDate = expirationDate;
        this.weight = weight;
        this.dimensions = dimensions;
    }

    // Геттери та сеттери
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getProductWorkId() {
        return productWorkId;
    }

    public void setProductWorkId(String productWorkId) {
        this.productWorkId = productWorkId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }
}
