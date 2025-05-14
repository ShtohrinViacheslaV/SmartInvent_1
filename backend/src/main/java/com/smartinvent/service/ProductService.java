package com.smartinvent.service;

import com.google.zxing.WriterException;
import com.smartinvent.models.Product;
import com.smartinvent.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product createProduct(Product product) {
        if (productRepository.existsByProductWorkId(product.getProductWorkId())) {
            throw new RuntimeException("ProductWorkId already exists: " + product.getProductWorkId());
        }
        return productRepository.save(product);
    }


    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

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

        return productRepository.save(existingProduct);
    }


    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }


}
