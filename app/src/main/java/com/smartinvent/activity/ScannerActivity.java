package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.smartinvent.model.Constants;
import com.smartinvent.service.QRService;

public class ScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startQrScanner();
    }

    private void startQrScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setOrientationLocked(false);
        integrator.setPrompt("Наведіть камеру на QR-код товару");
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedCode = result.getContents().trim();
                Log.d("QR_SCAN", "Просканований код: " + scannedCode);

                String productWorkId = QRService.parseScannedQRCode(scannedCode);

                if (productWorkId != null) {

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Constants.KEY_SCANNED_CODE, scannedCode);
                    setResult(RESULT_OK, resultIntent);

                } else {
                    // Якщо парсинг не вдався
                    Log.e("QR_SCAN", "Парсинг QR-коду не вдалося, неправильний формат");
                    setResult(RESULT_CANCELED);


                }
            } else {
                Log.d("QR_SCAN", "QR-код порожній або сканування скасовано");
                setResult(RESULT_CANCELED);

            }
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
