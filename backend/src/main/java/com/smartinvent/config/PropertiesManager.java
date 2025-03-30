//package com.smartinvent.config;
//
//import com.smartinvent.models.DatabaseConfig;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Properties;
//
//@Component
//public class PropertiesManager {
//
//    @Value("${spring.config.location}")
//    private String propertiesFilePath;
//
//    public void saveDatabaseConfig(DatabaseConfig config) {
//        Properties properties = new Properties();
//        try (FileInputStream input = new FileInputStream(propertiesFilePath)) {
//            properties.load(input);
//        } catch (IOException e) {
//            // Якщо файл не існує, просто створимо новий
//        }
//
//        properties.setProperty("db.url", config.getUrl());
//        properties.setProperty("db.username", config.getUsername());
//        properties.setProperty("db.password", config.getPassword());
//
//        try (FileOutputStream output = new FileOutputStream(propertiesFilePath)) {
//            properties.store(output, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public DatabaseConfig loadDatabaseConfig() {
//        Properties properties = new Properties();
//        try (FileInputStream input = new FileInputStream(propertiesFilePath)) {
//            properties.load(input);
//            String url = properties.getProperty("db.url");
//            String username = properties.getProperty("db.username");
//            String password = properties.getProperty("db.password");
//            return new DatabaseConfig(url, username, password);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}