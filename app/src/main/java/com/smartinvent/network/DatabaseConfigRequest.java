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
//    private static final String SERVER_URL = "http://192.168.19.76:8080/api/config";
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
