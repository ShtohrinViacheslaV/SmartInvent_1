package com.smartinvent.service;

import com.smartinvent.config.DynamicDataSourceConfig;
import com.smartinvent.models.DatabaseConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import javax.sql.DataSource;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
//@RequiredArgsConstructor
public class DatabaseInitializationService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;


    public DatabaseInitializationService(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    private static final List<String> TABLE_NAMES = List.of(
            "company", "employee", "category", "product",
            "storage", "transactions", "backup", "printout", "action_log"
    );


    public boolean checkIfTableExists(String tableName, DatabaseConfig config) {
        try {
            DynamicDataSourceConfig.setDataSource(
                    config.getUrl(),
                    config.getHost(),
                    config.getPort(),
                    config.getDatabase(),
                    config.getUsername(),
                    config.getPassword()
            );
            log.info("📌 Параметри підключення - host: {}, port: {}, database: {}", config.getHost(), config.getPort(), config.getDatabase());

            DataSource tempDataSource = DynamicDataSourceConfig.getDataSource();
            if (tempDataSource == null) {
                log.error("❌ Не вдалося створити DataSource для перевірки таблиць!");
                return false;
            }

            try {
                String dbProductName = jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName();

                String sql;
                if (dbProductName.equalsIgnoreCase("PostgreSQL")) {
                    sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
                } else if (dbProductName.equalsIgnoreCase("SQLite")) {
                    sql = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name = ?";
                } else {
                    throw new UnsupportedOperationException("Непідтримувана база даних: " + dbProductName);
                }

                Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
                return count != null && count > 0;
            } catch (Exception e) {
                log.error("❌ Помилка перевірки таблиці {} у базі: {}", tableName, e.getMessage());
                return false;
            }

        }
        catch (Exception e) {
            log.error("❌ Помилка перевірки таблиці '{}': {}", tableName, e.getMessage());
        }
        return false;
    }

//            try (Connection conn = tempDataSource.getConnection()) {
//                DatabaseMetaData metaData = conn.getMetaData();
//                String dbType = metaData.getDatabaseProductName().toLowerCase();
//
//                log.info("🔍 Перевіряємо таблицю '{}' в базі: {}", tableName, dbType);
//
//                if (dbType.contains("postgresql")) {
//                    String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
//                    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
//                    return count != null && count > 0;
//                } else if (dbType.contains("sqlite")) {
//                    try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, new String[]{"TABLE"})) {
//                        return rs.next();
//                    }
//                } else {
//                    log.warn("⚠️ Невідомий тип бази даних: {}", dbType);
//                }
//            }
//        } catch (SQLException e) {
//            log.error("❌ Помилка перевірки таблиці '{}': {}", tableName, e.getMessage());
//        }
//        return false;
//    }


    /**
     * Перевіряє, чи всі необхідні таблиці існують у базі даних.
     * Якщо хоча б однієї таблиці немає — повертає false.
     */
    public boolean checkTables(DatabaseConfig config) {
        try {
            for (String table : TABLE_NAMES) {
                if (!checkIfTableExists(table, config)) {
                    log.warn("⚠ Таблиця '{}' не знайдена в базі {}!", table, config.getUrl());
                    return false;
                }
            }
            log.info("✅ Всі необхідні таблиці існують у базі {}", config.getUrl());
            return true;
        } catch (Exception e) {
            log.error("❌ Помилка перевірки таблиць у базі {}", config.getUrl(), e);
            return false;
        }
    }


    public void clearDatabase() {
        try {
            // Виконати SQL-операції для видалення даних з таблиць
            String query = "DELETE FROM ?";  // використовуємо параметр для назви таблиці

            for (String table : TABLE_NAMES) {
                try {
                    // Очищаємо кожну таблицю
                    jdbcTemplate.execute("DELETE FROM " + table);
                    log.info("✅ Дані з таблиці '{}' очищені.", table);
                } catch (Exception e) {
                    log.error("❌ Помилка очищення таблиці '{}'", table, e);
                }
            }

            log.info("✅ База даних очищена!");
        } catch (Exception e) {
            log.error("❌ Помилка при очищенні бази даних", e);
        }
    }



    public boolean testConnection(DatabaseConfig config) {
        try {
            DynamicDataSourceConfig.setDataSource(
                    config.getUrl(),  // URL, якщо є
                    config.getHost(), // або передаємо окремі параметри
                    config.getPort(),
                    config.getDatabase(),
                    config.getUsername(),
                    config.getPassword()
            );
            log.info("📌 Параметри підключення 2 - host: {}, port: {}, database: {}", config.getHost(), config.getPort(), config.getDatabase());

            DataSource dataSource = DynamicDataSourceConfig.getDataSource();
            if (dataSource == null) {
                log.error("❌ Підключення не вдалося!");
                return false;
            }
            log.info("✅ Підключення успішне!");
            return true;
        } catch (Exception e) {
            log.error("❌ Помилка підключення", e);
            return false;
        }
    }

    /**
     * Ініціалізує базу даних, якщо необхідні таблиці відсутні.
     */
    public void initializeDatabase(DatabaseConfig config) {
        if (checkTables(config)) {
            log.info("✅ Всі необхідні таблиці вже існують.");
            return;
        }

        log.info("⏳ Виконуємо SQL-скрипт для створення таблиць...");
        executeSqlScript("sql/create_table.sql");

        if (checkTables(config)) {
            log.info("✅ Таблиці успішно створені!");
        } else {
            log.error("❌ Помилка: таблиці не створені!");
        }
    }




    /**
     * Виконує SQL-скрипт, що знаходиться у classpath.
     */
    private void executeSqlScript(String scriptPath) {
        try {
            Resource resource = new ClassPathResource(scriptPath);
            if (!resource.exists()) {
                log.error("❌ SQL-скрипт {} не знайдено!", scriptPath);
                return;
            }

            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String[] sqlStatements = sql.split(";");

            for (String statement : sqlStatements) {
                if (!statement.trim().isEmpty()) {
                    jdbcTemplate.execute(statement.trim());
                }
            }

            log.info("✅ Таблиці успішно створені!");
        } catch (IOException e) {
            log.error("❌ Помилка читання SQL-скрипта: {}", scriptPath, e);
        } catch (Exception e) {
            log.error("❌ Помилка виконання SQL-скрипта", e);
        }
    }
}

