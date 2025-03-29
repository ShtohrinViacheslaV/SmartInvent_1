package com.smartinvent.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BackupService клас для роботи з резервними копіями бази даних.
 */
@Service
public class BackupService {

    /**
     * Директорія для зберігання резервних копій.
     */
    private static final String BACKUP_DIRECTORY = "E:\\SmartInvent Projects\\smartinvent-backend\\src\\main\\resources\\backup";

    /**
     * Метод для створення резервної копії бази даних.
     *
     * @throws IOException виняток, якщо виникла помилка під час створення резервної копії
     */
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

    /**
     * Метод для відновлення бази даних з резервної копії.
     *
     * @param backupFile назва файлу резервної копії
     * @throws IOException виняток, якщо виникла помилка під час відновлення з резервної копії
     */
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

    /**
     * Метод для експортування даних з бази даних у форматі CSV.
     *
     * @throws IOException виняток, якщо виникла помилка під час експортування даних
     */
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

    /**
     * Метод для імпортування даних з CSV у базу даних.
     *
     * @param importFile назва файлу для імпортування
     * @throws IOException виняток, якщо виникла помилка під час імпортування даних
     */
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

    /**
     * Метод для отримання всіх резервних копій.
     *
     * @return список всіх резервних копій
     */
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
