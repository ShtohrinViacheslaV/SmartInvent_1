package com.smartinvent.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.smartinvent.R;
import com.smartinvent.model.*;
import com.smartinvent.network.ApiClient;
import com.smartinvent.service.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InventoryProductCheckActivity extends AppCompatActivity {

    private TextInputEditText productNameInput, productWorkIdInput, productDescriptionInput, productCategoryInput, productStorageInput;
    private TextInputEditText priceInput, countInput, manufacturerInput, expirationInput, weightInput, dimensionsInput, noteInput;

    private Long inventorySessionId;
    private Long productId;

    private InventoryProductResultDto currentProduct;
    private InventoryApi inventoryApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inventory_product_check);

        // Ініціалізація полів
        productNameInput = findViewById(R.id.productNameInput);
        productWorkIdInput = findViewById(R.id.productWorkIdInput);
        productDescriptionInput = findViewById(R.id.productDescriptionInput);
        productCategoryInput = findViewById(R.id.categoryInput);
        productStorageInput = findViewById(R.id.storageInput);


        priceInput = findViewById(R.id.priceInput);
        countInput = findViewById(R.id.countInput);
        manufacturerInput = findViewById(R.id.manufacturerInput);
        expirationInput = findViewById(R.id.expirationInput);
        weightInput = findViewById(R.id.weightInput);
        dimensionsInput = findViewById(R.id.dimensionsInput);

        noteInput = findViewById(R.id.noteInput);


        // Отримуємо параметри із Intent
        Long inventorySessionId = getIntent().getLongExtra(Constants.KEY_INVENTORY_SESSION_ID, -1);
        Log.d("SessionID InventoryScannerActivity", String.valueOf(inventorySessionId));


        productId = getIntent().getLongExtra(Constants.KEY_PRODUCT_ID, -1);
        Log.d("ProductID InventoryProductCheckActivity", productId + "");


        inventoryApi = ApiClient.getClient().create(InventoryApi.class);

        if (inventorySessionId == -1 || productId == -1) {
            Toast.makeText(this, "Некоректні параметри", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        InventoryProductResultDto currentProduct = getIntent().getParcelableExtra("product");
        if (currentProduct != null) {
            populateFields(currentProduct);
        } else {
            Toast.makeText(this, "❌ Дані продукту не передані", Toast.LENGTH_SHORT).show();
            finish();
        }

        findViewById(R.id.confirmButton).setOnClickListener(v -> saveResult());
    }

    private void populateFields(InventoryProductResultDto product) {
        productNameInput.setText(product.getProductName());
        productWorkIdInput.setText(product.getProductWorkId());
        productWorkIdInput.setEnabled(false);
        productDescriptionInput.setText(product.getProductDescription());
        productCategoryInput.setText(product.getCategoryName());
        productCategoryInput.setEnabled(false);
        productStorageInput.setText(product.getStorageName());
        productStorageInput.setEnabled(false);


        priceInput.setText(product.getPrice() != null ? String.valueOf(product.getPrice()) : "");
        countInput.setText(product.getCount() != null ? String.valueOf(product.getCount()) : "");
        manufacturerInput.setText(product.getManufacturer());
        expirationInput.setText(product.getExpirationDate());
        weightInput.setText(product.getWeight() != null ? String.valueOf(product.getWeight()) : "");
        dimensionsInput.setText(product.getDimensions());
        noteInput.setText(product.getDescription());
    }




    private void saveResult() {
        if (currentProduct == null) {
            Toast.makeText(this, "Немає товару для збереження", Toast.LENGTH_SHORT).show();
            return;
        }

        // Отримуємо введені користувачем значення
        String newName = productNameInput.getText() != null ? productNameInput.getText().toString().trim() : "";
        String newDescription = productDescriptionInput.getText() != null ? productDescriptionInput.getText().toString().trim() : "";
        String newManufacturer = manufacturerInput.getText() != null ? manufacturerInput.getText().toString().trim() : "";
        String newExpiration = expirationInput.getText() != null ? expirationInput.getText().toString().trim() : "";
        String newDimensions = dimensionsInput.getText() != null ? dimensionsInput.getText().toString().trim() : "";
        String newNote = noteInput.getText() != null ? noteInput.getText().toString().trim() : "";

        Double newPrice = null;
        Integer newCount = null;
        Double newWeight = null;
        try {
            newPrice = !priceInput.getText().toString().trim().isEmpty() ? Double.parseDouble(priceInput.getText().toString().trim()) : null;
        } catch (NumberFormatException ignored) {}
        try {
            newCount = !countInput.getText().toString().trim().isEmpty() ? Integer.parseInt(countInput.getText().toString().trim()) : null;
        } catch (NumberFormatException ignored) {}
        try {
            newWeight = !weightInput.getText().toString().trim().isEmpty() ? Double.parseDouble(weightInput.getText().toString().trim()) : null;
        } catch (NumberFormatException ignored) {}

        // Перевірка, чи були зміни
        boolean isModified =
                !equalsSafe(currentProduct.getProductName(), newName) ||
                        !equalsSafe(currentProduct.getProductDescription(), newDescription) ||
                        !equalsSafe(currentProduct.getManufacturer(), newManufacturer) ||
                        !equalsSafe(currentProduct.getExpirationDate(), newExpiration) ||
                        !equalsSafe(currentProduct.getDimensions(), newDimensions) ||
                        !equalsSafe(currentProduct.getDescription(), newNote) ||
                        !equalsSafe(currentProduct.getPrice(), newPrice) ||
                        !equalsSafe(currentProduct.getCount(), newCount) ||
                        !equalsSafe(currentProduct.getWeight(), newWeight);

        InventoryResult result = new InventoryResult();
        result.setProduct(currentProduct.getProductId());
        result.setInventorySessionId(inventorySessionId);
        result.setDescription(newNote);
        result.setStatusName(isModified ? InventoryProductStatusEnum.MODIFIED : InventoryProductStatusEnum.CONFIRMED);

        Employee employee = getCurrentEmployee();
        if (employee == null) {
            Toast.makeText(this, "Користувач не знайдений", Toast.LENGTH_SHORT).show();
            return;
        }
        result.setScannedBy(employee.getEmployeeId());

        // Відправка на сервер
        inventoryApi.addResult(result).enqueue(new Callback<InventoryResult>() {
            @Override
            public void onResponse(Call<InventoryResult> call, Response<InventoryResult> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InventoryProductCheckActivity.this, "Результат збережено", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(InventoryProductCheckActivity.this, "Помилка збереження результату", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventoryResult> call, Throwable t) {
                Toast.makeText(InventoryProductCheckActivity.this, "Помилка мережі: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Employee getCurrentEmployee() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long employeeId = prefs.getLong("employee_id", -1);
        if (employeeId == -1) return null;

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        return employee;
    }

    private boolean equalsSafe(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

}
