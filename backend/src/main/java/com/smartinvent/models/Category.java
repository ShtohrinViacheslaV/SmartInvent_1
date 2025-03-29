package com.smartinvent.models;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Клас-сутність для зберігання інформації про категорії товарів
 */
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    private String description;

    private String productType;  // Додаткове поле для типу товару


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * equals - метод для порівняння двох об'єктів
     *
     * @param o - об'єкт, з яким порівнюється поточний об'єкт
     * @return - результат порівняння
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryId, category.categoryId);
    }

    /**
     * hashCode - метод для отримання хеш-коду об'єкта
     *
     * @return - хеш-код об'єкта
     */
    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }
}