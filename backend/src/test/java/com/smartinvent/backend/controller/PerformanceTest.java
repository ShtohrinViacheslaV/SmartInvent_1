package com.smartinvent.backend.controller;

import com.smartinvent.models.Product;
import com.smartinvent.service.ProductService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PerformanceTest {

    private static final Logger log = LoggerFactory.getLogger(PerformanceTest.class);

    @Autowired
    private ProductService productService;

    @Test
    public void testReadProductsPerformance() {
        long startTime = System.currentTimeMillis();

        List<Product> productList = productService.getAllProducts();

        long endTime = System.currentTimeMillis();
        log.debug("PERF_TEST: Час читання товарів: {} мс", (endTime - startTime));

        assertNotNull(productList);
    }

    @Test
    public void testInsertManyProductsPerformance() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            Product product = new Product(
                    null,
                    "Товар " + i,
                    "Опис товару " + i,
                    "productWorkId" + i,
                    10,
                    "qrCode" + i,
                    null,
                    null
            );
            productService.createProduct(product);
        }

        long endTime = System.currentTimeMillis();
        log.debug("PERF_TEST: Час вставки 1000 товарів: {} мс", (endTime - startTime));
    }

    @Test
    public void testUpdateManyProductsPerformance() {
        List<Product> productList = productService.getAllProducts();
        int toIndex = Math.min(100, productList.size()); // Безпечний upper bound
        List<Product> subList = productList.subList(0, toIndex);

        long startTime = System.currentTimeMillis();

        for (Product p : productList) {
            p.setCount(p.getCount() + 1);
            productService.updateProduct(p.getProductId(), p);
        }

        long endTime = System.currentTimeMillis();
        log.debug("PERF_TEST: Час оновлення 100 товарів: {} мс", (endTime - startTime));
    }
}
