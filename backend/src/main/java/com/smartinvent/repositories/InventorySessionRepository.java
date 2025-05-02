package com.smartinvent.repositories;


import com.smartinvent.models.InventoryProductStatus;
import com.smartinvent.models.InventoryResult;
import com.smartinvent.models.InventorySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InventorySessionRepository extends JpaRepository<InventorySession, Long> {
    List<InventorySession> findByInventorySessionStatusName(String statusName);
}