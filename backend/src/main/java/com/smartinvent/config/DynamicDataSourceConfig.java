package com.smartinvent.config;

import com.smartinvent.models.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfig {

    private final ConfigService configService;

    public DynamicDataSourceConfig(ConfigService configService) {
        this.configService = configService;
    }

    @Bean
    public DataSource dataSource() {
        DatabaseConfig databaseConfig = configService.getDatabaseConfig();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(databaseConfig.getUrl());
        hikariConfig.setUsername(databaseConfig.getUsername());
        hikariConfig.setPassword(databaseConfig.getPassword());
        hikariConfig.setDriverClassName(databaseConfig.getDriverClassName());
        return new HikariDataSource(hikariConfig);
    }
}


//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DynamicDataSourceConfig {
//
//    @Autowired
//    private ConfigService configService;
//
////    @Bean(name = "dynamicDataSource")
//    @Bean
//    public DataSource dataSource() {
//        ConfigService.DatabaseConfig config = configService.loadConfig();
//
//        if (config == null || config.getDb_url() == null) {
//            System.out.println("⚠️ Конфігурація відсутня! База даних не підключена. Спочатку налаштуйте підключення через API.");
//            return new DriverManagerDataSource(); // Повертаємо пустий DataSource, щоб сервер запустився
//        }
//
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl(config.getDb_url());
//        dataSource.setUsername(config.getDb_username());
//        dataSource.setPassword(config.getDb_password());
//
//        System.out.println("✅ Підключено до бази: " + config.getDb_url());
//        return dataSource;
//    }
//}
