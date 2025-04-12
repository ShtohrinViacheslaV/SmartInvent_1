package com.smartinvent.backend.controller;

import com.smartinvent.controller.QRCodeController;
import com.smartinvent.service.QRCodeService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Base64;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QRCodeControllerTest {

    @Test
    void testGenerateQrCodeSuccess() throws Exception {
        final String productWorkId = "123456789";
        final byte[] qrCodeImage = new byte[]{1, 2, 3, 4}; // Приклад байтового масиву зображення

        // Мокаємо сервіс
        final QRCodeService qrCodeService = mock(QRCodeService.class);
        when(qrCodeService.generateQrCodeImage(productWorkId)).thenReturn(Base64.getEncoder().encodeToString(qrCodeImage));

        // Створюємо контролер
        final QRCodeController qrCodeController = new QRCodeController(qrCodeService);

        // Викликаємо метод
        final ResponseEntity<byte[]> response = qrCodeController.generateQrCode(productWorkId);

        // Перевіряємо відповідь
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertArrayEquals(qrCodeImage, response.getBody());
    }

    @Test
    void testGenerateQrCodeException() throws Exception {
        final String productWorkId = "123456789";

        // Мокаємо сервіс із винятком
        final QRCodeService qrCodeService = mock(QRCodeService.class);
        when(qrCodeService.generateQrCodeImage(productWorkId)).thenThrow(new IOException("Test Exception"));

        // Створюємо контролер
        final QRCodeController qrCodeController = new QRCodeController(qrCodeService);

        // Викликаємо метод
        final ResponseEntity<byte[]> response = qrCodeController.generateQrCode(productWorkId);

        // Перевіряємо, що повертається 500 статус
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}