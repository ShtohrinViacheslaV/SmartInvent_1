package com.smartinvent.backend.controller;

import com.smartinvent.controller.QRCodeController;
import com.smartinvent.service.QRCodeService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Base64;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QRCodeControllerTest {

    @Test
    void testGenerateQrCode_Success() throws Exception {
        String productWorkId = "123456789";
        byte[] qrCodeImage = new byte[]{1, 2, 3, 4}; // Приклад байтового масиву зображення

        // Мокаємо сервіс
        QRCodeService qrCodeService = mock(QRCodeService.class);
        when(qrCodeService.generateQrCodeImage(productWorkId)).thenReturn(Base64.getEncoder().encodeToString(qrCodeImage));

        // Створюємо контролер
        QRCodeController qrCodeController = new QRCodeController(qrCodeService);

        // Викликаємо метод
        ResponseEntity<byte[]> response = qrCodeController.generateQrCode(productWorkId);

        // Перевіряємо відповідь
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertArrayEquals(qrCodeImage, response.getBody());
    }

    @Test
    void testGenerateQrCode_Exception() throws Exception {
        String productWorkId = "123456789";

        // Мокаємо сервіс із винятком
        QRCodeService qrCodeService = mock(QRCodeService.class);
        when(qrCodeService.generateQrCodeImage(productWorkId)).thenThrow(new IOException("Test Exception"));

        // Створюємо контролер
        QRCodeController qrCodeController = new QRCodeController(qrCodeService);

        // Викликаємо метод
        ResponseEntity<byte[]> response = qrCodeController.generateQrCode(productWorkId);

        // Перевіряємо, що повертається 500 статус
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}