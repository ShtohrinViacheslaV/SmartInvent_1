package com.smartinvent.config;

import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class FileLoggingTree extends Timber.DebugTree {

    private final File logFile;

    public FileLoggingTree(File logFile) {
        this.logFile = logFile;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String priorityStr = getPriorityString(priority);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.append(time)
                    .append(" | ")
                    .append(priorityStr)
                    .append(" | ")
                    .append(tag != null ? tag : "NoTag")
                    .append(" | ")
                    .append(message)
                    .append("\n");

            if (t != null) {
                writer.append(Log.getStackTraceString(t)).append("\n");
            }

        } catch (IOException e) {
            Log.e("FileLoggingTree", "Error writing log to file", e);
        }
    }

    private String getPriorityString(int priority) {
        switch (priority) {
            case Log.VERBOSE: return "VERBOSE";
            case Log.DEBUG: return "DEBUG";
            case Log.INFO: return "INFO";
            case Log.WARN: return "WARN";
            case Log.ERROR: return "ERROR";
            case Log.ASSERT: return "ASSERT";
            default: return "UNKNOWN";
        }
    }
}
