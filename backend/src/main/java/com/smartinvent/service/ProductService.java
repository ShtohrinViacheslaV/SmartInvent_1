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

    @Autowired
    private ProductRepository productRepository;
    private final QRCodeService qrCodeService;

    public ProductService(QRCodeService qrCodeService) {
        this.productRepository = productRepository;
        this.qrCodeService = qrCodeService;
    }



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
}
