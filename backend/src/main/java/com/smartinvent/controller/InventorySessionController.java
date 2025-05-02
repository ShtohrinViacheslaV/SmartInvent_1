package com.smartinvent.controller;


import com.smartinvent.models.InventorySession;
import com.smartinvent.service.InventorySessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/inventory/session")
@RequiredArgsConstructor
public class InventorySessionController {

    private final InventorySessionService sessionService;

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
}