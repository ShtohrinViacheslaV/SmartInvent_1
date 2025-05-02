package com.smartinvent.repositories;


import com.smartinvent.models.InventorySessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface InventorySessionStatusRepository extends JpaRepository<InventorySessionStatus, Long> {
    Optional<InventorySessionStatus> findByName(String name);
}
