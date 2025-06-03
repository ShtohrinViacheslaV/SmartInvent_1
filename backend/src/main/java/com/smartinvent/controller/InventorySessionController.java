package com.smartinvent.controller;


import com.smartinvent.dto.InventoryProductResultDto;
import com.smartinvent.models.InventoryProductStatusEnum;
import com.smartinvent.models.InventorySession;
import com.smartinvent.service.InventorySessionService;
import com.smartinvent.service.SnapshotExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/inventory/session")
@RequiredArgsConstructor
public class InventorySessionController {

    private final InventorySessionService sessionService;
    private final SnapshotExportService backupService;

    // Створення сесії інвентаризації
    @PostMapping("/create")
    public ResponseEntity<InventorySession> createSession(@RequestBody InventorySession inventorySession) {
        InventorySession session = sessionService.createSession(inventorySession);
        return ResponseEntity.ok(session);
    }

    // Отримання всіх сесій
    @GetMapping("/all")
    public ResponseEntity<List<InventorySession>> getAllSessions() {
        List<InventorySession> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    // Отримання деталей сесії
    @GetMapping("/{sessionId}")
    public ResponseEntity<InventorySession> getSessionDetails(@PathVariable Long sessionId) {
        InventorySession session = sessionService.getSessionDetails(sessionId);
        return ResponseEntity.ok(session);
    }

    // Отримання всіх активних сесій
    @GetMapping("/active")
    public ResponseEntity<List<InventorySession>> getActiveSessions() {
        List<InventorySession> sessions = sessionService.getActiveSessions();
        return ResponseEntity.ok(sessions);
    }

    // Завершення сесії
    @PutMapping("/complete/{sessionId}")
    public ResponseEntity<InventorySession> completeSession(@PathVariable Long sessionId) {
        InventorySession session = sessionService.completeSession(sessionId);
        return ResponseEntity.ok(session);
    }

    @PutMapping("/cancel/{sessionId}")
    public ResponseEntity<InventorySession> cancelSession(@PathVariable Long sessionId) {
        InventorySession session = sessionService.cancelSession(sessionId);
        return ResponseEntity.ok(session);
    }


    @PostMapping("/{sessionId}/complete-and-backup")
    public ResponseEntity<String> completeAndBackupSession(@PathVariable Long sessionId) {
        try {
            backupService.completeSessionAndBackup(sessionId);
            return ResponseEntity.ok("Сесію успішно завершено та зроблено бекап.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Помилка при завершенні сесії: " + e.getMessage());
        }
    }



}