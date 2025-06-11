package com.smartinvent.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Представляє товар, який зберігається на складі у системі SmartInvent.
 * Містить інформацію про назву, опис, унікальний робочий ID, категорію,
 * склад, ціну, виробника, термін придатності, вагу та габарити.
 */
@Entity
@Table(name = "product")
public class Product {

    /** Унікальний ідентифікатор товару */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    /** Назва товару */
    @Column(nullable = false, length = 100)
    private String name;

    /** Опис товару */
    private String description;

    /** Унікальний робочий ID товару, який використовується для ідентифікації товару в системі */
    @Column(name = "product_work_id", nullable = false, unique = true, length = 100)
    private String productWorkId;

    /** Категорія, до якої належить товар */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /** Склад, на якому зберігається товар */
    @ManyToOne
    @JoinColumn(name = "storage_id", nullable = false)
    private Storage storage;

    /** Ціна товару */
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    /** Виробник товару */
    @Column(length = 255)
    private String manufacturer;

    /** Дата закінчення терміну придатності товару */
    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    /** Вага товару */
    @Column(precision = 10, scale = 2)
    private BigDecimal weight;

    /** Габарити товару (наприклад, розміри упаковки) */
    @Column(length = 50)
    private String dimensions;




    private Integer count;

    // --- Конструктори ---
    /**
     * Конструктор без параметрів, необхідний для JPA.
     */
    public Product() {
    }

    /**
     * Конструктор для створення нового товару з усіма необхідними полями.
     *
     * @param name Назва товару
     * @param description Опис товару
     * @param productWorkId Унікальний робочий ID товару
     * @param count Кількість товару на складі
     * @param category Категорія товару
     * @param storage Склад, на якому зберігається товар
     * @param price Ціна товару
     * @param manufacturer Виробник товару
     * @param expirationDate Дата закінчення терміну придатності товару
     * @param weight Вага товару
     * @param dimensions Габарити товару
     */
    public Product(String name, String description, String productWorkId,
                   Integer count, Category category, Storage storage,
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

    // --- Геттери та сеттери ---
    /* * Отримує унікальний ідентифікатор товару.
     *
     * @return Унікальний ідентифікатор товару
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Встановлює унікальний ідентифікатор товару.
     *
     * @param productId Унікальний ідентифікатор товару
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * Отримує назву товару.
     *
     * @return Назва товару
     */
    public String getName() {
        return name;
    }

    /**
     * Встановлює назву товару.
     *
     * @param name Назва товару
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Отримує опис товару.
     *
     * @return Опис товару
     */
    public String getDescription() {
        return description;
    }

    /**
     * Встановлює опис товару.
     *
     * @param description Опис товару
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Отримує унікальний робочий ID товару.
     *
     * @return Унікальний робочий ID товару
     */
    public String getProductWorkId() {
        return productWorkId;
    }

    /**
     * Встановлює унікальний робочий ID товару.
     *
     * @param productWorkId Унікальний робочий ID товару
     */
    public void setProductWorkId(String productWorkId) {
        this.productWorkId = productWorkId;
    }

    /**
     * Отримує кількість товару на складі.
     *
     * @return Кількість товару
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Встановлює кількість товару на складі.
     *
     * @param count Кількість товару
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * Отримує категорію товару.
     *
     * @return Категорія товару
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Встановлює категорію товару.
     *
     * @param category Категорія товару
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Отримує склад, на якому зберігається товар.
     *
     * @return Склад товару
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * Встановлює склад, на якому зберігається товар.
     *
     * @param storage Склад товару
     */
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    /**
     * Отримує ціну товару.
     *
     * @return Ціна товару
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Встановлює ціну товару.
     *
     * @param price Ціна товару
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Отримує виробника товару.
     *
     * @return Виробник товару
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Встановлює виробника товару.
     *
     * @param manufacturer Виробник товару
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Отримує дату закінчення терміну придатності товару.
     *
     * @return Дата закінчення терміну придатності
     */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Встановлює дату закінчення терміну придатності товару.
     *
     * @param expirationDate Дата закінчення терміну придатності
     */
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Отримує вагу товару.
     *
     * @return Вага товару
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * Встановлює вагу товару.
     *
     * @param weight Вага товару
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * Отримує габарити товару.
     *
     * @return Габарити товару
     */
    public String getDimensions() {
        return dimensions;
    }

    /**
     * Встановлює габарити товару.
     *
     * @param dimensions Габарити товару
     */
    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }
}
