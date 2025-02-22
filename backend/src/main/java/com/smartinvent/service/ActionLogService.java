package com.smartinvent.service;

import com.smartinvent.models.ActionLog;
import com.smartinvent.repositories.ActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActionLogService {

    @Autowired
    private ActionLogRepository actionLogRepository;

    public List<ActionLog> getAllActionLogs() {
        return actionLogRepository.findAll();
    }

    public Optional<ActionLog> getActionLogById(Long id) {
        return actionLogRepository.findById(id);
    }

    public ActionLog saveActionLog(ActionLog actionLog) {
        return actionLogRepository.save(actionLog);
    }

    public void deleteActionLog(Long id) {
        actionLogRepository.deleteById(id);
    }
}
