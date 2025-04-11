//package com.smartinvent.backend.json;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.smartinvent.models.Product;
//import com.smartinvent.repositories.ProductRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringRunner;
//import java.io.File;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class JsonImportTest {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Test
//    public void testImportJsonFile() throws Exception {
//        // Припустимо, у нас є JSON-файл product.json
//        File file = new File("src/test/resources/product.json");
//        Product product = objectMapper.readValue(file, Product.class);
//
//        productRepository.save(product);
//        Optional<Product> found = productRepository.findById(product.getProductId());
//
//        assertTrue(found.isPresent(), "❌ Не вдалося імпортувати продукт!");
//        System.out.println("✅ JSON-файл імпортований у базу!");
//    }
//}
