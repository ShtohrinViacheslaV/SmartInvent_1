package com.smartinvent.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGlobalExceptionHandler() throws Exception {
        mockMvc.perform(get("/api/invalid-endpoint"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorId").exists())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}
