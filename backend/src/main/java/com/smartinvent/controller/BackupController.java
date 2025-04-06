package com.smartinvent.controller;

import com.smartinvent.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/backup")
public class BackupController {

    @Autowired
    private BackupService backupService;

    // Створення резервної копії
    @PostMapping("/create")
    public ResponseEntity<String> createBackup() {
        try {
            backupService.createBackup();
            return ResponseEntity.ok("Backup created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create backup: " + e.getMessage());
        }
    }

    // Відновлення з резервної копії
    @PostMapping("/restore")
    public ResponseEntity<String> restoreBackup(@RequestParam String backupFile) {
        try {
            backupService.restoreBackup(backupFile);
            return ResponseEntity.ok("Backup restored successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to restore backup: " + e.getMessage());
        }
    }

    // Експорт даних у файл
    @PostMapping("/export")
    public ResponseEntity<String> exportData() {
        try {
            backupService.exportData();
            return ResponseEntity.ok("Data exported successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to export data: " + e.getMessage());
        }
    }

    // Імпорт даних з файлу
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
