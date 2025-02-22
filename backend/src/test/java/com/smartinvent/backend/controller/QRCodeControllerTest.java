package com.smartinvent.backend.controller;

import com.smartinvent.controller.QRCodeController;
import com.smartinvent.service.QRCodeService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QRCodeControllerTest {

    @Test
    void testGenerateQrCode_Success() throws Exception {
        Long productId = 123456789L;
        byte[] qrCodeImage = new byte[]{1, 2, 3, 4}; // Мок дані

        // Мокаємо сервіс
        QRCodeService qrCodeService = mock(QRCodeService.class);
        when(qrCodeService.generateQrCodeImage(productId)).thenReturn(qrCodeImage);

        // Створюємо контролер
        QRCodeController qrCodeController = new QRCodeController(qrCodeService);

        // Викликаємо метод
        ResponseEntity<byte[]> response = qrCodeController.generateQrCode(productId);

        // Перевіряємо відповідь
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertArrayEquals(qrCodeImage, response.getBody());
    }

    @Test
    void testGenerateQrCode_Exception() throws Exception {
        Long productId = 123456789L;

        // Мокаємо сервіс із винятком
        QRCodeService qrCodeService = mock(QRCodeService.class);
        when(qrCodeService.generateQrCodeImage(productId)).thenThrow(new IOException("Test Exception"));

        // Створюємо контролер
        QRCodeController qrCodeController = new QRCodeController(qrCodeService);

        // Викликаємо метод
        ResponseEntity<byte[]> response = qrCodeController.generateQrCode(productId);

        // Перевіряємо, що повертається 500 статус
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
