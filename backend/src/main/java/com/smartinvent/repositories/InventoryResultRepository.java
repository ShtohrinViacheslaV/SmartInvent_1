package com.smartinvent.repositories;


import com.smartinvent.models.InventoryProductStatusEnum;
import com.smartinvent.models.InventoryResult;
import com.smartinvent.models.InventorySession;
import com.smartinvent.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InventoryResultRepository extends JpaRepository<InventoryResult, Long> {
    // Перевірка, чи існує запис за сесією та продуктом
    Optional<InventoryResult> findBySessionIdAndProductProductId(Long sessionId, Long productId);

    // Отримання результатів за сесією і статусом
    List<InventoryResult> findBySessionIdAndStatus(Long sessionId, InventoryProductStatusEnum status);

    // Отримання результату для конкретного товару в сесії
    Optional<InventoryResult> findBySessionAndProduct(InventorySession session, Product product);

    // Отримання результатів за сесією
    List<InventoryResult> findBySession(InventorySession session);

}




//    Optional<InventoryResult> findBySessionIdAndProductId(Long sessionId, Long productId);
//
//    Optional<InventoryResult> findByInventorySessionAndProduct(InventorySession session, Product product);
//
//
//    List<InventoryResult> findBySessionId(Long sessionId);
//
//    List<InventoryResult> findByStatusName(String statusName);
//
//    // Можна додати додаткові методи для пошуку за іншими критеріями
//    List<InventoryResult> findBySessionIdAndStatusName(Long sessionId, String statusName);
//
//
//    // Отримання результатів інвентаризації для конкретної сесії
//    List<InventoryResult> findByInventorySession(InventorySession inventorySession);
//
//    // Отримання результатів інвентаризації для конкретного товару в сесії
//    List<InventoryResult> findByInventorySessionAndProduct(InventorySession inventorySession, Product product);
