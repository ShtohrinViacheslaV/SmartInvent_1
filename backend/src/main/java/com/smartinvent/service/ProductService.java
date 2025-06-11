package com.smartinvent.service;

import com.smartinvent.models.Product;
import com.smartinvent.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/* * Сервіс для роботи з товарами в системі SmartInvent.
 * Виконує CRUD операції над товарами, а також пошук товарів за назвою.
 */
@Service
public class ProductService {

    /** Репозиторій для доступу до даних про товари */
    private final ProductRepository productRepository;

    /**
     * Конструктор сервісу ProductService.
     * @param productRepository репозиторій для доступу до даних про товари
     */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Створює новий товар у системі.
     * @param product товар, який потрібно створити
     * @return створений товар
     * @throws RuntimeException якщо товар з таким унікальним робочим ID вже існує
     */
    public Product createProduct(Product product) {
        if (productRepository.existsByProductWorkId(product.getProductWorkId())) {
            throw new RuntimeException("ProductWorkId already exists: " + product.getProductWorkId());
        }
        return productRepository.save(product);
    }

    /**
     * Видаляє товар з системи за його унікальним ідентифікатором.
     * @param id унікальний ідентифікатор товару, який потрібно видалити
     * @throws RuntimeException якщо товар з таким ідентифікатором не знайдено
     */
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    /**
     * Отримує список усіх товарів у системі.
     * @return список усіх товарів
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Отримує товар за його унікальним ідентифікатором.
     * @param id унікальний ідентифікатор товару
     * @return товар з вказаним ідентифікатором
     * @throws RuntimeException якщо товар з таким ідентифікатором не знайдено
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * Отримує товар за його унікальним робочим ID.
     * @param productWorkId унікальний робочий ID товару
     * @return товар з вказаним робочим ID
     * @throws RuntimeException якщо товар з таким робочим ID не знайдено
     */
    public Product getProductByProductWorkId(String productWorkId) {
        return productRepository.findByProductWorkId(productWorkId)
                .orElseThrow(() -> new RuntimeException("Product not found with productWorkId: " + productWorkId));
    }

    /**
     * Оновлює інформацію про товар.
     * @param id унікальний ідентифікатор товару, який потрібно оновити
     * @param product нові дані товару
     * @return оновлений товар
     * @throws RuntimeException якщо товар з таким ідентифікатором не знайдено або якщо унікальний робочий ID вже існує
     */
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (!existingProduct.getProductWorkId().equals(product.getProductWorkId())) {
            if (productRepository.existsByProductWorkId(product.getProductWorkId())) {
                throw new RuntimeException("ProductWorkId already exists: " + product.getProductWorkId());
            }
        }

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setProductWorkId(product.getProductWorkId());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setStorage(product.getStorage());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setManufacturer(product.getManufacturer());
        existingProduct.setExpirationDate(product.getExpirationDate());
        existingProduct.setWeight(product.getWeight());
        existingProduct.setDimensions(product.getDimensions());
        existingProduct.setCount(product.getCount());

        return productRepository.save(existingProduct);
    }

    /**
     * Шукає товари за частиною назви.
     * @param query частина назви товару для пошуку
     * @return список товарів, що відповідають критерію пошуку
     */
    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

}
