package com.smartinvent.localdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inventory_scan")
public class InventoryScanEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long productId;

    public String status; // "CONFIRMED", "DAMAGED", etc.

    public String comment;

    public String timestamp;

    public long scannedBy;
}
