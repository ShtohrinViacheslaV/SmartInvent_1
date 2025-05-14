package com.smartinvent.models;


import jakarta.persistence.*;

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
    @Column(name = "inventory_result_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inventory_session_id", nullable = false)
    private InventorySession session;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "scanned_by", nullable = false)
    private Employee scannedBy;

    private LocalDateTime scanTime;

    @Enumerated(EnumType.STRING)
    private InventoryProductStatusEnum status;



    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InventorySession getSession() {
        return session;
    }

    public void setSession(InventorySession session) {
        this.session = session;
    }

    public InventoryProductStatusEnum getStatus() {
        return status;
    }

    public void setStatus(InventoryProductStatusEnum status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Employee getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(Employee scannedBy) {
        this.scannedBy = scannedBy;
    }

    public LocalDateTime getScanTime() {
        return scanTime;
    }

    public void setScanTime(LocalDateTime scanTime) {
        this.scanTime = scanTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
