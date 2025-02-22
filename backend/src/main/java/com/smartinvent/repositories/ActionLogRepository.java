package com.smartinvent.repositories;


import com.smartinvent.models.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}
