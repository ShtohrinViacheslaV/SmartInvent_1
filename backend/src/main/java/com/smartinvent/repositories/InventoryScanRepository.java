package com.smartinvent.repositories;

import com.smartinvent.models.InventoryScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryScanRepository extends JpaRepository<InventoryScan, Long> {
    List<InventoryScan> findByProductId(Long productId);
}
