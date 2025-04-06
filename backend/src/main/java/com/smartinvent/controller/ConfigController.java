package com.smartinvent.controller;

import com.smartinvent.config.ConfigService;
import com.smartinvent.models.DatabaseConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {
    private final ConfigService configService;

    @GetMapping
    public ResponseEntity<DatabaseConfig> getConfig() {
        return ResponseEntity.ok(configService.getDatabaseConfig());
    }

    @PostMapping
    public ResponseEntity<Void> updateConfig(@RequestBody DatabaseConfig newConfig) {
        configService.setDatabaseConfig(newConfig);
        configService.saveConfig();
        return ResponseEntity.ok().build();
    }
}








//
//import com.smartinvent.config.ConfigService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.concurrent.CompletableFuture;
//
//@RestController
//@RequestMapping("/config")
//public class ConfigController {
//
//    @Autowired
//    private ConfigService configService;
//
//    @PostMapping("/save")
//    public ResponseEntity<String> saveConfig(@RequestBody ConfigService.DatabaseConfig config) {
//        boolean success = configService.saveConfig(config);
//        if (success) {
//            restartApplication();
//            return ResponseEntity.ok("Конфігурація збережена! Сервер перезапускається...");
//        }
//        return ResponseEntity.badRequest().body("Помилка збереження конфігурації");
//    }
//
//    @GetMapping("/load")
//    public ResponseEntity<ConfigService.DatabaseConfig> loadConfig() {
//        ConfigService.DatabaseConfig config = configService.loadConfig();
//        return config != null ? ResponseEntity.ok(config) :
//                ResponseEntity.badRequest().body(null);
//    }
//
//    @Async
//    public void restartApplication() {
//        CompletableFuture.runAsync(() -> {
//            try {
//                System.out.println("🔄 Перезапуск сервера...");
//                Thread.sleep(2000); // Затримка перед перезапуском
//                System.exit(0);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//}


//package com.smartinvent.controller;
//
//import com.smartinvent.config.ConfigService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/config")
//public class ConfigController {
//
//    @Autowired
//    private ConfigService configService;
//
//    @PostMapping("/save")
//    public ResponseEntity<String> saveConfig(@RequestBody ConfigService.DatabaseConfig config) {
//        boolean success = configService.saveConfig(config);
//        return success ? ResponseEntity.ok("Конфігурація збережена!") :
//                ResponseEntity.badRequest().body("Помилка збереження конфігурації");
//    }
//
//    @GetMapping("/load")
//    public ResponseEntity<ConfigService.DatabaseConfig> loadConfig() {
//        ConfigService.DatabaseConfig config = configService.loadConfig();
//        return config != null ? ResponseEntity.ok(config) :
//                ResponseEntity.badRequest().body(null);
//    }
//}
