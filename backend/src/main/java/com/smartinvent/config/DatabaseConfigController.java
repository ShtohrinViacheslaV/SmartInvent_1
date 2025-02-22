//package com.smartinvent.config;
//
//import com.smartinvent.models.DatabaseConfig;
//import com.smartinvent.service.DatabaseConfigService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//
//@RestController
//@RequestMapping("/api/config")
//public class DatabaseConfigController {
//
//    private final DatabaseConfigService databaseConfigService;
//
//    @Autowired
//    public DatabaseConfigController(DatabaseConfigService databaseConfigService) {
//        this.databaseConfigService = databaseConfigService;
//    }
//
//    @PostMapping("/save")
//    public ResponseEntity<String> saveDatabaseConfig(@RequestBody DatabaseConfig config) {
//        boolean saved = databaseConfigService.saveConfig(config);
//        if (saved) {
//            return ResponseEntity.ok("Конфігурація збережена!");
//        } else {
//            return ResponseEntity.status(500).body("Помилка збереження конфігурації.");
//        }
//    }
//}

//package com.smartinvent.config;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/config")
//public class DatabaseConfigController {
//
//    private final DatabaseConfigService databaseConfigService;
//
//    public DatabaseConfigController(DatabaseConfigService databaseConfigService) {
//        this.databaseConfigService = databaseConfigService;
//    }
//
//
//    @PostMapping("/save")
//    public ResponseEntity<?> saveDatabaseConfig(@RequestBody DatabaseConfigRequest request) {
//        databaseConfigService.saveDatabaseConfig(
//                request.getDbType(),
//                request.getHost(),
//                request.getPort(),
//                request.getDatabase(),
//                request.getUser(),
//                request.getPassword(),
//                request.getUrl() // Передаємо URL
//        );
//        return ResponseEntity.ok().build();
//    }
//}
