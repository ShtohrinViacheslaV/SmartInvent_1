//package com.smartinvent.controller;
//
//import com.smartinvent.models.AuditLog;
//import com.smartinvent.service.AuditLogService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/audit-logs")
//public class AuditLogController {
//
//    @Autowired
//    private AuditLogService auditLogService;
//
//    // Отримати всі записи аудиту
//    @GetMapping
//    public List<AuditLog> getAllAuditLogs() {
//        return auditLogService.getAllAuditLogs();
//    }
//
//    // Отримати запис аудиту за ID
//    @GetMapping("/{id}")
//    public ResponseEntity<AuditLog> getAuditLogById(@PathVariable Long id) {
//        Optional<AuditLog> auditLog = auditLogService.getAuditLogById(id);
//        return auditLog.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Створити новий запис аудиту
//    @PostMapping
//    public AuditLog createAuditLog(@RequestBody AuditLog auditLog) {
//        return auditLogService.saveAuditLog(auditLog);
//    }
//
//    // Видалити запис аудиту за ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAuditLog(@PathVariable Long id) {
//        auditLogService.deleteAuditLog(id);
//        return ResponseEntity.noContent().build();
//    }
//}
