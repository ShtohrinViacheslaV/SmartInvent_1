package com.smartinvent.config;

import android.content.Context;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class DbConfigManager {
    private static final String FILE_NAME = "db_config.json";

    public static void saveConfig(Context context, DatabaseConfig config) {
        try {
            JSONObject json = new JSONObject();
            json.put("url", config.getUrl());
            json.put("username", config.getUsername());
            json.put("password", config.getPassword()); // Без шифрування

            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(json.toString().getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConfig loadConfig(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            JSONObject json = new JSONObject(new String(buffer, StandardCharsets.UTF_8));
            return new DatabaseConfig(
                    json.getString("url"),
                    json.getString("username"),
                    json.getString("password") // Без дешифрування
            );
        } catch (Exception e) {
            return null;
        }
    }
}
