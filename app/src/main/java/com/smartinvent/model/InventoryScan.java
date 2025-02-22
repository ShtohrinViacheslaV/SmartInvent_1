package com.smartinvent.model;

public class InventoryScan {

    private long id;
    private long productId;
    private String status; // "CONFIRMED", "DAMAGED", "NOT_FOUND", etc.
    private String comment;
    private String timestamp;
    private long scannedBy;

    // Конструктор
    public InventoryScan(long productId, String status, String comment, String timestamp, long scannedBy) {
        this.productId = productId;
        this.status = status;
        this.comment = comment;
        this.timestamp = timestamp;
        this.scannedBy = scannedBy;
    }

    // Геттери та сеттери
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public long getScannedBy() { return scannedBy; }
    public void setScannedBy(long scannedBy) { this.scannedBy = scannedBy; }
}
