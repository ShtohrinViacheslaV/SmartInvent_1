package com.smartinvent.config;

import com.smartinvent.models.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DynamicDataSourceConfig {

    private static DataSource currentDataSource;

    // Метод для налаштування підключення до БД
    // public static void setDataSource(String url)
    // public static void setDataSource(String host, String port, String database, String username, String password)
    public static void setDataSource(String url, String host, String port, String database, String username, String password) {
        try {
            String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            HikariDataSource dataSource = DataSourceBuilder.create()
                    .type(HikariDataSource.class)
                    .driverClassName("org.postgresql.Driver")
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .build();
            currentDataSource = dataSource;
            log.info("Налаштовано підключення до PostgreSQL: {}", jdbcUrl);
        } catch (Exception e) {
            log.error("Помилка при налаштуванні підключення до PostgreSQL", e);
        }
    }

    @Bean
    @Primary
    public static DataSource getDataSource() {
        log.debug("Виклик методу getDataSource");

        if (currentDataSource == null) {
            log.warn("⚠ База даних ще не налаштована! Повертаємо SQLite як запасну базу.");
            // Повертаємо локальну БД (SQLite) як запасну, якщо не налаштовано підключення до PostgreSQL
            return DataSourceBuilder.create()
                    .url("jdbc:sqlite:smartinvent_local.db")
                    .driverClassName("org.sqlite.JDBC")
                    .build();
        }
        log.info("Використовуємо підключення до PostgreSQL");
        return currentDataSource;
    }
}


//@Slf4j
//@Configuration
//public class DynamicDataSourceConfig {
//
//    private static DataSource currentDataSource;
//
//
//    // Метод для налаштування підключення до БД
//
//    public static void setDataSource(String url, String host, String port, String database, String username, String password) {
//        String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;
//        HikariDataSource dataSource = DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .driverClassName("org.postgresql.Driver")
//                .url(jdbcUrl)
//                .username(username)
//                .password(password)
//                .build();
//        currentDataSource = dataSource;
//
//
//    }
//    @Bean
//    @Primary
//    public static DataSource getDataSource() {
//        System.out.println("DynamicDataSourceConfig getDataSource ");
//
//        if (currentDataSource == null) {
//            log.warn("⚠ База даних ще не налаштована!");
//            // Повертаємо локальну БД (SQLite) як запасну, якщо не налаштовано підключення до PostgreSQL
//            return DataSourceBuilder.create()
//                    .url("jdbc:sqlite:smartinvent_local.db")
//                    .driverClassName("org.sqlite.JDBC")
//                    .build();
//        }
//        return currentDataSource;
//    }
//}

    //    public static void setDataSource(String url, String host, String port, String database, String username, String password) {
//        System.out.println("DynamicDataSourceConfig setDataSource ");
//
//        String jdbcUrl;
//
//        if (url != null && !url.isEmpty()) {
//            jdbcUrl = url;
//        } else {
//            jdbcUrl = "jdbc:postgresql://" + host  + ":" + port + "/" + database;
//        }
//
//        log.info("🔄 Налаштовуємо підключення до БД: {}", jdbcUrl);
//
//        HikariDataSource dataSource = DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .driverClassName("org.postgresql.Driver")
//                .url(jdbcUrl)
//                .username(username)
//                .password(password)
//                .build();
//
//        currentDataSource = dataSource;
//    }

//    @Bean
//    public DataSource getDataSource() {
//        DatabaseConfig config = DbConfigManager.loadConfig(); // Завантаження збереженої конфігурації
//        if (config == null || config.getUrl().isEmpty()) {
//            log.warn("⚠ База даних ще не налаштована!");
//            return new DriverManagerDataSource("jdbc:sqlite:smartinvent_local.db"); // Локальна БД за замовчуванням
//        }
//
//        String url = config.getUrl().isEmpty()
//                ? "jdbc:postgresql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase()
//                : config.getUrl();
//
//        log.info("✅ Використовується база даних: " + url);
//
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl(url);
//        dataSource.setUsername(config.getUsername());
//        dataSource.setPassword(config.getPassword());
//
//        return dataSource;
//    }
////
    // Створення біну для DataSource





