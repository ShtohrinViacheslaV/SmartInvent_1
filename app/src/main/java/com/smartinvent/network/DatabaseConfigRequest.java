package com.smartinvent.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smartinvent.config.DatabaseConfig;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatabaseConfigRequest {
    private static final String TAG = "DatabaseConfigRequest";

    private static final String SERVER_URL = "http://192.168.0.120:8080/api/config";
//    private static final String SERVER_URL = "http://192.168.249.76:8080/api/config";
    private final RequestQueue requestQueue;
    private final Context context;


    public DatabaseConfigRequest(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void sendConfigToServer(String host, String port, String database, String username, String password, String url, DatabaseConfigCallback callback) {
        System.out.println("DatabaseConfigRequest sendConfigToServer ");

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("host", host);
            requestBody.put("port", port);
            requestBody.put("database", database);
            requestBody.put("username", username);
            requestBody.put("password", password);
            requestBody.put("url", url);


            sendRequest(requestBody, callback);
        } catch (Exception e) {
            logAndShowError("JSON Exception: " + e.getMessage(), e);
            callback.onResult(false);
        }
    }

    public void sendConfigToServer(DatabaseConfig config, DatabaseConfigCallback callback) {
        System.out.println("DatabaseConfigRequest sendConfigToServer ");

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("host", config.getHost());
            requestBody.put("port", config.getPort());
            requestBody.put("database", config.getDatabase());
            requestBody.put("username", config.getUsername());
            requestBody.put("password", config.getPassword());
            requestBody.put("url", config.getUrl());

            sendRequest(requestBody, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onResult(false);
        }
    }

    public void sendUrlToServer(String url, DatabaseConfigCallback callback) {
        System.out.println("DatabaseConfigRequest sendUrlToServer ");

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("url", url);

            sendRequest(requestBody, callback);
        } catch (Exception e) {
            logAndShowError("JSON Exception: " + e.getMessage(), e);
            callback.onResult(false);
        }
    }

    private void sendRequest(JSONObject requestBody, DatabaseConfigCallback callback) {
        System.out.println("DatabaseConfigRequest sendRequest ");

        StringRequest request = new StringRequest(Request.Method.POST, SERVER_URL,
                response -> {
                    Log.d(TAG, "Success: " + response);
                    Toast.makeText(context, "Успішно відправлено!", Toast.LENGTH_SHORT).show();
                    callback.onResult(true);
                },
                error -> {
                    String errorMessage = getVolleyErrorDetails(error);
                    logAndShowError("Volley Error: " + errorMessage, error);
                    callback.onResult(false);
                }) {
            @Override
            public byte[] getBody() {
                return requestBody.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        requestQueue.add(request);
    }

    public interface DatabaseConfigCallback {

        void onResult(boolean success);
    }

    private void logAndShowError(String message, Exception e) {
        Log.e(TAG, message, e);
        Toast.makeText(context, "Помилка: " + message, Toast.LENGTH_LONG).show();
    }

    private String getVolleyErrorDetails(VolleyError error) {
        System.out.println("DatabaseConfigRequest getVolleyErrorDetails ");

        if (error.networkResponse != null) {
            return "Статус код: " + error.networkResponse.statusCode;
        }
        if (error.getMessage() != null) {
            return error.getMessage();
        }
        return "Невідома помилка";
    }
}

//    public void sendConfigToServer(String dbType, String host, String port, String database, String user, String password, DatabaseConfigCallback callback) {
//        try {
//            JSONObject requestBody = new JSONObject();
//            requestBody.put("dbType", dbType);
//            requestBody.put("host", host);
//            requestBody.put("port", port);
//            requestBody.put("database", database);
//            requestBody.put("user", user);
//            requestBody.put("password", password);
//
//            sendRequest(requestBody, callback);
//        } catch (Exception e) {
//            e.printStackTrace();
//            callback.onResult(false);
//        }
//    }

//    public void sendUrlToServer(String url, DatabaseConfigCallback callback) {
//        try {
//            JSONObject requestBody = new JSONObject();
//            requestBody.put("url", url);
//
//            sendRequest(requestBody, callback);
//        } catch (Exception e) {
//            e.printStackTrace();
//            callback.onResult(false);
//        }
//    }
//
//    private void sendRequest(JSONObject requestBody, DatabaseConfigCallback callback) {
//        StringRequest request = new StringRequest(Request.Method.POST, SERVER_URL,
//                response -> {
//                    Log.d("DatabaseConfig", "Success: " + response);
//                    callback.onResult(true);
//                },
//                error -> {
//                    Log.e("DatabaseConfig", "Error: " + error.getMessage());
//                    callback.onResult(false);
//                }) {
//            @Override
//            public byte[] getBody() {
//                return requestBody.toString().getBytes();
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//        };
//
//        requestQueue.add(request);
//    }

//    public interface DatabaseConfigCallback {
//        void onResult(boolean success);
//    }
//}
//


//package com.smartinvent.network;
//
//import android.content.Context;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class DatabaseConfigRequest {
//    private static final String CONFIG_FILE_NAME = "com/smartinvent/config/application-company.properties";
//    private final Context context;
//
//    public DatabaseConfigRequest(Context context) {
//        this.context = context;
//    }
//
//    public boolean saveConfigToFile(String dbType, String host, String port, String database, String user, String password) {
//        String config = String.format(
//                "db.type=%s\n" +
//                        "db.host=%s\n" +
//                        "db.port=%s\n" +
//                        "db.name=%s\n" +
//                        "db.user=%s\n" +
//                        "db.password=%s\n",
//                dbType, host, port, database, user, password
//        );
//
//        return saveToFile(config);
//    }
//
//    public boolean saveUrlToFile(String url) {
//        String config = "db.url=" + url + "\n";
//        return saveToFile(config);
//    }
//
//    private boolean saveToFile(String config) {
//        try {
//            File configFile = new File(context.getFilesDir(), CONFIG_FILE_NAME);
//            FileOutputStream fos = new FileOutputStream(configFile);
//            fos.write(config.getBytes());
//            fos.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
//
//
//
////package com.smartinvent.network;
////
////public class DatabaseConfigRequest {
////    private String dbType;
////    private String host;
////    private String port;
////    private String database;
////    private String user;
////    private String password;
////    private String url;
////
////    public DatabaseConfigRequest(String dbType, String host, String port, String database, String user, String password) {
////        this.dbType = dbType;
////        this.host = host;
////        this.port = port;
////        this.database = database;
////        this.user = user;
////        this.password = password;
////        this.url = url;
////    }
////
////    public String getDbType() { return dbType; }
////    public String getHost() { return host; }
////    public String getPort() { return port; }
////    public String getDatabase() { return database; }
////    public String getUser() { return user; }
////    public String getPassword() { return password; }
////    public String getUrl() { return url; } // Новий геттер
////}
////
////
