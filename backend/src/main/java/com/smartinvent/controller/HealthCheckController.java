package com.smartinvent.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@Slf4j
@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private final ApplicationContext context;

    @Autowired
    public HealthCheckController(ApplicationContext context) {
        this.context = context;
    }

    @GetMapping
    public String checkHealth() {
        DataSource dataSource = context.getBeanProvider(DataSource.class).getIfAvailable();

        if (dataSource == null) {
            log.warn("⚠️ WARNING: No database configured.");
            return "✅ Server is running, but database is NOT connected!";
        } else {
            log.info("✅ Server and database are connected.");
            return "✅ Server is running, and database is connected!";
        }
    }
}
