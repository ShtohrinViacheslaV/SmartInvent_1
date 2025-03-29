package com.smartinvent.repositories;


import com.smartinvent.models.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ActionLogRepository interface
 */
@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}
