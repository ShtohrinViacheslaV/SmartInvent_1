// Вказуємо, до якого пакету належить клас
package com.smartinvent.service;

// Імпортуємо класи з бібліотеки ZXing для створення QR-кодів
import com.google.zxing.BarcodeFormat;                  // Формати штрихкодів, зокрема QR-код
import com.google.zxing.WriterException;                // Виняток, який виникає при генерації QR-коду
import com.google.zxing.client.j2se.MatrixToImageWriter; // Утиліта для збереження QR-коду у файл чи потік
import com.google.zxing.common.BitMatrix;               // Двовимірна матриця бітів, що представляє QR-код
import com.google.zxing.qrcode.QRCodeWriter;            // Клас для створення QR-кодів

// Імпортуємо анотацію, яка вказує, що клас є сервісом у Spring Framework
import org.springframework.stereotype.Service;

// Імпортуємо класи для роботи з байтами, файлами і шляхами
import java.io.ByteArrayOutputStream;   // Потік для запису байтів у масив
import java.io.IOException;             // Виняток, який може виникнути при роботі з I/O
import java.nio.file.FileSystems;       // Доступ до файлової системи
import java.nio.file.Path;              // Представлення шляху до файлу

/**
 * QRCodeService — сервісний клас для створення і збереження QR-кодів.
 */
@Service  // Додає цей клас як Spring Bean, щоб його можна було використовувати як сервіс
public class QRCodeService {

    // Константи для розміру зображення QR-коду
    private static final int WIDTH = 300;   // Ширина зображення в пікселях
    private static final int HEIGHT = 300;  // Висота зображення в пікселях

    /**
     * Метод generateQrCodeImage створює QR-код на основі productId і повертає його як масив байтів.
     *
     * @param productId ID продукту, який буде зашифровано у QR-коді
     * @return Масив байтів, що представляє QR-код у форматі PNG
     * @throws WriterException Якщо виникла помилка при генерації QR-коду
     * @throws IOException Якщо виникла помилка при записі зображення у потік
     */
    public byte[] generateQrCodeImage(Long productId) throws WriterException, IOException {
        String content = String.valueOf(productId); // Перетворюємо productId у текст для QR-коду

        QRCodeWriter qrCodeWriter = new QRCodeWriter(); // Створюємо об’єкт для генерації QR-коду

        // Генеруємо QR-код у вигляді матриці бітів (чорно-білих пікселів)
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

        // Створюємо потік для запису зображення QR-коду у пам’ять
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Записуємо QR-код (bitMatrix) у потік у форматі PNG
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        // Повертаємо QR-код у вигляді масиву байтів
        return outputStream.toByteArray();
    }

    /**
     * Метод saveQrCodeToFile створює QR-код і зберігає його як зображення у файл.
     *
     * @param productId ID продукту, який буде зашифровано у QR-коді
     * @param filePath Повний шлях до файлу, куди буде збережено QR-код
     * @throws WriterException Якщо виникла помилка при генерації QR-коду
     * @throws IOException Якщо виникла помилка при збереженні зображення
     */
    public void saveQrCodeToFile(Long productId, String filePath) throws WriterException, IOException {
        String content = String.valueOf(productId); // Перетворюємо productId у текст

        QRCodeWriter qrCodeWriter = new QRCodeWriter(); // Ініціалізуємо генератор QR-кодів

        // Створюємо QR-код у вигляді бітової матриці
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

        // Отримуємо об'єкт Path для вказаного шляху
        Path path = FileSystems.getDefault().getPath(filePath);

        // Записуємо QR-код у файл у форматі PNG
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
