package com.smartinvent.backend.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SchemaValidationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testTableExists() {
        final String tableName = "product"; // Перевіряємо таблицю Product
        final Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?",
                new Object[]{tableName},
                Integer.class
        );
        assertNotNull(count);
        assertTrue(count > 0, "❌ Таблиця " + tableName + " не створена!");
        System.out.println("✅ Таблиця " + tableName + " існує!");
    }
}
