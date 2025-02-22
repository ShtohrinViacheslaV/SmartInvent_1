//package com.smartinvent.service;
//
//import com.smartinvent.models.ActionLog;
//import com.smartinvent.models.AuditLog;
//import com.smartinvent.repositories.AuditLogRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class AuditLogService {
//
//    @Autowired
//    private AuditLogRepository auditLogRepository;
//
//    public AuditLog createAuditLog(AuditLog auditLog) {
//        return auditLogRepository.save(auditLog);
//    }
//
//    public List<AuditLog> getAllAuditLogs() {
//        return auditLogRepository.findAll();
//    }
//
//    public Optional<AuditLog>  getAuditLogById(Long id) {
//        return auditLogRepository.findById(id);
//                //.orElseThrow(() -> new RuntimeException("AuditLog not found with id: " + id));
//    }
//
//    public AuditLog saveAuditLog(AuditLog auditLog) {
//        return auditLogRepository.save(auditLog);
//    }
//
//
//    public void deleteAuditLog(Long id) {
//        AuditLog auditLog = auditLogRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("AuditLog not found with id: " + id));
//        auditLogRepository.delete(auditLog);
//    }
//}
