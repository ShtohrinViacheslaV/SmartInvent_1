package com.smartinvent.service;


import com.smartinvent.models.Employee;
import com.smartinvent.models.InventorySession;
import com.smartinvent.models.InventorySessionStatusEnum;
import com.smartinvent.repositories.InventorySessionRepository;
import com.smartinvent.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;



@Service
@RequiredArgsConstructor
public class InventorySessionService {

    private final InventorySessionRepository sessionRepository;
    private final EmployeeRepository employeeRepository;


    // Створення сесії інвентаризації
    public InventorySession createSession(InventorySession inventorySession) {
        // Знайдемо співробітника за id
        Employee employee = employeeRepository.findById(inventorySession.getEmployee().getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Знайдемо статус сесії
        InventorySessionStatusEnum status = InventorySessionStatusEnum.ACTIVE;


        // Створимо нову сесію інвентаризації
        inventorySession.setEmployee(employee);
        inventorySession.setStatus(status);
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
        return sessionRepository.findByStatus(InventorySessionStatusEnum.ACTIVE);
    }


    // Завершення сесії інвентаризації
    public InventorySession completeSession(Long sessionId) {
        InventorySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        InventorySessionStatusEnum completedStatus = InventorySessionStatusEnum.COMPLETED;
        session.setStatus(completedStatus); // Заміна статусу на Enum
        session.setEndTime(LocalDateTime.now());

        return sessionRepository.save(session);
    }


}
