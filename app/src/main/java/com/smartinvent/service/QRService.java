package com.smartinvent.service;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

public class QRService {

    private static final int QR_CODE_SIZE = 400;

    public static Bitmap generateQRCode(String data) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);

        Bitmap bmp = Bitmap.createBitmap(QR_CODE_SIZE, QR_CODE_SIZE, Bitmap.Config.RGB_565);

        for (int x = 0; x < QR_CODE_SIZE; x++) {
            for (int y = 0; y < QR_CODE_SIZE; y++) {
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }


    public static String parseScannedQRCode(String scannedData) {
        if (scannedData == null || scannedData.isEmpty()) {
            return null;
        }
        return scannedData.trim();
    }
}
