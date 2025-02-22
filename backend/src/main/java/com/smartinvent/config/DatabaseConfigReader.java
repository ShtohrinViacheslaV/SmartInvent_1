//package com.smartinvent.config;
//
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PropertiesLoaderUtils;
//import java.io.IOException;
//import java.util.Properties;
//
//public class DatabaseConfigReader {
//    private static final String CONFIG_FILE = "config/application-company.properties";
//
//    public static Properties getDatabaseProperties() {
//        Properties properties = new Properties();
//        try {
//            Resource resource = new ClassPathResource(CONFIG_FILE);
//            properties = PropertiesLoaderUtils.loadProperties(resource);
//        } catch (IOException e) {
//            throw new RuntimeException("Помилка завантаження конфігурації БД", e);
//        }
//        return properties;
//    }
//}


//package com.smartinvent.config;
//
//import android.content.Context;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Properties;
//
//public class DatabaseConfigReader {
//    private static final String CONFIG_FILE_NAME = "application-company.properties";
//
//    public static Properties getDatabaseProperties(Context context) {
//        Properties properties = new Properties();
//        try {
//            File configFile = new File(context.getFilesDir(), CONFIG_FILE_NAME);
//            if (!configFile.exists()) {
//                throw new IOException("Файл конфігурації не знайдено: " + CONFIG_FILE_NAME);
//            }
//            FileInputStream fis = new FileInputStream(configFile);
//            properties.load(fis);
//            fis.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return properties;
//    }
//}
