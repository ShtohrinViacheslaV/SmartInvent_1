package com.smartinvent.controller;

import com.google.zxing.WriterException;
import com.smartinvent.service.QRCodeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Клас-контроллер для обробки запитів, пов'язаних з QR-кодами
 */
@RestController
@RequestMapping("/api/qr-code")
public class QRCodeController {

    /**
     * Об'єкт сервісу для роботи з QR-кодами
     */
    private final QRCodeService qrCodeService;

    /**
     * Конструктор
     *
     * @param qrCodeService - сервіс для роботи з QR-кодами
     */
    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    /**
     * Метод для генерації QR-коду для продукту
     *
     * @param productId - ідентифікатор продукту
     * @return - зображення QR-коду
     */
    @GetMapping("/{productId}")
    public ResponseEntity<byte[]> generateQrCode(@PathVariable Long productId) {
        try {
            byte[] qrCodeImage = qrCodeService.generateQrCodeImage(productId);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png");
            return new ResponseEntity<>(qrCodeImage, headers, HttpStatus.OK);
        } catch (WriterException | IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
