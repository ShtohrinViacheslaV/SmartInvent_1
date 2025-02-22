package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startQrScanner();
    }

    private void startQrScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Наведіть камеру на QR-код товару");
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Intent intent = new Intent();
                intent.putExtra("scannedCode", result.getContents());
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
