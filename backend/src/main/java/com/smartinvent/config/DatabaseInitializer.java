//package com.smartinvent.config;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.stereotype.Component;
//
//
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Properties;
//
//@Component
//public class DatabaseInitializer {
//    private final JdbcTemplate jdbcTemplate;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public DatabaseInitializer() {
//        this.jdbcTemplate = new JdbcTemplate(dataSource());
//    }
//
//    @PostConstruct
//    public void initializeDatabase() {
//        try {
//            // 1️⃣ Виконуємо SQL-скрипт створення таблиць
//            executeSQLScript("sql/create_table.sql");
//            System.out.println("✅ Таблиці успішно створені або вже існують.");
//
//            // 2️⃣ Імпортуємо дані з JSON
//            importDataFromJson("company_data.json", "INSERT INTO Company (name) VALUES (?)");
//            importDataFromJson("admin_data.json", "INSERT INTO Employee (company_id, username, password, role) VALUES (?, ?, ?, ?)");
//
//            System.out.println("✅ Дані успішно імпортовані в БД.");
//
//            // 3️⃣ Перехід на головну сторінку адміна (працює тільки в вебконтролерах)
//            // redirectToAdminPage(); ❌ Цю логіку слід реалізувати у Spring контролері
//
//        } catch (Exception e) {
//            throw new RuntimeException("❌ Помилка ініціалізації БД", e);
//        }
//    }
//
//    private DataSource dataSource() {
//        Properties dbProps = DatabaseConfigReader.getDatabaseProperties();
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl(dbProps.getProperty("db.url"));
//        dataSource.setUsername(dbProps.getProperty("db.user"));
//        dataSource.setPassword(dbProps.getProperty("db.password"));
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        return dataSource;
//    }
//
//    private void executeSQLScript(String path) throws Exception {
//        InputStream inputStream = new ClassPathResource(path).getInputStream();
//        String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
//        jdbcTemplate.execute(sql);
//    }
//
//    private void importDataFromJson(String path, String sql) throws Exception {
//        InputStream inputStream = new ClassPathResource(path).getInputStream();
//        JsonNode jsonData = objectMapper.readTree(inputStream);
//
//        if (path.contains("company_data.json")) {
//            for (JsonNode node : jsonData) {
//                jdbcTemplate.update(sql, node.get("name").asText());
//            }
//        } else if (path.contains("admin_data.json")) {
//            for (JsonNode node : jsonData) {
//                jdbcTemplate.update(sql,
//                        node.get("company_id").asInt(),
//                        node.get("username").asText(),
//                        node.get("password").asText(),
//                        node.get("role").asText()
//                );
//            }
//        }
//    }
//}
//
//
////package com.smartinvent.config;
////
////import org.springframework.core.io.ClassPathResource;
////import org.springframework.jdbc.core.JdbcTemplate;
////import org.springframework.jdbc.datasource.DriverManagerDataSource;
////import org.springframework.stereotype.Component;
////
////import javax.annotation.PostConstruct;
////import javax.sql.DataSource;
////import java.io.BufferedReader;
////import java.io.InputStream;
////import java.io.InputStreamReader;
////import java.util.Properties;
////import java.util.stream.Collectors;
////
////@Component
////public class DatabaseInitializer {
////    private final JdbcTemplate jdbcTemplate;
////
////    public DatabaseInitializer() {
////        this.jdbcTemplate = new JdbcTemplate(dataSource());
////    }
////
////    @PostConstruct
////    public void initializeDatabase() {
////        try {
////            String sql = loadSQLScript("sql/create_table.sql");
////            jdbcTemplate.execute(sql);
////            System.out.println("✅ Таблиці успішно створені або вже існують.");
////        } catch (Exception e) {
////            throw new RuntimeException("❌ Помилка ініціалізації БД", e);
////        }
////    }
////
////    private DataSource dataSource() {
////        Properties dbProps = DatabaseConfigReader.getDatabaseProperties();
////        DriverManagerDataSource dataSource = new DriverManagerDataSource();
////        dataSource.setUrl(dbProps.getProperty("db.url"));
////        dataSource.setUsername(dbProps.getProperty("db.user"));
////        dataSource.setPassword(dbProps.getProperty("db.password"));
////        dataSource.setDriverClassName("org.postgresql.Driver");
////        return dataSource;
////    }
////
////    private String loadSQLScript(String path) throws Exception {
////        InputStream inputStream = new ClassPathResource(path).getInputStream();
////        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
////            return reader.lines().collect(Collectors.joining("\n"));
////        }
////    }
////}
