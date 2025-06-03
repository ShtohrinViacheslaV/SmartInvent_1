package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.smartinvent.R;
import com.smartinvent.model.Constants;
import com.smartinvent.model.InventoryProductResultDto;
import com.smartinvent.model.InventoryProductStatusEnum;
import com.smartinvent.service.InventoryService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class InventoryProductReviewActivity extends AppCompatActivity {

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

        inventoryService = new InventoryService();

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
        saveButton.setOnClickListener(v -> confirmAndGoToInventoryResults());

        Button cancelButton = findViewById(R.id.backButton);
        cancelButton.setOnClickListener(v -> finish());

        Button notFoundButton = findViewById(R.id.notFoundButton);
        if (notFoundButton != null) {
            notFoundButton.setOnClickListener(v -> markAsNotFound());
            notFoundButton.setVisibility(View.GONE); // приховуємо за замовчуванням
        }

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

        // Для адміністратора всі поля редаговані (крім, наприклад, productWorkId, category, storage)
        productWorkIdInput.setEnabled(false);
        productCategoryInput.setEnabled(false);
        productStorageInput.setEnabled(false);

        // Інші поля — редаговані
        productNameInput.setEnabled(true);
        productDescriptionInput.setEnabled(true);
        priceInput.setEnabled(true);
        countInput.setEnabled(true);
        manufacturerInput.setEnabled(true);
        expirationInput.setEnabled(true);
        weightInput.setEnabled(true);
        dimensionsInput.setEnabled(true);
        noteInput.setEnabled(true);
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
                    Toast.makeText(InventoryProductReviewActivity.this, "Помилка завантаження продукту: " + errorMessage, Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        });
    }

    private void populateFields(InventoryProductResultDto product) {
        productNameInput.setText(product.getProductName());
        productWorkIdInput.setText(product.getProductWorkId());
        productDescriptionInput.setText(product.getProductDescription());
        productCategoryInput.setText(product.getCategoryName());
        productStorageInput.setText(product.getStorageName());
        priceInput.setText(product.getPrice() != null ? product.getPrice().stripTrailingZeros().toPlainString() : "");
        weightInput.setText(product.getWeight() != null ? product.getWeight().stripTrailingZeros().toPlainString() : "");
        expirationInput.setText(product.getExpirationDate() != null ? product.getExpirationDate().toString() : "");
        countInput.setText(product.getCount() != null ? product.getCount().toString() : "");
        manufacturerInput.setText(product.getManufacturer());
        dimensionsInput.setText(product.getDimensions());
        noteInput.setText(product.getDescription());

        Button notFoundButton = findViewById(R.id.notFoundButton);
        if (product.getStatus() == InventoryProductStatusEnum.UNCHECKED && notFoundButton != null) {
            notFoundButton.setVisibility(View.VISIBLE);
        }
    }

    private void markAsNotFound() {
        InventoryProductResultDto updatedDto = new InventoryProductResultDto();
        updatedDto.setInventoryResultId(currentProduct.getInventoryResultId());
        updatedDto.setInventorySessionId(currentProduct.getInventorySessionId());
        updatedDto.setProductId(currentProduct.getProductId());
        updatedDto.setStatus(InventoryProductStatusEnum.NOT_FOUND);

        inventoryService.saveOrUpdateInventoryProductResultDto(inventorySessionId, updatedDto, new InventoryService.InventoryActionCallback() {
            @Override
            public void onSuccess(boolean success) {
                runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(InventoryProductReviewActivity.this, "Позначено як не знайдено", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InventoryProductReviewActivity.this, ReviewInventoryResultsActivity.class);
                        intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, inventorySessionId);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(InventoryProductReviewActivity.this, "Не вдалося зберегти", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(InventoryProductReviewActivity.this, "Помилка: " + errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }


    private void confirmAndGoToInventoryResults() {
        InventoryProductResultDto updatedDto = new InventoryProductResultDto();
        updatedDto.setInventoryResultId(currentProduct.getInventoryResultId());
        updatedDto.setInventorySessionId(currentProduct.getInventorySessionId());
        updatedDto.setProductId(currentProduct.getProductId());

        // Отримання нових значень з полів
        BigDecimal newPrice = parseBigDecimal(priceInput.getText().toString().trim());
        Integer newCount = parseInteger(countInput.getText().toString().trim());
        String newManufacturer = manufacturerInput.getText().toString().trim();
        LocalDate newExpirationDate = parseLocalDate(expirationInput.getText().toString().trim());
        BigDecimal newWeight = parseBigDecimal(weightInput.getText().toString().trim());
        String newDimensions = dimensionsInput.getText().toString().trim();
        String newDescription = noteInput.getText().toString().trim();

        // Встановлення значень
        updatedDto.setPrice(newPrice);
        updatedDto.setCount(newCount);
        updatedDto.setManufacturer(newManufacturer);
        updatedDto.setExpirationDate(newExpirationDate);
        updatedDto.setWeight(newWeight);
        updatedDto.setDimensions(newDimensions);
        updatedDto.setDescription(newDescription);

        // Визначення нового статусу
        boolean isModified = !Objects.equals(strip(currentProduct.getPrice()), strip(newPrice))
                || !equals(currentProduct.getCount(), newCount)
                || !equals(currentProduct.getManufacturer(), newManufacturer)
                || !equals(currentProduct.getExpirationDate(), newExpirationDate)
                || !equals(currentProduct.getWeight(), newWeight)
                || !equals(currentProduct.getDimensions(), newDimensions)
                || !equals(currentProduct.getDescription(), newDescription);

        InventoryProductStatusEnum oldStatus = currentProduct.getStatus();
        InventoryProductStatusEnum newStatus;

        if (oldStatus == InventoryProductStatusEnum.UNCHECKED) {
            newStatus = isModified ? InventoryProductStatusEnum.MODIFIED : InventoryProductStatusEnum.CONFIRMED;
        } else if (oldStatus == InventoryProductStatusEnum.ADDED) {
            newStatus = InventoryProductStatusEnum.ADDED;
        } else if (oldStatus == InventoryProductStatusEnum.MODIFIED) {
            newStatus = InventoryProductStatusEnum.MODIFIED;
        } else if (oldStatus == InventoryProductStatusEnum.CONFIRMED) {
            newStatus = isModified ? InventoryProductStatusEnum.MODIFIED : InventoryProductStatusEnum.CONFIRMED;
        } else {
            newStatus = InventoryProductStatusEnum.CONFIRMED; // Default
        }

        updatedDto.setStatus(newStatus);

        // Відправлення на збереження
        inventoryService.saveOrUpdateInventoryProductResultDto(inventorySessionId, updatedDto, new InventoryService.InventoryActionCallback() {
            @Override
            public void onSuccess(boolean success) {
                runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(InventoryProductReviewActivity.this, "Продукт збережено", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InventoryProductReviewActivity.this, ReviewInventoryResultsActivity.class);
                        intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, inventorySessionId);
                        intent.putExtra(Constants.KEY_PRODUCT_ID, productId);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(InventoryProductReviewActivity.this, "Не вдалося зберегти", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(InventoryProductReviewActivity.this, "Помилка: " + errorMessage, Toast.LENGTH_LONG).show());
            }
        });
    }

    private boolean equals(Object a, Object b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }

    private BigDecimal strip(BigDecimal value) {
        return value == null ? null : value.stripTrailingZeros();
    }


    private BigDecimal parseBigDecimal(String str) {
        try {
            return TextUtils.isEmpty(str) ? null : new BigDecimal(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInteger(String str) {
        try {
            return str.isEmpty() ? null : Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDate parseLocalDate(String str) {
        try {
            return str.isEmpty() ? null : LocalDate.parse(str);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}




//    private void confirmAndGoToInventoryResults() {
//        // Тут логіка збереження оновленого продукту (аналог saveInventoryResult() в твоєму класі)
//        // Після успішного збереження перейти до InventoryResultsActivity, де буде список результатів інвентаризації
//
//        // Приклад без детальної валідації (для стислості)
//        InventoryProductResultDto updatedDto = new InventoryProductResultDto();
//        updatedDto.setInventoryResultId(currentProduct.getInventoryResultId());
//        updatedDto.setInventorySessionId(currentProduct.getInventorySessionId());
//        updatedDto.setProductId(currentProduct.getProductId());
//
//        updatedDto.setPrice(parseBigDecimal(priceInput.getText().toString().trim()));
//        updatedDto.setCount(parseInteger(countInput.getText().toString().trim()));
//        updatedDto.setManufacturer(manufacturerInput.getText().toString().trim());
//        updatedDto.setExpirationDate(parseLocalDate(expirationInput.getText().toString().trim()));
//        updatedDto.setWeight(parseBigDecimal(weightInput.getText().toString().trim()));
//        updatedDto.setDimensions(dimensionsInput.getText().toString().trim());
//        updatedDto.setDescription(noteInput.getText().toString().trim());
//
//        updatedDto.setStatus(InventoryProductStatusEnum.CONFIRMED);
//
//        inventoryService.saveOrUpdateInventoryProductResultDto(inventorySessionId, updatedDto, new InventoryService.InventoryActionCallback() {
//            @Override
//            public void onSuccess(boolean success) {
//                runOnUiThread(() -> {
//                    if (success) {
//                        Toast.makeText(InventoryProductReviewActivity.this, "Продукт збережено", Toast.LENGTH_SHORT).show();
//                        // Переходимо до сторінки з результатами інвентаризації
//                        Intent intent = new Intent(InventoryProductReviewActivity.this, ReviewInventoryResultsActivity.class);
//                        intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, inventorySessionId);
//                        intent.putExtra(Constants.KEY_PRODUCT_ID, productId); // для позначення галочкою
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(InventoryProductReviewActivity.this, "Не вдалося зберегти", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                runOnUiThread(() -> Toast.makeText(InventoryProductReviewActivity.this, "Помилка: " + errorMessage, Toast.LENGTH_LONG).show());
//            }
//        });
//    }