package com.smartinvent.dto;

import com.smartinvent.models.InventoryProductStatusEnum;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class InventoryProductResultDto {

    private Long inventoryResultId;
    private Long inventorySessionId;

    private Long productId;
    private String productName;
    private String productDescription;
    private String productWorkId;
    private Integer productCount;
    private String categoryName;
    private String storageName;
    private BigDecimal price;
    private Integer count;
    private String manufacturer;
    private LocalDate expirationDate;
    private BigDecimal weight;
    private String dimensions;

    private InventoryProductStatusEnum status;
    private Long scannedBy;
    private String description;
    private LocalDateTime scanTime;

    public InventoryProductResultDto() {
    }

    public InventoryProductResultDto(Long inventoryResultId, Long inventorySessionId,
                                     Long productId, String productName, String productDescription,
                                     String productWorkId, Integer productCount, String categoryName,
                                     String storageName, BigDecimal price, Integer count, String manufacturer,
                                     LocalDate expirationDate, BigDecimal weight, String dimensions,
                                     InventoryProductStatusEnum status, Long scannedBy, String description, LocalDateTime scanTime) {
        this.inventoryResultId = inventoryResultId;
        this.inventorySessionId = inventorySessionId;
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productWorkId = productWorkId;
        this.productCount = productCount;
        this.categoryName = categoryName;
        this.storageName = storageName;
        this.price = price;
        this.count = count;
        this.manufacturer = manufacturer;
        this.expirationDate = expirationDate;
        this.weight = weight;
        this.dimensions = dimensions;
        this.status = status;
        this.scannedBy = scannedBy;
        this.description = description;
        this.scanTime = scanTime;
    }

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductWorkId() {
        return productWorkId;
    }

    public void setProductWorkId(String productWorkId) {
        this.productWorkId = productWorkId;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public InventoryProductStatusEnum getStatus() {
        return status;
    }

    public void setStatus(InventoryProductStatusEnum status) {
        this.status = status;
    }

    public Long getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(Long scannedBy) {
        this.scannedBy = scannedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getScanTime() {
        return scanTime;
    }

    public void setScanTime(LocalDateTime scanTime) {
        this.scanTime = scanTime;
    }
}