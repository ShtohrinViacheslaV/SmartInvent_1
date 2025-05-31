package com.smartinvent.service;

import com.smartinvent.config.DynamicDataSourceConfig;
import com.smartinvent.models.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



@Service
@Slf4j
public class DatabaseInitializationService {

    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;
    private final DynamicDataSourceConfig dynamicDataSourceConfig;

    @Autowired
    public DatabaseInitializationService(JdbcTemplate jdbcTemplate, DataSource dataSource, DynamicDataSourceConfig dynamicDataSourceConfig) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.dynamicDataSourceConfig = dynamicDataSourceConfig;


    }

    public void updateDataSource(DataSource newDataSource) {
        log.info("🔄 Оновлення DataSource у DatabaseInitializationService...");
        this.dataSource = newDataSource;
        this.jdbcTemplate = new JdbcTemplate(newDataSource);
    }


    private static final List<String> TABLE_NAMES = List.of(
            "category", "company", "employee",  "inventory_result", "inventory_session",
            "storage", "transaction", "product"
    );


////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DataSource getDataSource(DatabaseConfig config) {
        System.out.println("DatabaseInitializationService getDataSource ");


        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        return dataSource;
    }


    public boolean testConnection(DatabaseConfig config) {
        System.out.println("DatabaseInitializationService testConnection ");
        try {
            dynamicDataSourceConfig.setDataSource(
                    config.getUrl(),  // URL, якщо є
                    config.getHost(), // або передаємо окремі параметри
                    config.getPort(),
                    config.getDatabase(),
                    config.getUsername(),
                    config.getPassword()
            );
            log.info("📌 Параметри підключення 2 - host: {}, port: {}, database: {}", config.getHost(), config.getPort(), config.getDatabase());

//            DataSource dataSource = DynamicDataSourceConfig.getDataSource();
            DataSource dataSource = dynamicDataSourceConfig.getDataSource();
            updateDataSource(dataSource); // Оновлюємо DataSource

            log.info("Using DataSource: {}", dataSource);

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

    public void initializeDatabase(DatabaseConfig config) {
        System.out.println("DatabaseInitializationService initializeDatabase ");

//        this.dataSource = getDataSource(config);
        this.dataSource = dynamicDataSourceConfig.getDataSource();

        if (!checkTables(config)) {
            log.info("⚠ Tables are missing, creating them now...");
            Path path = Paths.get("backend/src/main/resources/sql/create_table.sql");
            System.out.println("Checking file: " + path.toAbsolutePath());
            System.out.println("File exists: " + Files.exists(path));

            executeSqlScript("sql/create_table.sql");
            if (!checkTables(config)) {
                log.error("❌ Failed to create the necessary tables!");
            } else {
                log.info("✅ Tables created and verified successfully.");
            }
        } else {
            log.info("✅ All necessary tables already exist.");
        }
    }


    public boolean checkTables(DatabaseConfig config) {
        System.out.println("DatabaseInitializationService checkTables ");

        try {
            // Створення з'єднання один раз
            dynamicDataSourceConfig.setDataSource(
                    config.getUrl(),
                    config.getHost(),
                    config.getPort(),
                    config.getDatabase(),
                    config.getUsername(),
                    config.getPassword()
            );

            DataSource tempDataSource = dynamicDataSourceConfig.getDataSource();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(tempDataSource);

            for (String table : TABLE_NAMES) {
                if (!checkIfTableExists(table, jdbcTemplate)) {
                    log.info("🔍 Checking if table '{}' exists...", table);

                    log.warn("⚠ DatabaseInitializationService checkTables Таблиця '{}' не знайдена в базі {}!", table, config.getUrl());
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

    private boolean checkIfTableExists(String tableName, JdbcTemplate jdbcTemplate) {
        System.out.println("DatabaseInitializationService checkIfTableExists ");

        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
            String dbProductName = conn.getMetaData().getDatabaseProductName();
            log.info("🛠 dbProductName: {}", dbProductName);

            String sql;

            if (dbProductName.equalsIgnoreCase("PostgreSQL")) {
                sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
                log.info("🛠 Використовуємо SQL запит для перевірки таблиць в PostgreSQL");
            } else if (dbProductName.equalsIgnoreCase("SQLite")) {
                sql = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name = ?";
                log.info("🛠 Використовуємо SQL запит для перевірки таблиць в SQLite");
            } else {
                throw new UnsupportedOperationException("Непідтримувана база даних: " + dbProductName);
            }

            // Виконання запиту
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
            return count != null && count > 0;

        } catch (Exception e) {
            log.error("❌ Помилка перевірки таблиці {} у базі: {}", tableName, e.getMessage());
            return false;
        }
    }


    private void executeSqlScript(String scriptPath) {
        System.out.println("DatabaseInitializationService executeSqlScript ");


        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            System.out.println("Connected to: " + conn.getMetaData().getURL());
            String sql = new String(Files.readAllBytes(Paths.get("backend/src/main/resources/" + scriptPath)));
            stmt.execute(sql);
            log.info("✅ Tables created successfully!");


        } catch (SQLException | IOException e) {
            log.error("❌ Error executing SQL script", e);
        }
    }



    public void clearDatabase() {
        System.out.println("DatabaseInitializationService clearDatabase ");

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
}


//@Service
//@Slf4j
//public class DatabaseInitializationService {
//
//    private JdbcTemplate jdbcTemplate;
//    private DataSource dataSource;
//    private final DynamicDataSourceConfig dynamicDataSourceConfig;
//
//    @Autowired
//    public DatabaseInitializationService(DataSource dataSource, DynamicDataSourceConfig dynamicDataSourceConfig) {
//        this.dataSource = dataSource;
//        this.dynamicDataSourceConfig = dynamicDataSourceConfig;
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }
//
//    public void updateDataSource(DataSource newDataSource) {
//        log.info("🔄 Оновлення DataSource у DatabaseInitializationService...");
//        this.dataSource = newDataSource;
//        this.jdbcTemplate = new JdbcTemplate(newDataSource);
//        log.info("✅ DataSource успішно оновлено.");
//    }
//
//    public boolean testConnection(DatabaseConfig config) {
//        log.info("📌 Тестування підключення до бази даних: {}", config.getUrl());
//
//        if (dynamicDataSourceConfig.switchToPostgreSQL(config)) {
//            updateDataSource(dynamicDataSourceConfig.getActiveDataSource());
//            log.info("✅ Підключення до {} успішне!", config.getDatabase());
//            return true;
//        } else {
//            log.error("❌ Не вдалося підключитися до бази даних.");
//            return false;
//        }
//    }
//
//    private static final List<String> TABLE_NAMES = List.of(
//            "company", "employee", "category", "product",
//            "storage", "transactions", "backup", "printout", "action_log"
//    );
//
//    public void initializeDatabase(DatabaseConfig config) {
//        log.info("⚙ Ініціалізація бази даних: {}", config.getDatabase());
//        dynamicDataSourceConfig.ensureDataSourceIsActive(); // Перевіряємо, чи активний DataSource
//        this.dataSource = dynamicDataSourceConfig.getActiveDataSource();
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//
//        if (!checkTables()) {
//            log.warn("⚠ Таблиці відсутні, створюємо...");
//            executeSqlScript("sql/create_table.sql");
//            if (!checkTables()) {
//                log.error("❌ Неможливо створити таблиці!");
//            } else {
//                log.info("✅ Таблиці успішно створені.");
//            }
//        } else {
//            log.info("✅ Всі необхідні таблиці існують.");
//        }
//    }
//
//    public boolean checkTables() {
//        try {
//            for (String table : TABLE_NAMES) {
//                log.info("🔎 Перевірка таблиці: {}", table);
//                if (!checkIfTableExists(table)) {
//                    log.warn("❌ Таблиця '{}' не знайдена!", table);
//                    return false;
//                }
//            }
//            return true;
//        } catch (Exception e) {
//            log.error("❌ Помилка перевірки таблиць", e);
//            return false;
//        }
//    }
//
//    private boolean checkIfTableExists(String tableName) {
//        try (Connection conn = dataSource.getConnection()) {
//            String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
//            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
//            return count != null && count > 0;
//        } catch (Exception e) {
//            log.error("❌ Помилка перевірки таблиці '{}'", tableName, e);
//            return false;
//        }
//    }
//
//    private void executeSqlScript(String scriptPath) {
//        try (Connection conn = dataSource.getConnection();
//             Statement stmt = conn.createStatement()) {
//            String sql = new String(Files.readAllBytes(Paths.get("backend/src/main/resources/" + scriptPath)));
//            stmt.execute(sql);
//            log.info("✅ Таблиці створені!");
//        } catch (SQLException | IOException e) {
//            log.error("❌ Помилка виконання SQL-скрипта", e);
//        }
//    }
//}


//package com.smartinvent.service;
//
//import com.smartinvent.config.DynamicDataSourceConfig;
//import com.smartinvent.models.DatabaseConfig;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.stereotype.Service;
//
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//
//import java.io.IOException;
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.sql.*;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//
//@Service
//@Slf4j
//public class DatabaseInitializationService {
//
//    private JdbcTemplate jdbcTemplate;
//    private DataSource dataSource;
//    private DynamicDataSourceConfig dynamicDataSourceConfig;
//
////    @Autowired
////    public DatabaseInitializationService(JdbcTemplate jdbcTemplate, DataSource dataSource, DynamicDataSourceConfig dynamicDataSourceConfig) {
////        this.jdbcTemplate = jdbcTemplate;
////        this.dataSource = dataSource;
////        this.dynamicDataSourceConfig = dynamicDataSourceConfig;
////    }
//
//    @Autowired
//    public DatabaseInitializationService(DynamicDataSourceConfig dynamicDataSourceConfig, DataSource dataSource) {
//        this.dynamicDataSourceConfig = dynamicDataSourceConfig;
//        this.dataSource = dataSource;
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }
//
//
//    @Autowired
//    public void updateDataSource(DataSource newDataSource) {
//        log.info("🔄 Оновлення DataSource у DatabaseInitializationService...");
//
//        if (newDataSource == null) {
//            log.error("❌ Передано null у updateDataSource!");
//            return;
//        }
//
//        this.dataSource = newDataSource;
//        this.jdbcTemplate = new JdbcTemplate(newDataSource);
//    }
//
//
//
//    private static final List<String> TABLE_NAMES = List.of(
//            "company", "employee", "category", "product",
//            "storage", "transactions", "backup", "printout", "action_log"
//    );
//
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
////    private DataSource getDataSource(DatabaseConfig config) {
////        System.out.println("DatabaseInitializationService getDataSource ");
////
////
////        DriverManagerDataSource dataSource = new DriverManagerDataSource();
////        dataSource.setDriverClassName("org.postgresql.Driver");
////        dataSource.setUrl(config.getUrl());
////        dataSource.setUsername(config.getUsername());
////        dataSource.setPassword(config.getPassword());
////        return dataSource;
////    }
////
////
////    public boolean testConnection(DatabaseConfig config) {
////        System.out.println("DatabaseInitializationService testConnection ");
////        try {
////            dynamicDataSourceConfig.setDataSource(
////                    config.getUrl(),  // URL, якщо є
////                    config.getHost(), // або передаємо окремі параметри
////                    config.getPort(),
////                    config.getDatabase(),
////                    config.getUsername(),
////                    config.getPassword()
////            );
////            this.dataSource = dynamicDataSourceConfig.getDataSource();
////            this.jdbcTemplate = new JdbcTemplate(this.dataSource);
////
////            log.info("📌 Параметри підключення 2 - host: {}, port: {}, database: {}", config.getHost(), config.getPort(), config.getDatabase());
////
//////            DataSource dataSource = DynamicDataSourceConfig.getDataSource();
//////            DataSource dataSource = dynamicDataSourceConfig.getDataSource();
////            DataSource dataSource = this.dataSource;
////
////            updateDataSource(dataSource); // Оновлюємо DataSource
////
////            log.info("Using DataSource: {}", dataSource);
////
////            if (dataSource == null) {
////                log.error("❌ Підключення не вдалося!");
////                return false;
////            }
////            log.info("✅ Підключення успішне!");
////
////            return true;
////        } catch (Exception e) {
////            log.error("❌ Помилка підключення", e);
////            return false;
////        }
////    }
//    public boolean testConnection(DatabaseConfig config) {
//    try {
//        dynamicDataSourceConfig.setDataSource(
//                    config.getUrl(),  // URL, якщо є
//                    config.getHost(), // або передаємо окремі параметри
//                    config.getPort(),
//                    config.getDatabase(),
//                    config.getUsername(),
//                    config.getPassword()
//            );
//
//        // Отримуємо оновлений DataSource
//        this.dataSource = dynamicDataSourceConfig.getDataSource();
//        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
//
//        // Тестовий запит для перевірки з'єднання
//        try (Connection conn = this.dataSource.getConnection()) {
//            DatabaseMetaData metaData = conn.getMetaData();
//            log.info("✅ Успішне підключення до: {} - {}", metaData.getDatabaseProductName(), metaData.getURL());
//
//            // Виконуємо тестовий SQL-запит (наприклад, вибірка з системної таблиці)
//            String testQuery = "SELECT 1";
//            Integer result = jdbcTemplate.queryForObject(testQuery, Integer.class);
//
//            if (result != null && result == 1) {
//                log.info("✅ Тестовий запит виконано успішно");
//                return true;
//            } else {
//                log.warn("⚠️ Тестовий запит не повернув очікуваний результат");
//            }
//        }
//    } catch (SQLException e) {
//        log.error("❌ Помилка підключення: {}", e.getMessage(), e);
//    } catch (Exception e) {
//        log.error("❌ Невідома помилка при тестуванні підключення: {}", e.getMessage(), e);
//    }
//
//    log.warn("⚠️ Підключення до бази даних не вдалося");
//    return false;
//}
//
//
//    public void initializeDatabase(DatabaseConfig config) {
//        log.info("🔄 Ініціалізація бази даних...");
//
//        if (this.dataSource == null) {
//            dynamicDataSourceConfig.setDataSource(
//                    config.getUrl(),
//                    config.getHost(),
//                    config.getPort(),
//                    config.getDatabase(),
//                    config.getUsername(),
//                    config.getPassword()
//            );
//            this.dataSource = dynamicDataSourceConfig.getDataSource();
//            this.jdbcTemplate = new JdbcTemplate(this.dataSource);
//        }
//
//        if (!checkTables()) {
//            log.info("⚠ Таблиці відсутні, створюємо...");
//            executeSqlScript("sql/create_table.sql");
//
//            if (!checkTables()) {
//                log.error("❌ Неможливо створити необхідні таблиці!");
//            } else {
//                log.info("✅ Таблиці успішно створені.");
//            }
//        } else {
//            log.info("✅ Усі необхідні таблиці вже існують.");
//        }
//    }
//
//    public boolean checkTables() {
//        log.info("🔍 Перевірка існування таблиць...");
//
//        try (Connection conn = dataSource.getConnection()) {  ///////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//            for (String table : TABLE_NAMES) {
//                if (!checkIfTableExists(table)) {
//                    log.warn("⚠ Таблиця '{}' не знайдена!", table);
//                    return false;
//                }
//            }
//            log.info("✅ Всі необхідні таблиці існують.");
//            return true;
//        } catch (Exception e) {
//            log.error("❌ Помилка перевірки таблиць", e);
//            return false;
//        }
//    }
//
//    private boolean checkIfTableExists(String tableName) {
//        try (Connection conn = dataSource.getConnection()) {  // Використовуємо try-with-resources
//            String dbProductName = conn.getMetaData().getDatabaseProductName();
//            String sql;
//
//            if ("PostgreSQL".equalsIgnoreCase(dbProductName)) {
//                sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
//            } else if ("SQLite".equalsIgnoreCase(dbProductName)) {
//                sql = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name = ?";
//            } else {
//                throw new UnsupportedOperationException("Непідтримувана база даних: " + dbProductName);
//            }
//
//            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
//            return count != null && count > 0;
//
//        } catch (Exception e) {
//            log.error("❌ Помилка перевірки таблиці '{}': {}", tableName, e.getMessage());
//            return false;
//        }
//    }
//
//
//    private void executeSqlScript(String scriptPath) {
//        log.info("📜 Виконання SQL скрипту: {}", scriptPath);
//
//        try (Connection conn = dataSource.getConnection();
//             Statement stmt = conn.createStatement()) {
//            String sql = new String(Files.readAllBytes(Paths.get("backend/src/main/resources/" + scriptPath)));
//            stmt.execute(sql);
//            log.info("✅ Таблиці створено успішно!");
//        } catch (SQLException | IOException e) {
//            log.error("❌ Помилка виконання SQL скрипту", e);
//        }
//    }
//
//    public void clearDatabase() {
//        log.info("🗑 Очищення бази даних...");
//
//        try {
//            for (String table : TABLE_NAMES) {
//                jdbcTemplate.execute("DELETE FROM " + table);
//                log.info("✅ Дані з таблиці '{}' очищені.", table);
//            }
//            log.info("✅ База даних очищена!");
//        } catch (Exception e) {
//            log.error("❌ Помилка очищення бази даних", e);
//        }
//    }
//
//}
//
////    public void initializeDatabase(DatabaseConfig config) {
////        System.out.println("DatabaseInitializationService initializeDatabase ");
////
//////        this.dataSource = getDataSource(config);
////        this.dataSource = dynamicDataSourceConfig.getDataSource();
////
////        if (!checkTables(config)) {
////            log.info("⚠ Tables are missing, creating them now...");
////            Path path = Paths.get("backend/src/main/resources/sql/create_table.sql");
////            System.out.println("Checking file: " + path.toAbsolutePath());
////            System.out.println("File exists: " + Files.exists(path));
////
////            executeSqlScript("sql/create_table.sql");
////            if (!checkTables(config)) {
////                log.error("❌ Failed to create the necessary tables!");
////            } else {
////                log.info("✅ Tables created and verified successfully.");
////            }
////        } else {
////            log.info("✅ All necessary tables already exist.");
////        }
////    }
//
////
////    public boolean checkTables(DatabaseConfig config) {
////        System.out.println("DatabaseInitializationService checkTables ");
////
////        try {
////            // Створення з'єднання один раз
////            dynamicDataSourceConfig.setDataSource(
////                    config.getUrl(),
////                    config.getHost(),
////                    config.getPort(),
////                    config.getDatabase(),
////                    config.getUsername(),
////                    config.getPassword()
////            );
////
////            DataSource tempDataSource = dynamicDataSourceConfig.getDataSource();
////            if (tempDataSource == null) {
////                log.error("❌ DataSource не встановлено!");
////                return false;
////            }
////            JdbcTemplate jdbcTemplate = new JdbcTemplate(tempDataSource);
////
////            for (String table : TABLE_NAMES) {
////                if (!checkIfTableExists(table, jdbcTemplate)) {
////                    log.info("🔍 Checking if table '{}' exists...", table);
////
////                    log.warn("⚠ DatabaseInitializationService checkTables Таблиця '{}' не знайдена в базі {}!", table, config.getUrl());
////                    return false;
////                }
////            }
////            log.info("✅ Всі необхідні таблиці існують у базі {}", config.getUrl());
////            return true;
////        } catch (Exception e) {
////            log.error("❌ Помилка перевірки таблиць у базі {}", config.getUrl(), e);
////            return false;
////        }
////    }
////
////    private boolean checkIfTableExists(String tableName, JdbcTemplate jdbcTemplate) {
////        System.out.println("DatabaseInitializationService checkIfTableExists ");
////
////        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
////            if (conn.isClosed()) {
////                log.error("❌ З'єднання з базою даних закрите!");
////                return false;
////            }
////        } catch (Exception e) {
////            log.error("❌ Неможливо отримати з'єднання: {}", e.getMessage());
////            return false;
////        }
////
////        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
////            String dbProductName = conn.getMetaData().getDatabaseProductName();
////            log.info("🛠 dbProductName: {}", dbProductName);
////
////            String sql;
////
////            if (dbProductName.equalsIgnoreCase("PostgreSQL")) {
////                sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
////                log.info("🛠 Використовуємо SQL запит для перевірки таблиць в PostgreSQL");
////            } else if (dbProductName.equalsIgnoreCase("SQLite")) {
////                sql = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name = ?";
////                log.info("🛠 Використовуємо SQL запит для перевірки таблиць в SQLite");
////            } else {
////                throw new UnsupportedOperationException("Непідтримувана база даних: " + dbProductName);
////            }
////
////            // Виконання запиту
////            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
////            return count != null && count > 0;
////
////        } catch (Exception e) {
////            log.error("❌ Помилка перевірки таблиці {} у базі: {}", tableName, e.getMessage());
////            return false;
////        }
////    }
////
////
////    private void executeSqlScript(String scriptPath) {
////        System.out.println("DatabaseInitializationService executeSqlScript ");
////
////
////        try (Connection conn = dataSource.getConnection();
////             Statement stmt = conn.createStatement()) {
////            System.out.println("Connected to: " + conn.getMetaData().getURL());
////            String sql = new String(Files.readAllBytes(Paths.get("backend/src/main/resources/" + scriptPath)));
////            stmt.execute(sql);
////            log.info("✅ Tables created successfully!");
////
////
////        } catch (SQLException | IOException e) {
////            log.error("❌ Error executing SQL script", e);
////        }
////    }
////
////
////
////    public void clearDatabase() {
////        System.out.println("DatabaseInitializationService clearDatabase ");
////
////        try {
////            // Виконати SQL-операції для видалення даних з таблиць
////            String query = "DELETE FROM ?";  // використовуємо параметр для назви таблиці
////
////            for (String table : TABLE_NAMES) {
////                try {
////                    // Очищаємо кожну таблицю
////                    jdbcTemplate.execute("DELETE FROM " + table);
////                    log.info("✅ Дані з таблиці '{}' очищені.", table);
////                } catch (Exception e) {
////                    log.error("❌ Помилка очищення таблиці '{}'", table, e);
////                }
////            }
////
////            log.info("✅ База даних очищена!");
////        } catch (Exception e) {
////            log.error("❌ Помилка при очищенні бази даних", e);
////        }
////    }
////}
//
//
////    /**
////     * Перевіряє, чи всі необхідні таблиці існують у базі даних.
////     * Якщо хоча б однієї таблиці немає — повертає false.
////     */
////    public boolean checkTables(DatabaseConfig config) {
////        System.out.println("DatabaseInitializationService checkTables ");
////
////        try {
////            for (String table : TABLE_NAMES) {
////                if (!checkIfTableExists(table, config)) {
////                    log.info("🔍 Checking if table '{}' exists...", table);
////
////                    log.warn("⚠ DatabaseInitializationService checkTables Таблиця '{}' не знайдена в базі {}!", table, config.getUrl());
////                    return false;
////                }
////            }
////            log.info("✅ Всі необхідні таблиці існують у базі {}", config.getUrl());
////            return true;
////        } catch (Exception e) {
////            log.error("❌ Помилка перевірки таблиць у базі {}", config.getUrl(), e);
////            return false;
////        }
////    }
//
//
//
////
////
////    public boolean checkIfTableExists(String tableName, DatabaseConfig config) {
////        System.out.println("DatabaseInitializationService checkIfTableExists ");
////
////        try {
////            // Перевірка підключення до бази даних
////            dynamicDataSourceConfig.setDataSource(
////                    config.getUrl(),
////                    config.getHost(),
////                    config.getPort(),
////                    config.getDatabase(),
////                    config.getUsername(),
////                    config.getPassword()
////            );
////            log.info("📌 Параметри підключення - host: {}, port: {}, database: {}",
////                    config.getHost(), config.getPort(), config.getDatabase());
////
//////            DataSource tempDataSource = DynamicDataSourceConfig.getDataSource();
////            DataSource tempDataSource = dynamicDataSourceConfig.getDataSource();
////
////            log.info("🛠 Використовуємо DataSource: {}", tempDataSource);
////
////            if (tempDataSource == null) {
////                log.error("❌ Не вдалося створити DataSource для перевірки таблиць!");
////                return false;
////            }
////
////            // Ініціалізація JdbcTemplate
////            JdbcTemplate jdbcTemplate = new JdbcTemplate(tempDataSource);
////
////            // Підключення до бази та перевірка таблиці
////            try (Connection conn = tempDataSource.getConnection()) {
////                String dbProductName = conn.getMetaData().getDatabaseProductName();
////                log.info("🛠 dbProductName: {}", dbProductName);
////
////                String sql;
////
////                if (dbProductName.equalsIgnoreCase("PostgreSQL")) {
////                    sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
////                    log.info("🛠 Використовуємо SQL запит для перевірки таблиць в PostgreSQL");
////                } else if (dbProductName.equalsIgnoreCase("SQLite")) {
////                    sql = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name = ?";
////                    log.info("🛠 Використовуємо SQL запит для перевірки таблиць в SQLite");
////                } else {
////                    throw new UnsupportedOperationException("Непідтримувана база даних: " + dbProductName);
////                }
////
////                // Виконання запиту
////                Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
////                return count != null && count > 0;
////
////            } catch (Exception e) {
////                log.error("❌ Помилка перевірки таблиці {} у базі: {}", tableName, e.getMessage());
////                return false;
////            }
////
////        } catch (Exception e) {
////            log.error("❌ Помилка при налаштуванні підключення для перевірки таблиці '{}': {}", tableName, e.getMessage());
////            return false;
////        }
////    }
//
////    /**
////     * Повторна перевірка після створення таблиць.
////     * Лише перевіряє факт створення без виведення зайвих логів.
////     */
////    private void validateTablesAfterCreation(DatabaseConfig config) {
////        System.out.println("DatabaseInitializationService validateTablesAfterCreation ");
////
////        if (areTablesCreated(config)) {
////            log.info("✅ Таблиці успішно створені! Тепер можна зберігати дані.");
////        } else {
////            log.error("❌ Помилка: не всі таблиці створені! Можливо, є проблеми із SQL-скриптом.");
////        }
////    }
////
////
////    /**
////     * Перевіряє, чи всі необхідні таблиці існують у базі даних.
////     * Використовується для повторної перевірки після створення таблиць.
////     */
////    private boolean areTablesCreated(DatabaseConfig config) {
////        System.out.println("DatabaseInitializationService areTablesCreated ");
////
////        for (String table : TABLE_NAMES) {
////            if (!checkIfTableExists(table, config)) {
////                return false;
////            }
////        }
////        return true;
////    }
////}
//
//
//
//
////    /**
////     * Ініціалізує базу даних, якщо необхідні таблиці відсутні.
////     */
////
////    public void initializeDatabase(DatabaseConfig config) {
////        System.out.println("DatabaseInitializationService initializeDatabase ");
////
////        if (checkTables(config)) {
////            log.info("✅ Всі необхідні таблиці вже існують.");
////            return;
////        }
////
////        log.info("⏳ Виконуємо SQL-скрипт для створення таблиць...");
////        executeSqlScript("sql/create_table.sql");
////
////        // Викликаємо новий метод для повторної перевірки
////        validateTablesAfterCreation(config);
////    }
//
//
//
////
////    /**
////     * Виконує SQL-скрипт, що знаходиться у classpath.
////     */
////    private void executeSqlScript(String scriptPath) {
////        System.out.println("DatabaseInitializationService executeSqlScript ");
////
////        try {
////            Resource resource = new ClassPathResource(scriptPath);
////            if (!resource.exists()) {
////                log.error("❌ SQL-скрипт {} не знайдено!", scriptPath);
////                return;
////            }
////
////            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
////            String[] sqlStatements = sql.split(";");
////
////            for (String statement : sqlStatements) {
////                if (!statement.trim().isEmpty()) {
////                    jdbcTemplate.execute(statement.trim());
////                }
////            }
////
////            log.info("✅ Таблиці успішно створені!");
////        } catch (IOException e) {
////            log.error("❌ Помилка читання SQL-скрипта: {}", scriptPath, e);
////        } catch (Exception e) {
////            log.error("❌ Помилка виконання SQL-скрипта", e);
////        }
////    }
//
//
//
////    public void initializeDatabase(DatabaseConfig config) {
////        if (checkTables(config)) {
////            log.info("✅ Всі необхідні таблиці вже існують.");
////            return;
////        }
////
////        log.info("⏳ Виконуємо SQL-скрипт для створення таблиць...");
////        executeSqlScript("sql/create_table.sql");
////
////        if (checkTables(config)) {
////            log.info("✅ Таблиці успішно створені!");
////        } else {
////            log.error("❌ Помилка: таблиці не створені!");
////        }
////    }
//
//
////            try (Connection conn = tempDataSource.getConnection()) {
////                DatabaseMetaData metaData = conn.getMetaData();
////                String dbType = metaData.getDatabaseProductName().toLowerCase();
////
////                log.info("🔍 Перевіряємо таблицю '{}' в базі: {}", tableName, dbType);
////
////                if (dbType.contains("postgresql")) {
////                    String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
////                    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
////                    return count != null && count > 0;
////                } else if (dbType.contains("sqlite")) {
////                    try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, new String[]{"TABLE"})) {
////                        return rs.next();
////                    }
////                } else {
////                    log.warn("⚠️ Невідомий тип бази даних: {}", dbType);
////                }
////            }
////        } catch (SQLException e) {
////            log.error("❌ Помилка перевірки таблиці '{}': {}", tableName, e.getMessage());
////        }
////        return false;
////    }
//
//
////    public boolean checkTables() {
////        try {
////            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
////
////            for (String table : DatabaseInitializationService.TABLE_NAMES) {
////                try {
////                    Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
////                    if (count == null || count == 0) {
////                        log.warn("⚠ Таблиця '{}' не знайдена!", table);
////                        return false;
////                    }
////                } catch (Exception e) {
////                    log.error("❌ Помилка перевірки таблиці '{}'", table, e);
////                    return false;
////                }
////            }
////            log.info("✅ Всі необхідні таблиці існують.");
////            return true;
////        } catch (Exception e) {
////            log.error("❌ Помилка перевірки таблиць у БД", e);
////            return false;
////        }
////    }
////
////
////
//
//
////
////public class DatabaseInitializationService {
////
////    //    private final DynamicDataSourceConfig dynamicDataSourceConfig;
////    private final JdbcTemplate jdbcTemplate;
////
////    @Autowired
////    public DatabaseInitializationService(JdbcTemplate jdbcTemplate) {
////        this.jdbcTemplate = jdbcTemplate;
////    }
////
////    public boolean testConnection(DatabaseConfig config) {
////        try {
////            String jdbcUrl = config.getUrl();
////
////            // Якщо jdbcUrl не вказаний, будуємо його вручну
////            if (jdbcUrl == null || jdbcUrl.isBlank()) {
////                jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s",
////                        config.getHost(), config.getPort(), config.getDatabase());
////            }
////
////            log.info("🔍 Використовуємо jdbcUrl: {}", jdbcUrl);
////            log.info("🔍 Username: {}", config.getUsername());
////            log.info("🔍 Password: {}", config.getPassword() == null ? "null" : "***");
////            log.info("🔍 Host: {}", config.getHost());
////            log.info("🔍 Port: {}", config.getPort());
////            log.info("🔍 Database: {}", config.getDatabase());
////
////            HikariConfig hikariConfig = new HikariConfig();
////            hikariConfig.setJdbcUrl(jdbcUrl);
////            hikariConfig.setUsername(config.getUsername());
////            hikariConfig.setPassword(config.getPassword());
////            hikariConfig.setDriverClassName("org.postgresql.Driver");
////
////            HikariDataSource dataSource = new HikariDataSource(hikariConfig);
////            dataSource.getConnection().close();
////
////            log.info("✅ Підключення успішне!");
////            return true;
////        } catch (Exception e) {
////            log.error("❌ Помилка підключення: {}", e.getMessage(), e);
////            return false;
////        }
////    }
////
////
////
////    public void clearDatabase() {
////        List<String> tables = List.of("company", "employee", "category", "product", "storage", "transaction", "backup", "printout", "actionlog");
////
////        for (String table : tables) {
////            String sql = "DELETE FROM " + table;
////            jdbcTemplate.execute(sql);
////        }
////    }
////
////
////    public void initializeDatabase(DatabaseConfig config) {
////        testConnection(config); // Переконуємося, що підключення встановлено
////
////        // Приклад створення таблиці (завантаження SQL-файлу можна додати тут)
////        String sql = "CREATE TABLE IF NOT EXISTS example_table (id SERIAL PRIMARY KEY, name VARCHAR(255))";
////        jdbcTemplate.execute(sql);
////        log.info("✅ База даних перевірена та ініціалізована!");
////    }
////
////
////    public boolean checkTables(DatabaseConfig config) {
////        testConnection(config); // Переконуємося, що підключення встановлено
////
////        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public'";
////        int count = jdbcTemplate.queryForObject(sql, Integer.class); // тут була помилка
////        return count > 0;
////    }
////}
//
//
//
//
//
//
//
//
//
//
//
//
////package com.smartinvent.service;
////
////import com.smartinvent.config.DynamicDataSourceConfig;
////import com.smartinvent.models.DatabaseConfig;
////import lombok.extern.slf4j.Slf4j;
////import org.springframework.core.io.ClassPathResource;
////import org.springframework.core.io.Resource;
////import org.springframework.jdbc.core.JdbcTemplate;
////import org.springframework.stereotype.Service;
////
////import javax.sql.DataSource;
////import java.nio.charset.StandardCharsets;
////import java.sql.Connection;
////import java.util.List;
////
////@Slf4j
////@Service
////public class DatabaseInitializationService {
////
////    private final JdbcTemplate jdbcTemplate;
////    private final DataSource dataSource;
////
////    public DatabaseInitializationService(JdbcTemplate jdbcTemplate, DataSource dataSource) {
////        this.jdbcTemplate = jdbcTemplate;
////        this.dataSource = dataSource;
////    }
////
////    private static final List<String> TABLE_NAMES = List.of(
////            "company", "employee", "category", "product",
////            "storage", "transactions", "backup", "printout", "action_log"
////    );
////
////    public boolean checkIfTableExists(String tableName, DatabaseConfig config) {
////        try {
////            DynamicDataSourceConfig.setDataSource(
////                    config.getUrl(), config.getHost(), config.getPort(),
////                    config.getDatabase(), config.getUsername(), config.getPassword()
////            );
////
////            DataSource tempDataSource = DynamicDataSourceConfig.getDataSource();
////            if (tempDataSource == null) {
////                log.error("❌ Не вдалося створити DataSource!");
////                return false;
////            }
////
////            try (Connection conn = tempDataSource.getConnection()) {
////                String dbProductName = conn.getMetaData().getDatabaseProductName();
////                String sql = switch (dbProductName) {
////                    case "PostgreSQL" -> "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
////                    case "SQLite" -> "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name = ?";
////                    default -> throw new UnsupportedOperationException("Непідтримувана БД: " + dbProductName);
////                };
////
////                Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
////                return count != null && count > 0;
////            }
////        } catch (Exception e) {
////            log.error("❌ Помилка перевірки таблиці '{}': {}", tableName, e.getMessage());
////        }
////        return false;
////    }
////
////    public boolean checkTables(DatabaseConfig config) {
////        return TABLE_NAMES.stream().allMatch(table -> checkIfTableExists(table, config));
////    }
////
////    public void clearDatabase() {
////        for (String table : TABLE_NAMES) {
////            try {
////                jdbcTemplate.execute("DELETE FROM " + table);
////                log.info("✅ Очищено '{}'", table);
////            } catch (Exception e) {
////                log.error("❌ Помилка очищення '{}'", table, e);
////            }
////        }
////    }
////
////    public void initializeDatabase(DatabaseConfig config) {
////        if (checkTables(config)) {
////            log.info("✅ Всі таблиці існують.");
////            return;
////        }
////
////        executeSqlScript("sql/create_table.sql");
////
////        if (checkTables(config)) {
////            log.info("✅ Таблиці створено!");
////        } else {
////            log.error("❌ Не всі таблиці створені!");
////        }
////    }
////
////    private void executeSqlScript(String scriptPath) {
////        try {
////            Resource resource = new ClassPathResource(scriptPath);
////            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
////            for (String statement : sql.split(";")) {
////                if (!statement.trim().isEmpty()) {
////                    jdbcTemplate.execute(statement.trim());
////                }
////            }
////        } catch (Exception e) {
////            log.error("❌ Помилка виконання SQL-скрипта!", e);
////        }
////    }
////}
//
//
//
////
//////package com.smartinvent.service;
//////
//////import lombok.RequiredArgsConstructor;
//////import lombok.extern.slf4j.Slf4j;
//////import org.springframework.core.io.ClassPathResource;
//////import org.springframework.core.io.Resource;
//////import org.springframework.jdbc.core.JdbcTemplate;
//////import org.springframework.stereotype.Service;
//////import java.io.IOException;
//////import java.nio.charset.StandardCharsets;
//////import java.util.List;
//////
//////@Slf4j
//////@Service
//////@RequiredArgsConstructor
//////public class DatabaseInitializationService {
//////
//////    private final JdbcTemplate jdbcTemplate;
//////
//////    private static final List<String> TABLE_NAMES = List.of(
//////            "company", "employee", "category", "product",
//////            "storage", "transaction", "backup", "printout", "action_log"
//////    );
//////
//////
//////    public boolean isDatabaseInitialized() {
//////        String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
//////
//////        for (String table : TABLE_NAMES) {
//////            Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
//////            if (count == null || count == 0) {
//////                log.warn("⚠ Таблиця '{}' не знайдена!", table);
//////                return false;
//////            }
//////        }
//////        return true;
//////    }
////////    public boolean isDatabaseInitialized() {
////////        for (String table : TABLE_NAMES) {
////////            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";
////////            Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
////////            if (count == null || count == 0) {
////////                log.warn("⚠ Таблиця '{}' не знайдена!", table);
////////                return false;
////////            }
////////        }
////////        return true;
////////    }
//////
//////    public boolean checkTables() {
//////        try {
//////            for (String table : TABLE_NAMES) {
//////                String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
//////                Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
//////                if (count == null || count == 0) {
//////                    log.warn("⚠ Таблиця '{}' не знайдена!", table);
//////                    return false;
//////                }
//////            }
//////            log.info("✅ Всі необхідні таблиці існують.");
//////            return true;
//////        } catch (Exception e) {
//////            log.error("❌ Помилка перевірки таблиць в БД", e);
//////            return false;
//////        }
//////    }
//////
//////    public void initializeDatabase() {
//////
//////        if (isDatabaseInitialized()) {
//////            log.info("✅ Всі необхідні таблиці вже існують.");
//////            return ;
//////        }
//////
//////        log.info("⏳ Виконуємо SQL-скрипт для створення таблиць...");
//////        executeSqlScript("sql/create_table.sql");
//////
//////        if (isDatabaseInitialized()) {
//////            log.info("✅ Таблиці успішно створені!");
//////        } else {
//////            log.error("❌ Помилка: таблиці не створені!");
//////        }
//////    }
//////
//////
//////    private void executeSqlScript(String scriptPath) {
//////        try {
//////            Resource resource = new ClassPathResource(scriptPath);
//////            if (!resource.exists()) {
//////                log.error("❌ SQL-скрипт {} не знайдено!", scriptPath);
//////                return;
//////            }
//////
//////            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
//////            String[] sqlStatements = sql.split(";");
//////
//////            for (String statement : sqlStatements) {
//////                if (!statement.trim().isEmpty()) {
//////                    jdbcTemplate.execute(statement.trim());
//////                }
//////            }
//////
//////            log.info("✅ Таблиці успішно створені!");
//////        } catch (IOException e) {
//////            log.error("❌ Помилка читання SQL-скрипта: {}", scriptPath, e);
//////        } catch (Exception e) {
//////            log.error("❌ Помилка виконання SQL-скрипта", e);
//////        }
//////    }
////////    private void executeSqlScript(String scriptPath) {
////////        try {
////////            Resource resource = new ClassPathResource(scriptPath);
////////            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
////////            log.info("📜 Виконуємо SQL:\n{}", sql);
////////            jdbcTemplate.execute(sql);
////////            log.info("✅ Таблиці успішно створені!");
////////        } catch (IOException e) {
////////            log.error("❌ Помилка читання SQL-скрипта", e);
////////        } catch (Exception e) {
////////            log.error("❌ Помилка виконання SQL-скрипта", e);
////////        }
////////    }
//////}
//////
//////
//////
////////package com.smartinvent.service;
////////
////////import lombok.RequiredArgsConstructor;
////////import lombok.extern.slf4j.Slf4j;
////////import org.springframework.jdbc.core.JdbcTemplate;
////////import org.springframework.stereotype.Service;
////////import java.io.IOException;
////////import java.nio.file.Files;
////////import java.nio.file.Paths;
////////import java.util.List;
////////
////////@Slf4j
////////@Service
////////@RequiredArgsConstructor
////////public class DatabaseInitializationService {
////////
////////    private final JdbcTemplate jdbcTemplate;
////////
////////    private static final List<String> TABLE_NAMES = List.of(
////////            "company", "employee", "category", "product",
////////            "storage", "transaction", "backup", "printout", "action_log"
////////    );
////////
////////    public boolean isDatabaseInitialized() {
////////        for (String table : TABLE_NAMES) {
////////            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
////////            Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
////////            if (count == null || count == 0) {
////////                log.warn("Таблиця '{}' не знайдена!", table);
////////                return false;
////////            }
////////        }
////////
////////        return true;
////////    }
////////
////////    public boolean checkTables() {
////////        try {
////////            for (String table : TABLE_NAMES) {
////////                String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
////////                Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
////////                if (count == null || count == 0) {
////////                    log.warn("⚠ Таблиця '{}' не знайдена!", table);
////////                    return false;
////////                }
////////            }
////////            log.info("✅ Всі необхідні таблиці існують.");
////////            return true;
////////        } catch (Exception e) {
////////            log.error("❌ Помилка перевірки таблиць в БД", e);
////////            return false;
////////        }
////////    }
////////
////////
////////    public void initializeDatabase() {
////////        if (isDatabaseInitialized()) {
////////            log.info("✅ Всі необхідні таблиці вже існують.");
////////            return;
////////        }
////////
////////        log.info("⏳ Виконуємо SQL-скрипт для створення таблиць...");
////////        executeSqlScript("sql/create_table.sql");
////////    }
////////
////////    private void executeSqlScript(String scriptPath) {
////////        try {
////////            String sql = new String(Files.readAllBytes(Paths.get(scriptPath)));
////////            jdbcTemplate.execute(sql);
////////            log.info("✅ Таблиці успішно створені!");
////////        } catch (IOException e) {
////////            log.error("❌ Помилка читання SQL-скрипта", e);
////////        } catch (Exception e) {
////////            log.error("❌ Помилка виконання SQL-скрипта", e);
////////        }
////////    }
////////}
////////
////////
////////
//////////package com.smartinvent.service;
//////////
//////////import lombok.extern.slf4j.Slf4j;
//////////import org.springframework.stereotype.Service;
//////////
//////////import javax.annotation.PostConstruct;
//////////import javax.sql.DataSource;
//////////import java.io.IOException;
//////////import java.nio.file.Files;
//////////import java.nio.file.Paths;
//////////import java.sql.*;
//////////
//////////@Slf4j
//////////@Service
//////////public class DatabaseInitializationService {
//////////
//////////    private final DataSource dataSource;
//////////
//////////    public DatabaseInitializationService(DataSource dataSource) {
//////////        this.dataSource = dataSource;
//////////    }
//////////
//////////    @PostConstruct
//////////    public void initializeDatabase() {
//////////        log.info("🔄 Перевірка та ініціалізація бази даних...");
//////////
//////////        if (!isDatabaseInitialized()) {
//////////            log.info("⏳ База даних порожня. Виконуємо SQL-скрипт для створення таблиць...");
//////////            executeSqlScript("sql/create_table.sql");
//////////        } else {
//////////            log.info("✅ Всі необхідні таблиці вже існують.");
//////////        }
//////////    }
//////////
//////////    private boolean isDatabaseInitialized() {
//////////        String checkQuery = "SELECT count(*) FROM information_schema.tables WHERE table_name = 'company'";
//////////
//////////        try (Connection conn = dataSource.getConnection();
//////////             PreparedStatement stmt = conn.prepareStatement(checkQuery);
//////////             ResultSet rs = stmt.executeQuery()) {
//////////            if (rs.next()) {
//////////                return rs.getInt(1) > 0; // Якщо таблиця існує, повертаємо true
//////////            }
//////////        } catch (SQLException e) {
//////////            log.error("Помилка при перевірці бази даних", e);
//////////        }
//////////        return false;
//////////    }
//////////
//////////    private void executeSqlScript(String scriptPath) {
//////////        try (Connection conn = dataSource.getConnection();
//////////             Statement stmt = conn.createStatement()) {
//////////
//////////            String sql = new String(Files.readAllBytes(Paths.get("backend/src/main/resources/" + scriptPath)));
//////////            stmt.execute(sql);
//////////            log.info("✅ Таблиці успішно створені!");
//////////
//////////        } catch (SQLException | IOException e) {
//////////            log.error("❌ Помилка виконання SQL-скрипта", e);
//////////        }
//////////    }
//////////}
