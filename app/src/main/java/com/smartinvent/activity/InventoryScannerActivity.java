package com.smartinvent.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.smartinvent.R;
import com.smartinvent.model.Constants;
import com.smartinvent.model.InventoryProductResultDto;
import com.smartinvent.network.ApiClient;


import java.util.List;

import com.smartinvent.service.InventoryService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryScannerActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private Long inventorySessionId;


    private InventoryService inventoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inventorySessionId = getIntent().getLongExtra(Constants.KEY_INVENTORY_SESSION_ID, -1);
        Log.d("SessionID InventoryScannerActivity", String.valueOf(inventorySessionId));


        if (inventorySessionId == -1) {
            Toast.makeText(this, "❌ Сесія не передана", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        inventoryService = new InventoryService();


        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Скануйте QR-код товару");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.initiateScan();
    }

    // Обробка результату сканування
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedWorkId = result.getContents();
                findProductByWorkId(scannedWorkId);
            } else {
                Toast.makeText(this, "Сканування скасовано", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void findProductByWorkId(String productWorkId) {
        inventoryService.getProductByWorkId(inventorySessionId, productWorkId, new InventoryService.InventoryProductResultSingleCallback() {
            @Override
            public void onSuccess(InventoryProductResultDto dto) {
                Long productId = dto.getProductId();

                Intent intent = new Intent(InventoryScannerActivity.this, InventoryProductCheckActivity.class);
                intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, inventorySessionId);
                intent.putExtra(Constants.KEY_PRODUCT_ID, productId);
                intent.putExtra(Constants.KEY_PRODUCT, dto);
                Log.d("SessionID InventoryScannerActivity", String.valueOf(inventorySessionId));
                Log.d("ProductID InventoryScannerActivity", String.valueOf(productId));
                Log.d("ProductData InventoryScannerActivity", dto.toString());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                // Показуємо діалогове вікно з підтвердженням
                new AlertDialog.Builder(InventoryScannerActivity.this)
                        .setTitle("Товар не знайдено")
                        .setMessage("Цього товару немає в інвентаризації. Бажаєте його створити?")
                        .setPositiveButton("Створити", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Переходимо на сторінку створення товару
                                Intent intent = new Intent(InventoryScannerActivity.this, AddProductActivity.class);
                                intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, inventorySessionId);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Скасувати", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .show();
            }

        });
    }

}