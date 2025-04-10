package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
                    final String scannedCode = result.getData().getStringExtra("scannedCode");
                    Log.d("QR_SCAN", "Отримано код: " + scannedCode); // ✅ Логування

                    if (scannedCode != null && !scannedCode.isEmpty()) {
                        checkProductOnServer(scannedCode);
                    } else {
                        Toast.makeText(this, "Помилка: порожній код", Toast.LENGTH_SHORT).show();
                    }
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
        final Intent intent = new Intent(this, ScannerActivity.class);
        qrScannerLauncher.launch(intent);
    }

    private void checkProductOnServer(String productWorkId) {
        Log.d("QR_SCAN", "Отриманий код: " + productWorkId);

        try {
            productApi.getProductById(productWorkId).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Log.d("QR_SCAN", "HTTP код відповіді: " + response.code());

                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("QR_SCAN", "Товар знайдено: " + response.body().toString());
                        openProductDetails(response.body());
                    } else {
                        Log.d("QR_SCAN", "Товар не знайдено, пропонуємо створення нового");
                        showAddProductDialog(String.valueOf(productWorkId));
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Log.e("QR_SCAN", "Помилка підключення до сервера: ", t);
                    Toast.makeText(MainScannerActivity.this, "Помилка підключення до сервера", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Log.e("QR_SCAN", "Помилка формату ID товару: " + productWorkId, e);
            Toast.makeText(this, "Помилка: невірний формат ID товару", Toast.LENGTH_SHORT).show();
        }
    }



    private void openProductDetails(Product product) {
        final Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
        finish();
    }

    private void showAddProductDialog(String productWorkId) {
        new AlertDialog.Builder(this)
                .setTitle("Товар не знайдено")
                .setMessage("Додати новий товар?")
                .setPositiveButton("Так", (dialog, which) -> {
                    final Intent intent = new Intent(this, AddProductActivity.class);
                    intent.putExtra("productWorkId", productWorkId);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Ні", (dialog, which) -> finish())
                .show();
    }
}
