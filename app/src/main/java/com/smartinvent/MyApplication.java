package com.smartinvent;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyApplication extends Application {

    private static final String TAG = "SmartInventApp";
    private String logLevel = "DEBUG";  // Default log level

    @Override
    public void onCreate() {
        super.onCreate();
        loadLogConfig();  // Load the logging configuration
        logMessage("Application started with log level: " + logLevel);  // Example log message
    }

    private void loadLogConfig() {
        AssetManager assetManager = getAssets();
        try (InputStream inputStream = assetManager.open("log_config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            logLevel = properties.getProperty("log_level", "DEBUG");  // Default to DEBUG
            logMessage("Log level loaded: " + logLevel);
        } catch (IOException e) {
            logMessage("Error loading log configuration: " + e.getMessage());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        logMessage("Application terminated");
    }

    // This method will log messages based on the current log level
    public void logMessage(String message) {
        switch (logLevel) {
            case "DEBUG":
                Log.d(TAG, message);
                break;
            case "INFO":
                Log.i(TAG, message);
                break;
            case "ERROR":
                Log.e(TAG, message);
                break;
            default:
                Log.d(TAG, message);
                break;
        }
    }
}
