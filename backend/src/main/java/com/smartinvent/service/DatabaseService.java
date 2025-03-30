//package com.smartinvent.service;
//
//import com.smartinvent.models.DatabaseConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//
//@Slf4j
//@Service
//public class DatabaseService {
//
//    private final DataSource dataSource;
//
//    public DatabaseService(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    public boolean initializeDatabase() {
//        log.info("⏳ Починаємо створення таблиць...");
//
//        try (Connection connection = dataSource.getConnection();
//             Statement statement = connection.createStatement()) {
//
//            // Зчитуємо SQL-скрипт
//            String sql = new String(Files.readAllBytes(Paths.get("src/main/resources/sql/create_table.sql")));
//
//            // Виконуємо SQL
//            statement.execute(sql);
//
//            log.info("✅ Всі таблиці успішно створені!");
//            return true;
//
//        } catch (SQLException | IOException e) {
//            log.error("❌ Помилка під час створення таблиць", e);
//            return false;
//        }
//    }
//}
//
