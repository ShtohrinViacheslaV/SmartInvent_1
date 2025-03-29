package com.smartinvent;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

/**
 * BackendApplication class
 */
@Slf4j
@SpringBootApplication
public class BackendApplication {

    /**
     * Main метод для запуску додатку
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BackendApplication.class, args);

        // Перевіряємо, чи є DataSource
        DataSource dataSource = context.getBeanProvider(DataSource.class).getIfAvailable();

        // Якщо DataSource не існує, виводимо повідомлення про помилку
        if (dataSource == null) {
            log.warn("⚠️ WARNING: No database configured. Running in limited mode.");
        } else {
            log.info("✅ Database connection established successfully.");
        }
    }
}


//@SpringBootApplication
//@EnableJpaRepositories("com.smartinvent.repositories")
//public class BackendApplication {
//    public static void main(String[] args) {
//        SpringApplication.run(BackendApplication.class, args);
//    }
//}


//package com.smartinvent;
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//
//@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "SmartInvent API", version = "1.0", description = "API для інвентаризації"))
//@EnableJpaRepositories(basePackages = "com.smartinvent.repositories")
//@EntityScan("com.smartinvent.models")
//public class BackendApplication {
//
//
//    public static void main(String[] args) {
//        SpringApplication.run(BackendApplication.class, args);
//
//    }
//
//}
