package com.smartinvent.backend.service;

import com.google.zxing.WriterException;
import com.smartinvent.service.QRCodeService;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class QRCodeServiceTest {

    private final QRCodeService qrCodeService = new QRCodeService();

    @Test
    void testGenerateQrCodeImage() throws WriterException, IOException {
        Long productId = 123456789L;

        byte[] qrCodeImage = qrCodeService.generateQrCodeImage(productId);

        // Перевіряємо, що зображення не є порожнім
        assertNotNull(qrCodeImage);
        assertTrue(qrCodeImage.length > 0, "QR Code image should not be empty");

        // Додатково можемо перевірити збереження у файл, якщо потрібно
        String filePath = "test-qr-code.png";
        qrCodeService.saveQrCodeToFile(productId, filePath);
    }
}
