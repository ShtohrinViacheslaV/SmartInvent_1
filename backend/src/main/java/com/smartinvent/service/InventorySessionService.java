package com.smartinvent.service;


import com.smartinvent.models.Employee;
import com.smartinvent.models.InventorySession;
import com.smartinvent.models.InventorySessionStatusEnum;
import com.smartinvent.repositories.InventorySessionRepository;
import com.smartinvent.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InventorySessionService {

    private final InventorySessionRepository sessionRepository;
    private final EmployeeRepository employeeRepository;
    private final InventoryResultService inventoryResultService;
    private final SnapshotExportService snapshotExportService;


    // Створення сесії інвентаризації
    public InventorySession createSession(InventorySession inventorySession) {
        // Знайдемо співробітника за id
        Employee employee = employeeRepository.findById(inventorySession.getEmployee().getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDateTime now = LocalDateTime.now();
        InventorySessionStatusEnum status;

        // Встановлюємо статус залежно від того, коли починається сесія
        if (inventorySession.getStartTime() == null) {
            inventorySession.setStartTime(now);
            status = InventorySessionStatusEnum.ACTIVE;
        } else if (inventorySession.getStartTime().isAfter(now)) {
            status = InventorySessionStatusEnum.PLANNED;
        } else {
            status = InventorySessionStatusEnum.ACTIVE;
        }

        inventorySession.setEmployee(employee);
        inventorySession.setStatus(status);

        // ✅ Спочатку зберігаємо сесію в базу
        InventorySession savedSession = sessionRepository.save(inventorySession);

        // ✅ Тільки після цього викликаємо сервіси
        if (status == InventorySessionStatusEnum.ACTIVE) {
            inventoryResultService.initializeResultsForSession(savedSession);
            snapshotExportService.exportProductSnapshot(savedSession);
        }

        return savedSession;
    }


    // Отримання всіх сесій
    public List<InventorySession> getAllSessions() {
        activatePlannedSessionsIfNeeded();
        completeFinishedSessionsIfNeeded();

        return sessionRepository.findAll();
    }

    // Отримання деталей сесії за id
    public InventorySession getSessionDetails(Long sessionId) {
        activatePlannedSessionsIfNeeded();
        completeFinishedSessionsIfNeeded();

        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    // Отримання всіх активних сесій
    public List<InventorySession> getActiveSessions() {
        activatePlannedSessionsIfNeeded();
        completeFinishedSessionsIfNeeded();

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

    @Transactional
    public void activatePlannedSessionsIfNeeded() {
        LocalDateTime now = LocalDateTime.now();

        List<InventorySession> sessionsToActivate =
                sessionRepository.findByStatusAndStartTimeBefore(InventorySessionStatusEnum.PLANNED, now);

        for (InventorySession session : sessionsToActivate) {
            session.setStatus(InventorySessionStatusEnum.ACTIVE);
            inventoryResultService.initializeResultsForSession(session);
            snapshotExportService.exportProductSnapshot(session);
            sessionRepository.save(session);

        }

        if (!sessionsToActivate.isEmpty()) {
            System.out.println("Activated " + sessionsToActivate.size() + " session(s)");
        }
    }

    @Transactional
    public void completeFinishedSessionsIfNeeded() {
        LocalDateTime now = LocalDateTime.now();

        // Знайти всі активні сесії, які мають endTime і він уже минув
        List<InventorySession> sessionsToComplete = sessionRepository.findAll().stream()
                .filter(session ->
                        session.getStatus() == InventorySessionStatusEnum.ACTIVE &&
                                session.getEndTime() != null &&
                                session.getEndTime().isBefore(now)
                )
                .collect(Collectors.toList());

        for (InventorySession session : sessionsToComplete) {
            session.setStatus(InventorySessionStatusEnum.COMPLETED);
            sessionRepository.save(session);
        }

        if (!sessionsToComplete.isEmpty()) {
            System.out.println("Completed " + sessionsToComplete.size() + " session(s)");
        }
    }


}
