package com.smartinvent.controller;


import com.smartinvent.models.DatabaseConfig;
import com.smartinvent.service.DatabaseInitializationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

/**
 * Клас-контроллер для обробки запитів, пов'язаних з базою даних
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class DatabaseController {

    /**
     * Об'єкт сервісу для ініціалізації бази даних
     */
    private final DatabaseInitializationService databaseService;

    /**
     * Об'єкт джерела даних
     */
    private final DataSource dataSource;

    /**
     * Конструктор класу
     *
     * @param dataSource      - джерело даних
     * @param databaseService - сервіс для ініціалізації бази даних
     */
    @Autowired
    public DatabaseController(DataSource dataSource, DatabaseInitializationService databaseService) {
        this.dataSource = dataSource;
        this.databaseService = databaseService;
    }

    /**
     * Метод для перевірки підключення до бази даних
     *
     * @param config - конфігурація бази даних
     * @return - відповідь про результат перевірки підключення
     */
    @PostMapping("/testConnection")
    public ResponseEntity<String> testDbConnection(@RequestBody DatabaseConfig config) {
        System.out.println("DatabaseController testDbConnection ");

        log.info("🔍 Перевіряємо підключення: {}", config);
        boolean success = databaseService.testConnection(config);
        return success ? ResponseEntity.ok("✅ Підключення успішне!") : ResponseEntity.badRequest().body("❌ Помилка підключення!");
    }

    /**
     * Метод для ініціалізації бази даних
     *
     * @param config - конфігурація бази даних
     * @return - відповідь про результат ініціалізації бази даних
     */
    @PostMapping("/setupDatabase")
    public ResponseEntity<String> setupDatabase(@RequestBody DatabaseConfig config) {
        System.out.println("DatabaseController setupDatabase ");
        databaseService.initializeDatabase(config);
        return ResponseEntity.ok("✅ База даних перевірена та ініціалізована!");
    }

    /**
     * Метод для перевірки наявності таблиць в базі даних
     *
     * @param config - конфігурація бази даних
     * @return - відповідь про результат перевірки наявності таблиць
     */
    @PostMapping("/checkTables")
    public ResponseEntity<Boolean> checkTables(@RequestBody DatabaseConfig config) {
        boolean tablesExist = databaseService.checkTables(config);
        return ResponseEntity.ok(tablesExist);
    }
}


//
//        if (dataSource instanceof HikariDataSource hikariDataSource) {
//            if (hikariDataSource.isClosed()) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("Помилка: DataSource закритий!");
//            }
//        }
//
//        try (Connection connection = dataSource.getConnection()) {
//            if (connection.isValid(2)) {
//                return ResponseEntity.ok("Підключення успішне!");
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("Помилка підключення до БД!");
//            }
//        } catch (SQLException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Помилка SQL: " + e.getMessage());
//        }
//    }


//    @PostMapping("/clearDatabase")
//    public ResponseEntity<String> clearDatabase(@RequestBody DatabaseConfig config) {
//        System.out.println("DatabaseController clearDatabase ");
//
//
//        databaseService.clearDatabase();
//        return ResponseEntity.ok("✅ Таблиці очищені!");
//    }


//

//
//
//package com.smartinvent.controller;
//
//import com.smartinvent.models.DatabaseConfig;
//import com.smartinvent.service.DatabaseService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class DatabaseController {
//
//    private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);
//
//    private final DatabaseService databaseService;
//
//    @Autowired
//    public DatabaseController(DatabaseService databaseService) {
//        this.databaseService = databaseService;
//    }
//
//    @PostMapping("/testConnection")
//    public ResponseEntity<?> testDbConnection(@RequestBody DatabaseConfig config) {
//        logger.info("Отримано запит testConnection з конфігурацією: {}", config);
//        return ResponseEntity.ok("Connection successful!");
//    }
//
//    @PostMapping("/initializeDatabase")
//    public ResponseEntity<?> initializeDatabase() {
//        try {
//            databaseService.initializeDatabase();
//            return ResponseEntity.ok("Database initialized successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Database initialization failed: " + e.getMessage());
//        }
//    }
//}

