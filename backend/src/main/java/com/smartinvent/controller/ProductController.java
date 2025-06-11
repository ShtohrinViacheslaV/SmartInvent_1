package com.smartinvent.controller;

import com.smartinvent.models.Product;
import com.smartinvent.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контролер для роботи з товарами в системі SmartInvent.
 * Виконує CRUD операції над товарами та пошук товарів за назвою.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    /**
     * Сервіс для роботи з товарами.
     * Використовується для виконання CRUD операцій над товарами.
     */
    @Autowired
    private ProductService productService;

    /**
     * Отримує список усіх товарів у системі.
     * @return ResponseEntity зі списком товарів
     */
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Створює новий товар у системі.
     * @param product товар, який потрібно створити
     * @return ResponseEntity зі створеним товаром
     */
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    /**
     * Оновлює існуючий товар у системі.
     * @param id унікальний ідентифікатор товару, який потрібно оновити
     * @param product нові дані для товару
     * @return ResponseEntity з оновленим товаром або 404 Not Found, якщо товар не знайдено
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Видаляє товар з системи за його унікальним ідентифікатором.
     * @param id унікальний ідентифікатор товару, який потрібно видалити
     * @return ResponseEntity з кодом 204 No Content, якщо видалення успішне
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Пошук товарів за назвою.
     * @param query частина назви товару для пошуку
     * @return ResponseEntity зі списком товарів, що відповідають критерію пошуку
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
        List<Product> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

    /**
     * Отримує товар за його унікальним ідентифікатором.
     * @param id унікальний ідентифікатор товару
     * @return ResponseEntity з товаром або 404 Not Found, якщо товар не знайдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    /**
     * Отримує товар за його унікальним робочим ID.
     * @param productWorkId унікальний робочий ID товару
     * @return ResponseEntity з товаром або 404 Not Found, якщо товар не знайдено
     */
    @GetMapping("byWorkId/{productWorkId}")
    public ResponseEntity<Product> getProductByWorkId(@PathVariable String productWorkId) {
        Product product = productService.getProductByProductWorkId(productWorkId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

}
