package com.smartinvent.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * QRCodeService клас для роботи з QR-кодами.
 */
@Service
public class QRCodeService {

    // Розміри QR-коду
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    /**
     * Метод generateQrCodeImage для генерації QR-коду із productId.
     *
     * @param productId id продукту
     * @return QR-код у вигляді масиву байтів
     * @throws WriterException
     * @throws IOException
     */
    public byte[] generateQrCodeImage(Long productId) throws WriterException, IOException {
        String content = String.valueOf(productId); // Генеруємо QR-код із productId
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Метод saveQrCodeToFile для збереження QR-коду продукту у файл.
     *
     * @param productId id продукту
     * @param filePath  шлях до файлу
     * @throws WriterException
     * @throws IOException
     */
    public void saveQrCodeToFile(Long productId, String filePath) throws WriterException, IOException {
        String content = String.valueOf(productId);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
