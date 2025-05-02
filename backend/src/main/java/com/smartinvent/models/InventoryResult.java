package com.smartinvent.models;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryResultId;

    @ManyToOne
    @JoinColumn(name = "inventory_session_id", nullable = false)
    private InventorySession inventorySession;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "scanned_by", nullable = false)
    private Employee scannedBy;

    private LocalDateTime scanTime;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private InventoryProductStatus status;

    private String description;

}
