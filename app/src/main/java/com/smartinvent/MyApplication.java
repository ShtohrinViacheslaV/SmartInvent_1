package com.smartinvent;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.android.volley.BuildConfig;
import com.smartinvent.config.FileLoggingTree;
import io.sentry.android.core.SentryAndroid;
import timber.log.Timber;
import timber.log.Timber.DebugTree;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyApplication extends Application {

    private static final String TAG = "SmartInventApp";
    private String logLevel = "DEBUG";  // Default log level

    @Override
    public void onCreate() {
        super.onCreate();
        loadLogConfig();
        logMessage("Application started with log level: " + logLevel);

//        FirebaseApp.initializeApp(this);

//        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        SentryAndroid.init(this, options -> {
            options.setDsn("https://8c57587821931877c442dbfccbc29cc1@o4509137541267456.ingest.de.sentry.io/4509137543168080");
            options.setTracesSampleRate(1.0);
            options.setDebug(true);
        });


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

