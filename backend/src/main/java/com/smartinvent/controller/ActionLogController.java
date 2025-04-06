package com.smartinvent.controller;

import com.smartinvent.models.ActionLog;
import com.smartinvent.service.ActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/action-logs")
public class ActionLogController {

    @Autowired
    private ActionLogService actionLogService;

    @GetMapping
    public List<ActionLog> getAllActionLogs() {
        return actionLogService.getAllActionLogs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionLog> getActionLogById(@PathVariable Long id) {
        Optional<ActionLog> actionLog = actionLogService.getActionLogById(id);
        return actionLog.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ActionLog createActionLog(@RequestBody ActionLog actionLog) {
        return actionLogService.saveActionLog(actionLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActionLog(@PathVariable Long id) {
        actionLogService.deleteActionLog(id);
        return ResponseEntity.noContent().build();
    }
}

