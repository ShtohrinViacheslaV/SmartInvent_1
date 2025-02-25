//package com.smartinvent.models;
//
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "inventory_scan")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class InventoryScan {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;
//
//    @ManyToOne
//    @JoinColumn(name = "scanned_by", nullable = false)
//    private Employee scannedBy;
//
//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private Status status;
//
//    @Column
//    private String comment;
//
//    @Column(name = "timestamp", nullable = false)
//    private LocalDateTime timestamp = LocalDateTime.now();
//
//    public enum Status {
//        CONFIRMED, DAMAGED, NOT_FOUND, UNEXPECTED, RECHECK
//    }
//}
