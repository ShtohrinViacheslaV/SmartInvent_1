//package com.smartinvent.service;
//
//import com.smartinvent.model.DatabaseConfig;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//
//@Service
//public class DatabaseConfigService {
//
//    private static final String CONFIG_FILE_PATH = "config/application-company.properties";
//
//    public boolean saveConfig(DatabaseConfig config) {
//        try {
//            File file = new File(CONFIG_FILE_PATH);
//            file.getParentFile().mkdirs(); // Створюємо папку, якщо її немає
//            FileWriter writer = new FileWriter(file);
//
//            if (config.getUrl() != null && !config.getUrl().isEmpty()) {
//                writer.write("db.url=" + config.getUrl() + "\n");
//            } else {
//                writer.write("db.type=" + config.getDbType() + "\n");
//                writer.write("db.host=" + config.getHost() + "\n");
//                writer.write("db.port=" + config.getPort() + "\n");
//                writer.write("db.name=" + config.getDatabase() + "\n");
//                writer.write("db.user=" + config.getUser() + "\n");
//                writer.write("db.password=" + config.getPassword() + "\n");
//            }
//
//            writer.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
//
//
////package com.smartinvent.config;
////
////import org.springframework.stereotype.Service;
////import java.io.IOException;
////
////@Service
////public class DatabaseConfigService {
////
////    private final ConfigService configService;
////
////    public DatabaseConfigService(ConfigService configService) {
////        this.configService = configService;
////    }
////
////    public void saveDatabaseConfig(String dbType, String host, String port, String database, String user, String password, String url) {
////        if (url == null || url.isEmpty()) {
////            url = switch (dbType) {
////                case "PostgreSQL" -> "jdbc:postgresql://" + host + ":" + port + "/" + database;
////                case "MySQL" -> "jdbc:mysql://" + host + ":" + port + "/" + database;
////                case "MSSQL" -> "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + database;
////                default -> throw new RuntimeException("Невідома база даних: " + dbType);
////            };
////        }
////
////        try {
////            configService.saveDatabaseConfig(url, user, password);
////        } catch (IOException e) {
////            throw new RuntimeException("Помилка збереження конфігурації БД", e);
////        }
////    }
////
////}
