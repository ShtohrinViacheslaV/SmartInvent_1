package com.smartinvent.repositories;


import com.smartinvent.models.InventoryProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryProductStatusRepository extends JpaRepository<InventoryProductStatus, Long> {
    Optional<InventoryProductStatus> findByName(String name);
}
