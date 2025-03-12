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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseInitializationService {

    private final JdbcTemplate jdbcTemplate;

    private static final List<String> TABLE_NAMES = List.of(
            "company", "employee", "category", "product",
            "storage", "transaction", "backup", "printout", "action_log"
    );

    /**
     * Перевіряє, чи всі необхідні таблиці існують у базі даних.
     * Якщо хоча б однієї таблиці немає — повертає false.
     */
    public boolean checkTables() {
        try {
            String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?";

            for (String table : TABLE_NAMES) {
                try {
                    Integer count = jdbcTemplate.queryForObject(query, Integer.class, table);
                    if (count == null || count == 0) {
                        log.warn("⚠ Таблиця '{}' не знайдена!", table);
                        return false;
                    }
                } catch (Exception e) {
                    log.error("❌ Помилка перевірки таблиці '{}'", table, e);
                    return false;
                }
            }
            log.info("✅ Всі необхідні таблиці існують.");
            return true;
        } catch (Exception e) {
            log.error("❌ Помилка перевірки таблиць у БД", e);
            return false;
        }
    }


    public boolean testConnection(DatabaseConfig config) {
        try {
            DynamicDataSourceConfig.setDataSource(config.getUrl(), config.getUsername(), config.getPassword());
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
    public void initializeDatabase() {
        if (checkTables()) {
            log.info("✅ Всі необхідні таблиці вже існують.");
            return;
        }

        log.info("⏳ Виконуємо SQL-скрипт для створення таблиць...");
        executeSqlScript("sql/create_table.sql");

        if (checkTables()) {
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
