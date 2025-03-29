package com.smartinvent.controller;

import com.smartinvent.models.ActionLog;
import com.smartinvent.service.ActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Клас-контроллер для обробки запитів, пов'язаних з логами дій
 */
@RestController
@RequestMapping("/api/action-logs")
public class ActionLogController {
    /**
     * Об'єкт сервісу для роботи з логами дій
     */
    @Autowired
    private ActionLogService actionLogService;

    /**
     * Метод для отримання всіх логів дій
     *
     * @return - список логів дій
     */
    @GetMapping
    public List<ActionLog> getAllActionLogs() {
        return actionLogService.getAllActionLogs();
    }

    /**
     * Метод для отримання логу дій по його ідентифікатору
     *
     * @param id - ідентифікатор логу дій
     * @return - лог дій
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActionLog> getActionLogById(@PathVariable Long id) {
        Optional<ActionLog> actionLog = actionLogService.getActionLogById(id);
        return actionLog.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Метод для створення нового логу дій
     *
     * @param actionLog - об'єкт логу дій
     * @return - створений лог дій
     */
    @PostMapping
    public ActionLog createActionLog(@RequestBody ActionLog actionLog) {
        return actionLogService.saveActionLog(actionLog);
    }

    /**
     * Метод для оновлення інформації про лог дій
     *
     * @param id        - ідентифікатор логу дій
     * @return - оновлений лог дій
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActionLog(@PathVariable Long id) {
        actionLogService.deleteActionLog(id);
        return ResponseEntity.noContent().build();
    }
}

