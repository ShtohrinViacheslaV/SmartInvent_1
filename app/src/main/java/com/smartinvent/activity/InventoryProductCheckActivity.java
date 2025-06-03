package com.smartinvent.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.smartinvent.R;
import com.smartinvent.model.*;
import com.smartinvent.service.InventoryService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class InventoryProductCheckActivity extends AppCompatActivity {

    private TextInputEditText productNameInput, productWorkIdInput, productDescriptionInput,
            productCategoryInput, productStorageInput, priceInput, countInput, manufacturerInput,
            expirationInput, weightInput, dimensionsInput, noteInput;

    private InventoryService inventoryService;
    private Long inventorySessionId;
    private Long productId;

    private InventoryProductResultDto currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_product_check);

        // Ініціалізація сервісу
        inventoryService = new InventoryService();

        // Отримання ID з Intent
        inventorySessionId = getIntent().getLongExtra(Constants.KEY_INVENTORY_SESSION_ID, -1);
        productId = getIntent().getLongExtra(Constants.KEY_PRODUCT_ID, -1);

        if (inventorySessionId == -1 || productId == -1) {
            Toast.makeText(this, "Невірні дані продукту або сесії", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initViews();
        loadProductData();

        Button saveButton = findViewById(R.id.confirmButton);
        saveButton.setOnClickListener(v -> saveInventoryResult());

        Button cancelButton = findViewById(R.id.backButton);
        cancelButton.setOnClickListener(v -> finish());

    }

    private void initViews() {
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
    }

    private void loadProductData() {
        inventoryService.getProductById(inventorySessionId, productId, new InventoryService.InventoryProductResultSingleCallback() {
            @Override
            public void onSuccess(InventoryProductResultDto productResultDto) {
                currentProduct = productResultDto;
                runOnUiThread(() -> populateFields(productResultDto));
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(InventoryProductCheckActivity.this, "Помилка завантаження продукту: " + errorMessage, Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        });
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


        priceInput.setText(product.getPrice() != null ? product.getPrice().stripTrailingZeros().toPlainString() : "");
        weightInput.setText(product.getWeight() != null ? product.getWeight().stripTrailingZeros().toPlainString() : "");
        expirationInput.setText(product.getExpirationDate() != null ? product.getExpirationDate().toString() : "");
        countInput.setText(product.getCount() != null ? product.getCount().toString() : "");
        manufacturerInput.setText(product.getManufacturer());
        dimensionsInput.setText(product.getDimensions());
        noteInput.setText(product.getDescription());
    }


    private void saveInventoryResult() {
        InventoryProductResultDto updatedDto = new InventoryProductResultDto();
        updatedDto.setInventoryResultId(currentProduct.getInventoryResultId());
        updatedDto.setInventorySessionId(currentProduct.getInventorySessionId());
        updatedDto.setProductId(currentProduct.getProductId());

        // Ціна
        String priceText = priceInput.getText().toString().trim();
        try {
            updatedDto.setPrice(!priceText.isEmpty() ? new BigDecimal(priceText) : null);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Невірний формат ціни", Toast.LENGTH_SHORT).show();
            return;
        }

        // Кількість (обов’язкове поле)
        String countText = countInput.getText().toString().trim();
        if (countText.isEmpty()) {
            Toast.makeText(this, "Поле 'Кількість' є обов'язковим", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            updatedDto.setCount(Integer.parseInt(countText));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Невірний формат кількості", Toast.LENGTH_SHORT).show();
            return;
        }

        // Виробник
        String manufacturer = manufacturerInput.getText().toString().trim();
        updatedDto.setManufacturer(manufacturer.isEmpty() ? null : manufacturer);

        // Термін придатності
        String inputDate = expirationInput.getText().toString().trim();
        try {
            updatedDto.setExpirationDate(!inputDate.isEmpty() ? LocalDate.parse(inputDate) : null);
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Невірний формат дати", Toast.LENGTH_SHORT).show();
            return;
        }

        // Вага
        String weightText = weightInput.getText().toString().trim();
        try {
            updatedDto.setWeight(!weightText.isEmpty() ? new BigDecimal(weightText) : null);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Невірний формат ваги", Toast.LENGTH_SHORT).show();
            return;
        }

        // Габарити
        String dimensions = dimensionsInput.getText().toString().trim();
        updatedDto.setDimensions(dimensions.isEmpty() ? null : dimensions);

        // Опис
        String note = noteInput.getText().toString().trim();
        updatedDto.setDescription(note.isEmpty() ? null : note);

        InventoryProductStatusEnum previousStatus = currentProduct.getStatus();
        InventoryProductStatusEnum status;

        // Якщо товар мав статус ADDED, залишаємо цей статус незалежно від змін
        if (previousStatus == InventoryProductStatusEnum.ADDED) {
            status = InventoryProductStatusEnum.ADDED;
        } else if (previousStatus == InventoryProductStatusEnum.MODIFIED) {
            status = InventoryProductStatusEnum.MODIFIED;
        } else {
            boolean isModified = isModified(updatedDto, currentProduct);
            status = isModified
                    ? InventoryProductStatusEnum.MODIFIED
                    : InventoryProductStatusEnum.CONFIRMED;
        }


        updatedDto.setStatus(status);

        // Повідомлення користувачу
        String message = status == InventoryProductStatusEnum.MODIFIED
                ? "Ви впевнені, що внесені зміни до товару є правильними і мають бути збережені?"
                : "Підтвердіть, що всі дані товару залишились без змін і відповідають дійсності.";

        new AlertDialog.Builder(this)
                .setTitle("Підтвердження")
                .setMessage(message)
                .setPositiveButton("Так", (dialog, which) -> {
                    Employee scannedBy = getCurrentEmployee();
                    if (scannedBy == null) {
                        Toast.makeText(this, "Не вдалося визначити користувача", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    updatedDto.setScannedBy(scannedBy.getEmployeeId());
                    updatedDto.setScanTime(LocalDateTime.now());

                    inventoryService.saveOrUpdateInventoryProductResultDto(inventorySessionId, updatedDto, new InventoryService.InventoryActionCallback() {
                        @Override
                        public void onSuccess(boolean success) {
                            runOnUiThread(() -> {
                                if (success) {
                                    Toast.makeText(InventoryProductCheckActivity.this, "Продукт збережено", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(InventoryProductCheckActivity.this, "Не вдалося зберегти", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            runOnUiThread(() -> Toast.makeText(InventoryProductCheckActivity.this, "Помилка: " + errorMessage, Toast.LENGTH_LONG).show());
                        }
                    });
                })
                .setNegativeButton("Скасувати", (dialog, which) -> dialog.dismiss())
                .show();
    }



    private boolean isModified(InventoryProductResultDto updated, InventoryProductResultDto original) {
        return !(
                Objects.equals(original.getCount(), updated.getCount()) &&
                        compareBigDecimal(original.getPrice(), updated.getPrice()) &&
                        stringEqualsIgnoreNull(original.getManufacturer(), updated.getManufacturer()) &&
                        Objects.equals(original.getExpirationDate(), updated.getExpirationDate()) &&
                        compareBigDecimal(original.getWeight(), updated.getWeight()) &&
                        stringEqualsIgnoreNull(original.getDimensions(), updated.getDimensions()) &&
                        stringEqualsIgnoreNull(original.getDescription(), updated.getDescription())
        );
    }

    private boolean stringEqualsIgnoreNull(String a, String b) {
        return (a == null ? "" : a.trim()).equals(b == null ? "" : b.trim());
    }


    private boolean compareBigDecimal(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.compareTo(b) == 0;
    }



    private Employee getCurrentEmployee() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long employeeId = prefs.getLong(Constants.KEY_EMPLOYEE_ID, -1);
        if (employeeId == -1) return null;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        Log.d("USER_PREFS", "Отримано employeeId: " + employeeId);

        return employee;
    }


}
