package com.smartinvent.service;

import com.smartinvent.models.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseService клас для роботи з базою даних.
 */
@Service
public class DatabaseService {

    /**
     * DataSource об'єкт для роботи з базою даних.
     */
    private final DataSource dataSource;

    /**
     * Конструктор класу DatabaseService.
     *
     * @param dataSource DataSource об'єкт
     */
    @Autowired
    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Метод testConnection для перевірки з'єднання з базою даних.
     *
     * @param config DatabaseConfig об'єкт
     * @return true, якщо з'єднання успішне, інакше - false
     */
    public boolean testConnection(DatabaseConfig config) {
        try (Connection conn = dataSource.getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Метод initializeDatabase для створення таблиць в базі даних.
     */
    public void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Company (id SERIAL PRIMARY KEY, name VARCHAR(255), email VARCHAR(255), password TEXT)";
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при створенні таблиць", e);
        }
    }
}
