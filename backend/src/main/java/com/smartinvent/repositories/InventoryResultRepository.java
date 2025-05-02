package com.smartinvent.repositories;


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

    Optional<InventoryResult> findBySessionIdAndProductId(Long sessionId, Long productId);

    List<InventoryResult> findBySessionId(Long sessionId);

    List<InventoryResult> findByStatusName(String statusName);

    // Можна додати додаткові методи для пошуку за іншими критеріями
    List<InventoryResult> findBySessionIdAndStatusName(Long sessionId, String statusName);


    // Отримання результатів інвентаризації для конкретної сесії
    List<InventoryResult> findByInventorySession(InventorySession inventorySession);

    // Отримання результатів інвентаризації для конкретного товару в сесії
    List<InventoryResult> findByInventorySessionAndProduct(InventorySession inventorySession, Product product);


}

//@Repository
//public interface InventoryResultRepository extends JpaRepository<InventoryResult, Long> {
//
//    List<InventoryResult> findByInventorySession_InventorySessionId(Long sessionId);
//
//    List<InventoryResult> findByInventorySessionId(Long inventorySessionId);
//
//    boolean existsByInventorySession_InventorySessionIdAndProduct_ProductId(Long sessionId, Long productId);
//
//    @Query("""
//            SELECT p FROM Product p
//            WHERE NOT EXISTS (
//                SELECT r FROM InventoryResult r
//                WHERE r.inventorySession.inventorySessionId = :sessionId
//                  AND r.product.productId = p.productId
//            )
//            """)
//    List<Product> findProductsNotInInventoryResultForSession(@Param("sessionId") Long sessionId);
//}
