package com.smartinvent.config;


import android.content.Context;
import android.content.SharedPreferences;

public class DbConfigManager {
    private static final String PREF_NAME = "db_config";
    private static final String KEY_HOST = "db_host";
    private static final String KEY_PORT = "db_port";
    private static final String KEY_USER = "db_user";
    private static final String KEY_PASSWORD = "db_password";
    private static final String KEY_URL = "db_url";

    public static void saveConfig(Context context, DatabaseConfig config) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_HOST, config.getHost());
        editor.putString(KEY_PORT, config.getPort());
        editor.putString(KEY_USER, config.getUsername());
        editor.putString(KEY_PASSWORD, config.getPassword());
        editor.putString(KEY_URL, config.getUrl());
        editor.apply(); // Зберігаємо зміни
    }

    public static DatabaseConfig loadConfig(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String host = prefs.getString(KEY_HOST, "");
        String port = prefs.getString(KEY_PORT, "");
        String user = prefs.getString(KEY_USER, "");
        String password = prefs.getString(KEY_PASSWORD, "");
        String url = prefs.getString(KEY_URL, "");
        return new DatabaseConfig(host, port, user, password, url);
    }

    public static void clearConfig(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}


//package com.smartinvent.config;
//
//import android.content.Context;
//import org.json.JSONObject;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.nio.charset.StandardCharsets;
//
//public class DbConfigManager {
//    private static final String FILE_NAME = "db_config.json";
//
//    public static void saveConfig(Context context, DatabaseConfig config) {
//        try {
//            JSONObject json = new JSONObject();
//            json.put("host", config.getHost());
//            json.put("port", config.getPort());
//            json.put("username", config.getUsername());
//            json.put("password", config.getPassword());
//            json.put("url", config.getUrl());
//
//            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
//            fos.write(json.toString().getBytes(StandardCharsets.UTF_8));
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static DatabaseConfig loadConfig(Context context) {
//        try {
//            FileInputStream fis = context.openFileInput(FILE_NAME);
//            byte[] buffer = new byte[fis.available()];
//            fis.read(buffer);
//            fis.close();
//
//            JSONObject json = new JSONObject(new String(buffer, StandardCharsets.UTF_8));
//            return new DatabaseConfig(
//                    json.getString("host"),
//                    json.getString("port"),
//                    json.getString("username"),
//                    json.getString("password"),
//                    json.getString("url")
//            );
//        } catch (Exception e) {
//            return null;
//        }
//    }
//}

//package com.smartinvent.config;
//
//import android.content.Context;
//import org.json.JSONObject;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.nio.charset.StandardCharsets;
//
//public class DbConfigManager {
//    private static final String FILE_NAME = "db_config.json";
//
//    public static void saveConfig(Context context, DatabaseConfig config) {
//        try {
//            JSONObject json = new JSONObject();
//            json.put("host", config.getHost());
//            json.put("port", config.getPort());
//            json.put("username", config.getUsername());
//            json.put("password", config.getPassword()); // Без шифрування
//            json.put("url", config.getUrl());
//
//
//            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
//            fos.write(json.toString().getBytes(StandardCharsets.UTF_8));
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static DatabaseConfig loadConfig(Context context) {
//        try {
//            FileInputStream fis = context.openFileInput(FILE_NAME);
//            byte[] buffer = new byte[fis.available()];
//            fis.read(buffer);
//            fis.close();
//
//            JSONObject json = new JSONObject(new String(buffer, StandardCharsets.UTF_8));
//            return new DatabaseConfig(
//                    json.getString("host"),
//                    json.getString("port"),
//                    json.getString("username"),
//                    json.getString("password"),
//                    json.getString("url")
//            );
//        } catch (Exception e) {
//            return null;
//        }
//    }
//}
