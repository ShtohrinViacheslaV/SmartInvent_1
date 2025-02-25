package com.smartinvent.service;

import com.smartinvent.models.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class DatabaseService {
    private final DataSource dataSource;

    @Autowired
    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean testConnection(DatabaseConfig config) {
        try (Connection conn = dataSource.getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Ініціалізація бази
    public void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Company (id SERIAL PRIMARY KEY, name VARCHAR(255), email VARCHAR(255), password TEXT)";
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при створенні таблиць", e);
        }
    }
}
