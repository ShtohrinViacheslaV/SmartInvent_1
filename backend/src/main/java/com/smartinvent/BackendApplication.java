package com.smartinvent;


import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

@Slf4j
@SpringBootApplication
public class BackendApplication {


    public static void main(String[] args) {
        log.info("Starting SmartInvent backend application...");
        ApplicationContext context = SpringApplication.run(BackendApplication.class, args);
        log.info("SmartInvent backend started successfully.");
//        Sentry.captureException(new RuntimeException("Manual test error 1"));
//        Sentry.captureException(new RuntimeException("Manual test error 2"));

        // Перевіряємо, чи є DataSource
        DataSource dataSource = context.getBeanProvider(DataSource.class).getIfAvailable();

        if (dataSource == null) {
            log.warn("⚠️ WARNING: No database configured. Running in limited mode.");
        } else {
            log.info("✅ Database connection established successfully.");
        }
    }
}
