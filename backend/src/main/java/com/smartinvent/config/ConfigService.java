package com.smartinvent.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartinvent.models.DatabaseConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class ConfigService {
    private static final String CONFIG_PATH = "config.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Getter @Setter
    private DatabaseConfig databaseConfig;

    public ConfigService() {
        loadConfig();
    }

    @PostConstruct
    public void loadConfig() {
        try {
            File configFile = new File(CONFIG_PATH);
            if (configFile.exists()) {
                databaseConfig = objectMapper.readValue(configFile, DatabaseConfig.class);
            } else {
                databaseConfig = new DatabaseConfig();
                saveConfig();
            }

            // Перевірка, чи всі поля ініціалізовані
            if (databaseConfig.getHost() == null) databaseConfig.setHost("");
            if (databaseConfig.getPort() == null) databaseConfig.setPort("");
            if (databaseConfig.getUsername() == null) databaseConfig.setUsername("");
            if (databaseConfig.getPassword() == null) databaseConfig.setPassword("");
            if (databaseConfig.getUrl() == null) databaseConfig.setUrl("");


            log.info("Config loaded successfully: {}", databaseConfig);
        } catch (IOException e) {
            log.error("Failed to load config", e);
        }
    }



    public DatabaseConfig getConfig() {
        return databaseConfig;
    }

    public void updateConfig(DatabaseConfig newConfig) {
        this.databaseConfig = newConfig;
        saveConfig();
    }

        public boolean isConfigFileExists() {
        return new File(CONFIG_PATH).exists();
    }


    public void saveConfig() {
        try {
            objectMapper.writeValue(new File(CONFIG_PATH), databaseConfig);
            log.info("Конфігурація збережена успішно.");
        } catch (IOException e) {
            log.error("Помилка збереження конфігурації", e);
        }
    }

    public void saveConfig(DatabaseConfig config) {
        this.databaseConfig = config;
        saveConfig();
    }



}


//    @PostConstruct
//    public void loadConfig() {
//        try {
//            File configFile = new File(CONFIG_PATH);
//            if (configFile.exists()) {
//                databaseConfig = objectMapper.readValue(configFile, DatabaseConfig.class);
//            } else {
//                databaseConfig = new DatabaseConfig();
//                databaseConfig.setUrl("");  // Запобігаємо NullPointerException
//                databaseConfig.setUsername("");
//                databaseConfig.setPassword("");
//                saveConfig();
//            }
//            log.info("Config loaded successfully: {}", databaseConfig);
//        } catch (IOException e) {
//            log.error("Failed to load config", e);
//        }
//    }

//package com.smartinvent.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.smartinvent.models.DatabaseConfig;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.stereotype.Service;
//import javax.annotation.PostConstruct;
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//
//@Service
//@Getter
//@Setter
//public class ConfigService {
//    private static final String CONFIG_PATH = "config/config.json";
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private DatabaseConfig databaseConfig;
//
//    @PostConstruct
//    public void loadConfig() {
//        File configFile = new File(CONFIG_PATH);
//        if (configFile.exists()) {
//            try {
//                databaseConfig = objectMapper.readValue(configFile, DatabaseConfig.class);
//            } catch (IOException e) {
//                throw new RuntimeException("Error loading config", e);
//            }
//        } else {
//            databaseConfig = new DatabaseConfig();
//        }
//    }
//
//    public void saveConfig() {
//        try {
//            objectMapper.writeValue(new File(CONFIG_PATH), databaseConfig);
//        } catch (IOException e) {
//            throw new RuntimeException("Error saving config", e);
//        }
//    }
//}


//package com.smartinvent.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Service
//public class ConfigService {
//
//    private static final String CONFIG_PATH = "config/config.json";
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public static class DatabaseConfig {
//        private String db_url;
//        private String db_username;
//        private String db_password;
//
//        // Геттери та сеттери
//        public String getDb_url() { return db_url; }
//        public void setDb_url(String db_url) { this.db_url = db_url; }
//        public String getDb_username() { return db_username; }
//        public void setDb_username(String db_username) { this.db_username = db_username; }
//        public String getDb_password() { return db_password; }
//        public void setDb_password(String db_password) { this.db_password = db_password; }
//    }
//
//
//    public boolean isConfigFileExists() {
//        return new File(CONFIG_PATH).exists();
//    }
//
//
//    public void saveDatabaseConfig(String url, String user, String password) throws IOException {
//        Map<String, String> config = new HashMap<>();
//        config.put("dbUrl", url);
//        config.put("dbUser", user);
//        config.put("dbPassword", password);
//
//        objectMapper.writeValue(new File(CONFIG_PATH), config);
//        System.out.println("✅ Конфігурація бази даних успішно збережена!");
//    }
//
//    public boolean saveConfig(DatabaseConfig config) {
//        try {
//            objectMapper.writeValue(new File(CONFIG_PATH), config);
//            log.info("✅ Конфігурація успішно збережена у файл {}", CONFIG_PATH);
//            return true;
//        } catch (IOException e) {
//            log.error("❌ Помилка збереження конфігурації: {}", e.getMessage());
//            return false;
//        }
//    }
//
//    public DatabaseConfig loadConfig() {
//        File configFile = new File(CONFIG_PATH);
//        if (!configFile.exists()) {
//            log.warn("⚠️ Файл конфігурації {} не знайдено", CONFIG_PATH);
//            return null;
//        }
//        try {
//            return objectMapper.readValue(configFile, DatabaseConfig.class);
//        } catch (IOException e) {
//            log.error("❌ Помилка завантаження конфігурації: {}", e.getMessage());
//            return null;
//        }
//    }
//}
