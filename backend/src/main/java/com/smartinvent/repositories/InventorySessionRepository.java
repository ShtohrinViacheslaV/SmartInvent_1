package com.smartinvent.repositories;


import com.smartinvent.models.InventorySession;
import com.smartinvent.models.InventorySessionStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface InventorySessionRepository extends JpaRepository<InventorySession, Long> {
    List<InventorySession> findByStatus(InventorySessionStatusEnum status);
    List<InventorySession> findByStatusAndStartTimeBefore(InventorySessionStatusEnum status, LocalDateTime time);

}