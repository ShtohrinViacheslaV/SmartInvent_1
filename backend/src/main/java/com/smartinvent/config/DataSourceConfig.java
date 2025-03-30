//package com.smartinvent.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Value("${db.mode}")
//    private String dbMode;
//
//    @Bean
//    public DataSource dataSource() {
//        if ("postgres".equalsIgnoreCase(dbMode)) {
//            return createPostgresDataSource();
//        } else {
//            return createSQLiteDataSource();
//        }
//    }
//
//    private DataSource createPostgresDataSource() {
//        HikariDataSource ds = new HikariDataSource();
//        ds.setJdbcUrl(System.getProperty("db.url", "jdbc:postgresql://default-host/default-db"));
//        ds.setUsername(System.getProperty("db.user", "postgres"));
//        ds.setPassword(System.getProperty("db.password", "password"));
//        ds.setDriverClassName("org.postgresql.Driver");
//        return ds;
//    }
//
//    private DataSource createSQLiteDataSource() {
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setUrl("jdbc:sqlite:smartinvent_local.db");
//        ds.setDriverClassName("org.sqlite.JDBC");
//        return ds;
//    }
//}
