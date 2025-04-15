package com.smartinvent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smartinvent.models.Product;
import com.smartinvent.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Received request to fetch all products");
        List<Product> products = productService.getAllProducts();
        logger.debug("Found {} products", products.size());
        return ResponseEntity.ok(products);
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        logger.info("Attempting to create a new product");
        logger.debug("Product data: {}", product);
        Product createdProduct = productService.createProduct(product);
        logger.info("Product created successfully with id={}", createdProduct.getProductWorkId());
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Attempting to update product with id={}", id);
        Product updatedProduct = productService.updateProduct(id, product);
        logger.info("Product updated successfully with id={}", updatedProduct.getProductWorkId());
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Attempting to delete product with id={}", id);
        productService.deleteProduct(id);
        logger.info("Product deleted successfully with id={}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
        logger.info("Searching for products with query: {}", query);
        List<Product> products = productService.searchProducts(query);
        logger.debug("Found {} products matching query '{}'", products.size(), query);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("Fetching product by id={}", id);
        Product product = productService.getProductById(id);
        logger.debug("Product found: {}", product);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/checkQrCode")
    public ResponseEntity<Boolean> isQrCodeUnique(@RequestParam String qrCode) {
        logger.info("Checking uniqueness of QR code: {}", qrCode);
        boolean isUnique = productService.isQrCodeUnique(qrCode);
        logger.debug("QR code '{}' is unique: {}", qrCode, isUnique);
        return ResponseEntity.ok(isUnique);
    }


}
