package com.smartinvent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

@Slf4j
@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        final ApplicationContext context = SpringApplication.run(BackendApplication.class, args);

        final DataSource dataSource = context.getBeanProvider(DataSource.class).getIfAvailable();

        if (dataSource == null) {
            log.warn("⚠️ WARNING: No database configured. Running in limited mode.");
        } else {
            log.info("✅ Database connection established successfully.");
        }
    }
}