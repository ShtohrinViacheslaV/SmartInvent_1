package com.smartinvent.service;

import com.google.zxing.WriterException;
import com.smartinvent.models.Product;
import com.smartinvent.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * ProductService клас для роботи з Product об'єктами.
 * Описує операції, такі як створення, оновлення, видалення і отримання продуктів з бази даних.
 */
@Service
public class ProductService {

    /**
     * QRCodeService об'єкт для роботи з QR-кодами продуктів.
     *
     * @see com.smartinvent.service.QRCodeService
     */
    private final QRCodeService qrCodeService;

    /**
     * ProductRepository об'єкт для роботи з Product об'єктами.
     *
     * @see com.smartinvent.repositories.ProductRepository
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Конструктор класу ProductService.
     *
     * @param qrCodeService QRCodeService об'єкт для роботи з QR-кодами продуктів
     */
    public ProductService(QRCodeService qrCodeService) {
        this.productRepository = productRepository;
        this.qrCodeService = qrCodeService;
    }

    /**
     * Метод createProduct для створення нового Product об'єкта.
     *
     * @param product Product об'єкт
     * @return створений Product об'єкт
     */
    public Product createProduct(Product product) {
        try {
            // Генерація QR-коду
            byte[] qrCodeImage = qrCodeService.generateQrCodeImage(product.getProductId());
            product.setQrCode(qrCodeImage);

            // Збереження продукту
            return productRepository.save(product);
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generating QR code for product: " + product.getName(), e);
        }
    }
//    public Product createProduct(Product product) {
//        return productRepository.save(product);
//    }

    /**
     * Метод updateProduct для оновлення Product об'єкта за його id.
     *
     * @param id      id Product об'єкта
     * @param product Product об'єкт
     * @return оновлений Product об'єкт
     */
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setProductWorkId(product.getProductWorkId());
        existingProduct.setCount(product.getCount());
        existingProduct.setQrCode(product.getQrCode());
        existingProduct.setCategory(product.getCategory());
        return productRepository.save(existingProduct);
    }

    /**
     * Метод deleteProduct для видалення Product об'єкта за його id.
     *
     * @param id id Product об'єкта
     */
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    /**
     * Метод getAllProducts для отримання всіх Product об'єктів.
     *
     * @return список всіх Product об'єктів
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Метод getProductById для отримання Product об'єкта за його id.
     *
     * @param id id Product об'єкта
     * @return Product об'єкт
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
}
