package com.smartinvent.localdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface InventoryScanDao {

    @Insert
    void insertScan(InventoryScanEntity scan);

    @Query("SELECT * FROM inventory_scan WHERE productId = :productId")
    LiveData<List<InventoryScanEntity>> getScansByProduct(long productId);
}
