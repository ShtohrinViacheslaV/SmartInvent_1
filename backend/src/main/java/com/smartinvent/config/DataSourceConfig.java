//package com.smartinvent.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
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
//    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
//
//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//
//    @Bean(name = "defaultDataSource")
//    public DataSource dataSource() {
//        if (url == null || url.isEmpty() || username == null || username.isEmpty()) {
//            logger.error("Помилка: Невірні параметри підключення до БД!");
//            throw new IllegalStateException("Параметри БД не можуть бути порожніми");
//        }
//
//        logger.info("Підключення до бази даних: {}", url);
//
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//
//        return dataSource;
//    }
//}
//
//
////package com.smartinvent.config;
////
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.jdbc.datasource.DriverManagerDataSource;
////
////import javax.sql.DataSource;
////
////@Configuration
////public class DataSourceConfig {
////
////    @Value("${spring.datasource.url}")
////    private String url;
////
////    @Value("${spring.datasource.username}")
////    private String username;
////
////    @Value("${spring.datasource.password}")
////    private String password;
////
////    @Bean
////    public DataSource dataSource() {
////        DriverManagerDataSource dataSource = new DriverManagerDataSource();
////        dataSource.setDriverClassName("org.postgresql.Driver");
////        dataSource.setUrl(url);
////        dataSource.setUsername(username);
////        dataSource.setPassword(password);
////        return dataSource;
////    }
////}
