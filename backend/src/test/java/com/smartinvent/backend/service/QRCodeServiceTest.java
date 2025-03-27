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
        String productWorkId = String.valueOf(123456789L);

        String qrCodeImage = qrCodeService.generateQrCodeImage(productWorkId);

        // Перевіряємо, що зображення не є порожнім
        assertNotNull(qrCodeImage);

        // Додатково можемо перевірити збереження у файл, якщо потрібно
        String filePath = "test-qr-code.png";
        qrCodeService.saveQrCodeToFile(productWorkId, filePath);
    }
}
