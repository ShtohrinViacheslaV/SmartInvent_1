package com.smartinvent.backend.database;

import com.smartinvent.models.Product;
import com.smartinvent.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveAndFindProduct() {
        Product product = new Product();
        product.setName("Тестовий товар");
        productRepository.save(product);

        Optional<Product> found = productRepository.findById(product.getProductId());
        assertTrue(found.isPresent(), "❌ Товар не знайдено!");
        assertEquals("Тестовий товар", found.get().getName());
        System.out.println("✅ Товар успішно збережений і знайдений!");
    }
}
