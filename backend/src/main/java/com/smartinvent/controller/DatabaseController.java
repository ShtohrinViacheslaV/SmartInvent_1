package com.smartinvent.controller;


import com.smartinvent.models.DatabaseConfig;
import com.smartinvent.service.DatabaseInitializationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class DatabaseController {

    private final DatabaseInitializationService databaseService;


    @PostMapping("/testConnection")
    public ResponseEntity<?> testDbConnection(@RequestBody DatabaseConfig config) {
        log.info("Отримано запит testConnection з конфігурацією: {}", config);
        return ResponseEntity.ok("Connection successful!");
    }


    @PostMapping("/setupDatabase")
    public ResponseEntity<String> setupDatabase() {
        databaseService.initializeDatabase();
        return ResponseEntity.ok("✅ База даних перевірена та ініціалізована!");
    }

    @PostMapping("/checkTables")
    public ResponseEntity<Boolean> checkTables() {
        boolean tablesExist = databaseService.checkTables();
        return ResponseEntity.ok(tablesExist);
    }
}





//
//
//package com.smartinvent.controller;
//
//import com.smartinvent.models.DatabaseConfig;
//import com.smartinvent.service.DatabaseService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class DatabaseController {
//
//    private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);
//
//    private final DatabaseService databaseService;
//
//    @Autowired
//    public DatabaseController(DatabaseService databaseService) {
//        this.databaseService = databaseService;
//    }
//
//    @PostMapping("/testConnection")
//    public ResponseEntity<?> testDbConnection(@RequestBody DatabaseConfig config) {
//        logger.info("Отримано запит testConnection з конфігурацією: {}", config);
//        return ResponseEntity.ok("Connection successful!");
//    }
//
//    @PostMapping("/initializeDatabase")
//    public ResponseEntity<?> initializeDatabase() {
//        try {
//            databaseService.initializeDatabase();
//            return ResponseEntity.ok("Database initialized successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Database initialization failed: " + e.getMessage());
//        }
//    }
//}

