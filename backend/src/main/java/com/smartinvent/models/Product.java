package com.smartinvent.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    @Column(name = "product_work_id", nullable = false, unique = true, length = 100)
    private String productWorkId;

    private Integer count;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "storage_id", nullable = false)
    private Storage storage;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 255)
    private String manufacturer;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(length = 50)
    private String dimensions;

    public Product() {
    }

    // Конструктор без ID (наприклад, для створення)
    public Product(String name, String description, String productWorkId, Integer count, Category category, Storage storage,
                   BigDecimal price, String manufacturer, LocalDate expirationDate,
                   BigDecimal weight, String dimensions) {
        this.name = name;
        this.description = description;
        this.productWorkId = productWorkId;
        this.count = count;
        this.category = category;
        this.storage = storage;
        this.price = price;
        this.manufacturer = manufacturer;
        this.expirationDate = expirationDate;
        this.weight = weight;
        this.dimensions = dimensions;
    }

    // --- Геттери та Сеттери ---
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
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