//    public boolean checkTables() {
//        try {
//            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
//
//            for (String table : TABLE_NAMES) {
//                try {
//                    Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
//                    if (count == null || count == 0) {
//                        log.warn("⚠ Таблиця '{}' не знайдена!", table);
//                        return false;
//                    }
//                } catch (Exception e) {
//                    log.error("❌ Помилка перевірки таблиці '{}'", table, e);
//                    return false;
//                }
//            }
//            log.info("✅ Всі необхідні таблиці існують.");
//            return true;
//        } catch (Exception e) {
//            log.error("❌ Помилка перевірки таблиць у БД", e);
//            return false;
//        }
//    }



//package com.smartinvent.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class DatabaseInitializationService {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    private static final List<String> TABLE_NAMES = List.of(
//            "company", "employee", "category", "product",
//            "storage", "transaction", "backup", "printout", "action_log"
//    );
//
//
//    public boolean isDatabaseInitialized() {
//        String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
//
//        for (String table : TABLE_NAMES) {
//            Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
//            if (count == null || count == 0) {
//                log.warn("⚠ Таблиця '{}' не знайдена!", table);
//                return false;
//            }
//        }
//        return true;
//    }
////    public boolean isDatabaseInitialized() {
////        for (String table : TABLE_NAMES) {
////            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
////            Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
////            if (count == null || count == 0) {
////                log.warn("⚠ Таблиця '{}' не знайдена!", table);
////                return false;
////            }
////        }
////        return true;
////    }
//
//    public boolean checkTables() {
//        try {
//            for (String table : TABLE_NAMES) {
//                String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
//                Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
//                if (count == null || count == 0) {
//                    log.warn("⚠ Таблиця '{}' не знайдена!", table);
//                    return false;
//                }
//            }
//            log.info("✅ Всі необхідні таблиці існують.");
//            return true;
//        } catch (Exception e) {
//            log.error("❌ Помилка перевірки таблиць в БД", e);
//            return false;
//        }
//    }
//
//    public void initializeDatabase() {
//
//        if (isDatabaseInitialized()) {
//            log.info("✅ Всі необхідні таблиці вже існують.");
//            return ;
//        }
//
//        log.info("⏳ Виконуємо SQL-скрипт для створення таблиць...");
//        executeSqlScript("sql/create_table.sql");
//
//        if (isDatabaseInitialized()) {
//            log.info("✅ Таблиці успішно створені!");
//        } else {
//            log.error("❌ Помилка: таблиці не створені!");
//        }
//    }
//
//
//    private void executeSqlScript(String scriptPath) {
//        try {
//            Resource resource = new ClassPathResource(scriptPath);
//            if (!resource.exists()) {
//                log.error("❌ SQL-скрипт {} не знайдено!", scriptPath);
//                return;
//            }
//
//            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
//            String[] sqlStatements = sql.split(";");
//
//            for (String statement : sqlStatements) {
//                if (!statement.trim().isEmpty()) {
//                    jdbcTemplate.execute(statement.trim());
//                }
//            }
//
//            log.info("✅ Таблиці успішно створені!");
//        } catch (IOException e) {
//            log.error("❌ Помилка читання SQL-скрипта: {}", scriptPath, e);
//        } catch (Exception e) {
//            log.error("❌ Помилка виконання SQL-скрипта", e);
//        }
//    }
////    private void executeSqlScript(String scriptPath) {
////        try {
////            Resource resource = new ClassPathResource(scriptPath);
////            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
////            log.info("📜 Виконуємо SQL:\n{}", sql);
////            jdbcTemplate.execute(sql);
////            log.info("✅ Таблиці успішно створені!");
////        } catch (IOException e) {
////            log.error("❌ Помилка читання SQL-скрипта", e);
////        } catch (Exception e) {
////            log.error("❌ Помилка виконання SQL-скрипта", e);
////        }
////    }
//}
//
//
//
////package com.smartinvent.service;
////
////import lombok.RequiredArgsConstructor;
////import lombok.extern.slf4j.Slf4j;
////import org.springframework.jdbc.core.JdbcTemplate;
////import org.springframework.stereotype.Service;
////import java.io.IOException;
////import java.nio.file.Files;
////import java.nio.file.Paths;
////import java.util.List;
////
////@Slf4j
////@Service
////@RequiredArgsConstructor
////public class DatabaseInitializationService {
////
////    private final JdbcTemplate jdbcTemplate;
////
////    private static final List<String> TABLE_NAMES = List.of(
////            "company", "employee", "category", "product",
////            "storage", "transaction", "backup", "printout", "action_log"
////    );
////
////    public boolean isDatabaseInitialized() {
////        for (String table : TABLE_NAMES) {
////            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
////            Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
////            if (count == null || count == 0) {
////                log.warn("Таблиця '{}' не знайдена!", table);
////                return false;
////            }
////        }
////
////        return true;
////    }
////
////    public boolean checkTables() {
////        try {
////            for (String table : TABLE_NAMES) {
////                String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
////                Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
////                if (count == null || count == 0) {
////                    log.warn("⚠ Таблиця '{}' не знайдена!", table);
////                    return false;
////                }
////            }
////            log.info("✅ Всі необхідні таблиці існують.");
////            return true;
////        } catch (Exception e) {
////            log.error("❌ Помилка перевірки таблиць в БД", e);
////            return false;
////        }
////    }
////
////
////    public void initializeDatabase() {
////        if (isDatabaseInitialized()) {
////            log.info("✅ Всі необхідні таблиці вже існують.");
////            return;
////        }
////
////        log.info("⏳ Виконуємо SQL-скрипт для створення таблиць...");
////        executeSqlScript("sql/create_table.sql");
////    }
////
////    private void executeSqlScript(String scriptPath) {
////        try {
////            String sql = new String(Files.readAllBytes(Paths.get(scriptPath)));
////            jdbcTemplate.execute(sql);
////            log.info("✅ Таблиці успішно створені!");
////        } catch (IOException e) {
////            log.error("❌ Помилка читання SQL-скрипта", e);
////        } catch (Exception e) {
////            log.error("❌ Помилка виконання SQL-скрипта", e);
////        }
////    }
////}
////
////
////
//////package com.smartinvent.service;
//////
//////import lombok.extern.slf4j.Slf4j;
//////import org.springframework.stereotype.Service;
//////
//////import javax.annotation.PostConstruct;
//////import javax.sql.DataSource;
//////import java.io.IOException;
//////import java.nio.file.Files;
//////import java.nio.file.Paths;
//////import java.sql.*;
//////
//////@Slf4j
//////@Service
//////public class DatabaseInitializationService {
//////
//////    private final DataSource dataSource;
//////
//////    public DatabaseInitializationService(DataSource dataSource) {
//////        this.dataSource = dataSource;
//////    }
//////
//////    @PostConstruct
//////    public void initializeDatabase() {
//////        log.info("🔄 Перевірка та ініціалізація бази даних...");
//////
//////        if (!isDatabaseInitialized()) {
//////            log.info("⏳ База даних порожня. Виконуємо SQL-скрипт для створення таблиць...");
//////            executeSqlScript("sql/create_table.sql");
//////        } else {
//////            log.info("✅ Всі необхідні таблиці вже існують.");
//////        }
//////    }
//////
//////    private boolean isDatabaseInitialized() {
//////        String checkQuery = "SELECT count(*) FROM information_schema.tables WHERE table_name = 'company'";
//////
//////        try (Connection conn = dataSource.getConnection();
//////             PreparedStatement stmt = conn.prepareStatement(checkQuery);
//////             ResultSet rs = stmt.executeQuery()) {
//////            if (rs.next()) {
//////                return rs.getInt(1) > 0; // Якщо таблиця існує, повертаємо true
//////            }
//////        } catch (SQLException e) {
//////            log.error("Помилка при перевірці бази даних", e);
//////        }
//////        return false;
//////    }
//////
//////    private void executeSqlScript(String scriptPath) {
//////        try (Connection conn = dataSource.getConnection();
//////             Statement stmt = conn.createStatement()) {
//////
//////            String sql = new String(Files.readAllBytes(Paths.get("backend/src/main/resources/" + scriptPath)));
//////            stmt.execute(sql);
//////            log.info("✅ Таблиці успішно створені!");
//////
//////        } catch (SQLException | IOException e) {
//////            log.error("❌ Помилка виконання SQL-скрипта", e);
//////        }
//////    }
//////}
