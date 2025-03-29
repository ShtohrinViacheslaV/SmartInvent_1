package com.smartinvent.controller;

import com.smartinvent.models.Category;
import com.smartinvent.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Клас-контроллер для обробки запитів, пов'язаних з категоріями
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    /**
     * Об'єкт сервісу для роботи з категоріями
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * Метод для отримання всіх категорій
     *
     * @return - список категорій
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Метод для створення нової категорії
     *
     * @param category - об'єкт категорії
     * @return - створена категорія
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(createdCategory);
    }

    /**
     * Метод для оновлення інформації про категорію
     *
     * @param id
     * @param category
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * Метод для видалення категорії
     *
     * @param id - ідентифікатор категорії
     * @return - відповідь про результат видалення категорії
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
