package com.smartinvent.service;

import com.smartinvent.models.Category;
import com.smartinvent.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CategoryService клас для роботи з Category об'єктами.
 */
@Service
public class CategoryService {

    /**
     * CategoryRepository об'єкт для роботи з Category об'єктами.
     *
     * @see com.smartinvent.repositories.CategoryRepository
     */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Метод для створення нового Category об'єкта.
     *
     * @param category Category об'єкт
     * @return створений Category об'єкт
     */
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Метод для оновлення Category об'єкта за його id.
     *
     * @param id       id Category об'єкта
     * @param category Category об'єкт
     * @return оновлений Category об'єкт
     */
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        existingCategory.setProductType(category.getProductType());
        return categoryRepository.save(existingCategory);
    }

    /**
     * Метод для видалення Category об'єкта за його id.
     *
     * @param id id Category об'єкта
     */
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    /**
     * Метод для отримання всіх Category об'єктів.
     *
     * @return список всіх Category об'єктів
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Метод для отримання Category об'єкта за його id.
     *
     * @param id id Category об'єкта
     * @return Category об'єкт
     */
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }
}