//
//
//    private HikariDataSource dataSource;
//
//    public DataSource getDataSource() {
//        return dataSource;
//    }
//
//    public void setDataSource(String url, String username, String password) {
//        if (dataSource != null) {
//            dataSource.close(); // Закриваємо попереднє підключення
//        }
//
//        HikariDataSource hikariDataSource = new HikariDataSource();
//        hikariDataSource.setJdbcUrl(url);
//        hikariDataSource.setUsername(username);
//        hikariDataSource.setPassword(password);
//        hikariDataSource.setDriverClassName("org.postgresql.Driver");
//
//        this.dataSource = hikariDataSource;
//    }
//}


//
////package com.smartinvent.config;
////
////import com.zaxxer.hikari.HikariDataSource;
////import lombok.extern.slf4j.Slf4j;
////import org.springframework.boot.jdbc.DataSourceBuilder;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.context.annotation.Primary;
////import javax.sql.DataSource;
////
////@Slf4j
////@Configuration
////public class DynamicDataSourceConfig {
////
////    private static DataSource currentDataSource;
////
////    public static void setDataSource(String url, String username, String password) {
////        log.info("🔄 Налаштовуємо підключення до БД: {}", url);
////
////        HikariDataSource dataSource = DataSourceBuilder.create()
////                .type(HikariDataSource.class)
////                .driverClassName("org.postgresql.Driver")
////                .url(url)
////                .username(username)
////                .password(password)
////                .build();
////
////        currentDataSource = dataSource;
////    }
////
////    @Bean
////    @Primary
////    public static DataSource getDataSource() {
////        if (currentDataSource == null) {
////            log.warn("⚠ База даних ще не налаштована!");
////            return null;
////        }
////        return currentDataSource;
////    }
////}
//
//
////package com.smartinvent.config;
////
////import com.smartinvent.models.DatabaseConfig;
////import com.zaxxer.hikari.HikariConfig;
////import com.zaxxer.hikari.HikariDataSource;
////import lombok.extern.slf4j.Slf4j;
////import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.context.annotation.DependsOn;
////import javax.sql.DataSource;
////
////@Slf4j
////@Configuration
////@DependsOn("configService")
////public class DynamicDataSourceConfig {
////
////    private final ConfigService configService;
////
////    public DynamicDataSourceConfig(ConfigService configService) {
////        this.configService = configService;
////    }
////
////    @Bean
////    @ConditionalOnProperty(name = "database.enabled", havingValue = "true", matchIfMissing = false)
////    public DataSource dataSource() {
////        DatabaseConfig databaseConfig = configService.getConfig();
////
////        if (databaseConfig == null) {
////            log.warn("⚠️ Database config is missing. Skipping database initialization.");
////            return null;
////        }
////
////        boolean isUrlPresent = databaseConfig.getUrl() != null && !databaseConfig.getUrl().isEmpty();
////        boolean isManualConfigPresent = databaseConfig.getHost() != null && !databaseConfig.getHost().isEmpty()
////                && databaseConfig.getPort() != null && !databaseConfig.getPort().isEmpty()
////                && databaseConfig.getUsername() != null && !databaseConfig.getUsername().isEmpty()
////                && databaseConfig.getPassword() != null;
////
////        if (!isUrlPresent && !isManualConfigPresent) {
////            log.warn("⚠️ No valid database configuration found. Skipping database initialization.");
////            return null;
////        }
////
////        HikariConfig hikariConfig = new HikariConfig();
////
////        if (isUrlPresent) {
////            hikariConfig.setJdbcUrl(databaseConfig.getUrl());
////        } else {
////            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/mydatabase",
////                    databaseConfig.getHost(),
////                    databaseConfig.getPort());
////            hikariConfig.setJdbcUrl(jdbcUrl);
////        }
////
////        hikariConfig.setUsername(databaseConfig.getUsername());
////        hikariConfig.setPassword(databaseConfig.getPassword());
////        hikariConfig.setDriverClassName("org.postgresql.Driver");
////        hikariConfig.setMaximumPoolSize(10);
////        hikariConfig.setConnectionTimeout(30000);
////
////        log.info("✅ Database configuration is valid. Initializing DataSource...");
////        return new HikariDataSource(hikariConfig);
////    }
////}
////
////
////
//////package com.smartinvent.config;
//////
//////import com.smartinvent.models.DatabaseConfig;
//////import com.zaxxer.hikari.HikariConfig;
//////import com.zaxxer.hikari.HikariDataSource;
//////import lombok.extern.slf4j.Slf4j;
//////import org.springframework.beans.factory.annotation.Autowired;
//////import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//////import org.springframework.context.annotation.Bean;
//////import org.springframework.context.annotation.Configuration;
//////import org.springframework.context.annotation.DependsOn;
//////import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//////
//////import javax.sql.DataSource;
//////import java.util.HashMap;
//////import java.util.Map;
//////import java.util.Optional;
//////
//////
//////@Slf4j
//////@Configuration
//////@DependsOn("configService") // Впевненість, що конфігурація завантажена
//////public class DynamicDataSourceConfig {
//////
//////    private final ConfigService configService;
//////
//////    public DynamicDataSourceConfig(ConfigService configService) {
//////        this.configService = configService;
//////    }
//////
//////
//////    @Bean
//////    @ConditionalOnProperty(name = "database.enabled", havingValue = "true", matchIfMissing = false)
//////    public DataSource dataSource() {
//////        DatabaseConfig databaseConfig = configService.getConfig();
//////
//////        if (databaseConfig == null || databaseConfig.getUrl() == null || databaseConfig.getUrl().isEmpty()) {
//////            log.warn("⚠️ Database config is missing. Skipping database initialization.");
//////            return null;  // Не створюємо DataSource
//////        }
//////
//////        log.info("✅ Database configuration found, initializing DataSource...");
//////        HikariConfig hikariConfig = new HikariConfig();
//////        hikariConfig.setJdbcUrl(databaseConfig.getUrl());
//////        hikariConfig.setUsername(databaseConfig.getUsername());
//////        hikariConfig.setPassword(databaseConfig.getPassword());
//////
//////        return new HikariDataSource(hikariConfig);
//////    }
//////}
//////
//////
////////    public DataSource dataSource() {
////////        DatabaseConfig databaseConfig = configService.getDatabaseConfig();
////////
////////        if (databaseConfig == null) {
////////            throw new IllegalStateException("DatabaseConfig is null!");
////////        }
////////        if (databaseConfig.getUrl() == null) {
////////            throw new IllegalStateException("Database URL is null!");
////////        }
////////        if (databaseConfig.getUsername() == null) {
////////            throw new IllegalStateException("Database username is null!");
////////        }
////////        if (databaseConfig.getPassword() == null) {
////////            throw new IllegalStateException("Database password is null!");
////////        }
////////
////////        HikariConfig hikariConfig = new HikariConfig();
////////        hikariConfig.setJdbcUrl(databaseConfig.getUrl());
////////        hikariConfig.setUsername(databaseConfig.getUsername());
////////        hikariConfig.setPassword(databaseConfig.getPassword());
//////////        hikariConfig.setDriverClassName(databaseConfig.getDriverClassName());
////////
////////        return new HikariDataSource(hikariConfig);
////////    }
//////
////////}
//////
//////
////////import org.springframework.beans.factory.annotation.Autowired;
////////import org.springframework.context.annotation.Bean;
////////import org.springframework.context.annotation.Configuration;
////////import org.springframework.jdbc.datasource.DriverManagerDataSource;
////////
////////import javax.sql.DataSource;
////////
////////@Configuration
////////public class DynamicDataSourceConfig {
////////
////////    @Autowired
////////    private ConfigService configService;
////////
//////////    @Bean(name = "dynamicDataSource")
////////    @Bean
////////    public DataSource dataSource() {
////////        ConfigService.DatabaseConfig config = configService.loadConfig();
////////
////////        if (config == null || config.getDb_url() == null) {
////////            System.out.println("⚠️ Конфігурація відсутня! База даних не підключена. Спочатку налаштуйте підключення через API.");
////////            return new DriverManagerDataSource(); // Повертаємо пустий DataSource, щоб сервер запустився
////////        }
////////
////////        DriverManagerDataSource dataSource = new DriverManagerDataSource();
////////        dataSource.setDriverClassName("org.postgresql.Driver");
////////        dataSource.setUrl(config.getDb_url());
////////        dataSource.setUsername(config.getDb_username());
////////        dataSource.setPassword(config.getDb_password());
////////
////////        System.out.println("✅ Підключено до бази: " + config.getDb_url());
////////        return dataSource;
////////    }
////////}
