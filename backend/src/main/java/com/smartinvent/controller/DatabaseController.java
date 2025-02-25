package com.smartinvent.controller;

import com.smartinvent.models.DatabaseConfig;
import com.smartinvent.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/db")
public class DatabaseController {
    @Autowired
    private DatabaseService databaseService;

    @PostMapping("/test")
    public ResponseEntity<?> testConnection(@RequestBody DatabaseConfig config) {
        if (databaseService.testConnection(config)) {
            return ResponseEntity.ok("Підключення успішне");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Помилка підключення");
    }

    @PostMapping("/init")
    public ResponseEntity<?> initializeDatabase() {
        databaseService.initializeDatabase();
        return ResponseEntity.ok("База даних ініціалізована");
    }
}