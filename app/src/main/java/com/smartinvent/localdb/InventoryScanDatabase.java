package com.smartinvent.localdb;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {InventoryScanEntity.class}, version = 1, exportSchema = false)
public abstract class InventoryScanDatabase extends RoomDatabase {

    public abstract InventoryScanDao inventoryScanDao();

    private static volatile InventoryScanDatabase INSTANCE;

    public static InventoryScanDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (InventoryScanDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    InventoryScanDatabase.class, "inventory_scan_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
