package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.model.Product;
import com.smartinvent.network.ApiClient;
import com.smartinvent.service.ProductApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScannerActivity extends AppCompatActivity {

    private ProductApi productApi;
    private final ActivityResultLauncher<Intent> qrScannerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String scannedCode = result.getData().getStringExtra("scannedCode");
                    checkProductOnServer(Long.parseLong(scannedCode));
                } else {
                    Toast.makeText(this, "Сканування скасовано", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productApi = ApiClient.getClient().create(ProductApi.class);
        startQrScanner();
    }

    private void startQrScanner() {
        Intent intent = new Intent(this, ScannerActivity.class);
        qrScannerLauncher.launch(intent);

//        IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setOrientationLocked(false);
//        integrator.setPrompt("Наведіть камеру на QR-код товару");
//        integrator.setBeepEnabled(true);
//        integrator.initiateScan();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            if (result.getContents() != null) {
//                String scannedCode = result.getContents();
//                checkProductOnServer(scannedCode);
//            } else {
//                Toast.makeText(this, "Сканування скасовано", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    private void checkProductOnServer(Long productId) {
        try {
            productApi.getProductById(productId).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        openProductDetails(response.body());
                    } else {
                        showAddProductDialog(String.valueOf(productId));
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(MainScannerActivity.this, "Помилка підключення до сервера", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Помилка: невірний формат ID товару", Toast.LENGTH_SHORT).show();
        }
    }


    private void openProductDetails(Product product) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
        finish();
    }

    private void showAddProductDialog(String productId) {
        new AlertDialog.Builder(this)
                .setTitle("Товар не знайдено")
                .setMessage("Додати новий товар?")
                .setPositiveButton("Так", (dialog, which) -> {
                    Intent intent = new Intent(this, AddProductActivity.class);
                    intent.putExtra("product_id", productId);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Ні", (dialog, which) -> finish())
                .show();
    }
}
