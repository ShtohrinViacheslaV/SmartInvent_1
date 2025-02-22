package com.smartinvent.backend.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDatabaseConnection() {
        assertNotNull(dataSource);
        try (Connection conn = dataSource.getConnection()) {
            assertFalse(conn.isClosed());
            System.out.println("✅ Підключення до БД успішне: " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            fail("❌ Не вдалося підключитися до БД: " + e.getMessage());
        }
    }
}
