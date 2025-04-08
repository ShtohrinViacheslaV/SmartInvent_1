package com.smartinvent.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartinvent.controller.ProductController;
import com.smartinvent.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.smartinvent.service.ProductService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

    @WebMvcTest(ProductController.class)
    public class ProductServiceRestDocsTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        public void setUp(RestDocumentationContextProvider restDocumentation) {
            ProductService mockedProductService = Mockito.mock(ProductService.class);
            ProductController controller = new ProductController(mockedProductService);

            this.mockMvc = MockMvcBuilders
                    .standaloneSetup(controller)
                    .apply(documentationConfiguration(restDocumentation))
                    .build();
        }

        @Test
        public void shouldDocumentCreateProduct() throws Exception {
            Product product = new Product();
            product.setProductId(1L);
            product.setName("Test Product");

            mockMvc.perform(RestDocumentationRequestBuilders.post("/api/products")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(product)))
                    .andExpect(status().isOk())
                    .andDo(document("create-product"));
        }
    }


