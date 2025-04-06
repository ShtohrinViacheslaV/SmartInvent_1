package com.smartinvent.controller;

import com.google.zxing.WriterException;
import com.smartinvent.service.QRCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/qr-code")
public class QRCodeController {

    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/{productWorkId}")
    public ResponseEntity<byte[]> generateQrCode(@PathVariable String productWorkId) {
        try {
            final String qrCodeBase64 = qrCodeService.generateQrCodeImage(productWorkId);
            final byte[] qrCodeBytes = Base64.getDecoder().decode(qrCodeBase64);

            return ResponseEntity.ok()
                    .header("Content-Type", "image/png")
                    .body(qrCodeBytes);
        } catch (WriterException | IOException | IllegalArgumentException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
