package com.smartinvent.repositories;


import com.smartinvent.models.InventorySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InventorySessionRepository extends JpaRepository<InventorySession, Long> {
    List<InventorySession> findByStatus_Name(String statusName);

}