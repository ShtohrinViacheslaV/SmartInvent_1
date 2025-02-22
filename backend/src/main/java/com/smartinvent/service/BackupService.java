package com.smartinvent.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackupService {

    private static final String BACKUP_DIRECTORY = "E:\\SmartInvent Projects\\smartinvent-backend\\src\\main\\resources\\backup";

    // Створення резервної копії
    public void createBackup() throws IOException {
        String backupFileName = BACKUP_DIRECTORY + "backup_" + System.currentTimeMillis() + ".sql";

        // Використовуємо команду PostgreSQL для створення резервної копії
        ProcessBuilder processBuilder = new ProcessBuilder(
                "pg_dump", "-U", "db_username", "-h", "db_host", "-d", "db_name", "-f", backupFileName);
        processBuilder.environment().put("PGPASSWORD", "db_password");
        Process process = processBuilder.start();

        // Очікуємо завершення процесу
        try {
            if (process.waitFor() != 0) {
                throw new RuntimeException("Error while creating backup. Process exited with non-zero status.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Backup process was interrupted.", e);
        }
    }

    // Відновлення з резервної копії
    public void restoreBackup(String backupFile) throws IOException {
        String backupFilePath = BACKUP_DIRECTORY + backupFile;

        // Використовуємо команду PostgreSQL для відновлення з резервної копії
        ProcessBuilder processBuilder = new ProcessBuilder(
                "psql", "-U", "db_username", "-h", "db_host", "-d", "db_name", "-f", backupFilePath);
        processBuilder.environment().put("PGPASSWORD", "db_password");
        Process process = processBuilder.start();

        // Очікуємо завершення процесу
        try {
            if (process.waitFor() != 0) {
                throw new RuntimeException("Error while restoring backup. Process exited with non-zero status.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Restore process was interrupted.", e);
        }
    }

    // Експорт даних у файл
    public void exportData() throws IOException {
        String exportFileName = BACKUP_DIRECTORY + "export_" + System.currentTimeMillis() + ".csv";

        // Експорт даних з бази даних у формат CSV (налаштуйте SQL-запит на ваші потреби)
        ProcessBuilder processBuilder = new ProcessBuilder(
                "psql", "-U", "db_username", "-h", "db_host", "-d", "db_name", "-c",
                "COPY (SELECT * FROM your_table) TO '" + exportFileName + "' CSV HEADER;");
        processBuilder.environment().put("PGPASSWORD", "db_password");
        Process process = processBuilder.start();

        // Очікуємо завершення процесу
        try {
            if (process.waitFor() != 0) {
                throw new RuntimeException("Error while exporting data. Process exited with non-zero status.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Export process was interrupted.", e);
        }
    }

    // Імпорт даних з файлу
    public void importData(String importFile) throws IOException {
        String importFilePath = BACKUP_DIRECTORY + importFile;

        // Імпорт даних з CSV у базу даних
        ProcessBuilder processBuilder = new ProcessBuilder(
                "psql", "-U", "db_username", "-h", "db_host", "-d", "db_name", "-c",
                "COPY your_table FROM '" + importFilePath + "' CSV HEADER;");
        processBuilder.environment().put("PGPASSWORD", "db_password");
        Process process = processBuilder.start();

        // Очікуємо завершення процесу
        try {
            if (process.waitFor() != 0) {
                throw new RuntimeException("Error while importing data. Process exited with non-zero status.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Import process was interrupted.", e);
        }
    }

    // Інші методи для взаємодії з BackupRepository (якщо необхідно)
    public List<String> getAllBackups() {
        File folder = new File(BACKUP_DIRECTORY);
        File[] listOfFiles = folder.listFiles();

        // Перевіряємо всі файли в директорії
        List<String> backupFiles = new ArrayList<>();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".sql")) {
                    backupFiles.add(file.getName());
                }
            }
        }
        return backupFiles;
    }
}
