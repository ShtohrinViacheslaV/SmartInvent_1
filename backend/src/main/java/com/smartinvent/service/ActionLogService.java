package com.smartinvent.service;

import com.smartinvent.models.ActionLog;
import com.smartinvent.repositories.ActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * ActionLogService клас для роботи з ActionLog об'єктами.
 */
@Service
public class ActionLogService {

    /**
     * ActionLogRepository об'єкт для роботи з ActionLog об'єктами.
     *
     * @see com.smartinvent.repositories.ActionLogRepository
     */
    @Autowired
    private ActionLogRepository actionLogRepository;

    /**
     * Метод для отримання всіх ActionLog об'єктів.
     *
     * @return список всіх ActionLog об'єктів
     */
    public List<ActionLog> getAllActionLogs() {
        return actionLogRepository.findAll();
    }

    /**
     * Метод для отримання ActionLog об'єкта за його id.
     *
     * @param id id ActionLog об'єкта
     * @return ActionLog об'єкт
     */
    public Optional<ActionLog> getActionLogById(Long id) {
        return actionLogRepository.findById(id);
    }

    /**
     * Метод для збереження ActionLog об'єкта.
     *
     * @param actionLog ActionLog об'єкт
     * @return збережений ActionLog об'єкт
     */
    public ActionLog saveActionLog(ActionLog actionLog) {
        return actionLogRepository.save(actionLog);
    }

    /**
     * Метод для видалення ActionLog об'єкта за його id.
     *
     * @param id id ActionLog об'єкта
     */
    public void deleteActionLog(Long id) {
        actionLogRepository.deleteById(id);
    }
}
