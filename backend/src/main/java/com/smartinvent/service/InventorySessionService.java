package com.smartinvent.service;


import com.smartinvent.dto.InventorySessionProductDTO;
import com.smartinvent.models.Employee;
import com.smartinvent.models.InventoryResult;
import com.smartinvent.models.InventorySession;
import com.smartinvent.models.InventorySessionStatus;
import com.smartinvent.repositories.InventorySessionRepository;
import com.smartinvent.repositories.InventorySessionStatusRepository;
import com.smartinvent.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InventorySessionService {

    private final InventorySessionRepository sessionRepository;
    private final EmployeeRepository employeeRepository;
    private final InventorySessionStatusRepository statusRepository;

    // Створення сесії інвентаризації
    public InventorySession createSession(InventorySession inventorySession) {
        // Знайдемо співробітника за id
        Employee employee = employeeRepository.findById(inventorySession.getEmployee().getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Знайдемо статус сесії
        InventorySessionStatus status = statusRepository.findByName("ACTIVE")
                .orElseThrow(() -> new RuntimeException("Session status ACTIVE not found"));

        // Створимо нову сесію інвентаризації
        inventorySession.setEmployee(employee);
        inventorySession.setInventorySessionStatus(status);
        inventorySession.setStartTime(LocalDateTime.now());

        return sessionRepository.save(inventorySession);
    }

    // Отримання всіх сесій
    public List<InventorySession> getAllSessions() {
        return sessionRepository.findAll();
    }

    // Отримання деталей сесії за id
    public InventorySession getSessionDetails(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    // Отримання всіх активних сесій
    public List<InventorySession> getActiveSessions() {
        return sessionRepository.findByInventorySessionStatusName("ACTIVE");
    }


    // Завершення сесії інвентаризації
    public InventorySession completeSession(Long sessionId) {
        InventorySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        InventorySessionStatus completedStatus = statusRepository.findByName("COMPLETED")
                .orElseThrow(() -> new RuntimeException("Session status COMPLETED not found"));

        session.setInventorySessionStatus(completedStatus);
        session.setEndTime(LocalDateTime.now());

        return sessionRepository.save(session);
    }

}
