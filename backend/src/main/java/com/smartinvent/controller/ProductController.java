package com.smartinvent.controller;

import com.smartinvent.models.Product;
import com.smartinvent.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Клас-контроллер для обробки запитів, пов'язаних з продуктами
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /**
     * Об'єкт сервісу для роботи з продуктами
     */
    @Autowired
    private ProductService productService;

    /**
     * Метод для отримання всіх продуктів
     *
     * @return - список продуктів
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Метод для створення нового продукту
     *
     * @param product - об'єкт продукту
     * @return - створений продукт
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    /**
     * Метод для оновлення інформації про продукт
     *
     * @param id      - ідентифікатор продукту
     * @param product - об'єкт продукту
     * @return - оновлений продукт
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Метод для видалення продукту
     *
     * @param id - ідентифікатор продукту
     * @return - видалений продукт
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
