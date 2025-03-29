package com.smartinvent.controller;

import com.smartinvent.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Клас-контроллер для обробки запитів, пов'язаних з резервними копіями
 */
@RestController
@RequestMapping("/api/backup")
public class BackupController {

    /**
     * Об'єкт сервісу для роботи з резервними копіями
     */
    @Autowired
    private BackupService backupService;

    /**
     * Метод для створення резервної копії
     *
     * @return - відповідь про результат створення резервної копії
     */
    @PostMapping("/create")
    public ResponseEntity<String> createBackup() {
        try {
            backupService.createBackup();
            return ResponseEntity.ok("Backup created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create backup: " + e.getMessage());
        }
    }

    /**
     * Метод для відновлення резервної копії
     *
     * @param backupFile - файл резервної копії
     * @return - відповідь про результат відновлення резервної копії
     */
    @PostMapping("/restore")
    public ResponseEntity<String> restoreBackup(@RequestParam String backupFile) {
        try {
            backupService.restoreBackup(backupFile);
            return ResponseEntity.ok("Backup restored successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to restore backup: " + e.getMessage());
        }
    }

    /**
     * Метод для експортування даних
     *
     * @return - відповідь про результат експортування даних
     */
    @PostMapping("/export")
    public ResponseEntity<String> exportData() {
        try {
            backupService.exportData();
            return ResponseEntity.ok("Data exported successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to export data: " + e.getMessage());
        }
    }

    /**
     * Метод для імпортування даних
     *
     * @param importFile - файл для імпортування даних
     * @return - відповідь про результат імпортування даних
     */
    @PostMapping("/import")
    public ResponseEntity<String> importData(@RequestParam String importFile) {
        try {
            backupService.importData(importFile);
            return ResponseEntity.ok("Data imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to import data: " + e.getMessage());
        }
    }
}
