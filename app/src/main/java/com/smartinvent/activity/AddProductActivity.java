package com.smartinvent.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.smartinvent.R;
import com.smartinvent.model.*;
import com.smartinvent.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


public class AddProductActivity extends AppCompatActivity {

    private EditText addName, addDescription, addProductWorkId, addPrice, addCount, addManufacturer, addExpirationDate, addWeight, addDimensions;
    private MaterialAutoCompleteTextView addspnCategory, addspnStorage;
    private Button btnScanQr, btnViewQr, addbtnSave, addbtnCancel;

    private ProductService productService;
    private CategoryService categoryService;
    private StorageService storageService;
    private TransactionService transactionService;
    private InventoryService inventoryService;

    private static final String ADD_NEW_CATEGORY = "➕ Додати нову категорію";
    private static final String ADD_NEW_STORAGE = "➕ Додати новий склад";


    private List<Category> categoryList = new ArrayList<>();
    private List<Storage> storageList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> storageAdapter;

    private long companyId;

    private static final int QR_SCAN_REQUEST_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        addName = findViewById(R.id.add_name);
        addDescription = findViewById(R.id.add_description);
        addProductWorkId = findViewById(R.id.add_product_work_id);
        addPrice = findViewById(R.id.add_price);
        addManufacturer = findViewById(R.id.add_manufacturer);
        addExpirationDate = findViewById(R.id.add_expiration_date);
        addExpirationDate.setOnClickListener(v -> showDatePickerDialog());
        addExpirationDate.setInputType(InputType.TYPE_NULL); // Забороняємо введення вручну
        addWeight = findViewById(R.id.add_weight);
        addDimensions = findViewById(R.id.add_dimensions);
        addCount = findViewById(R.id.add_count);
        addspnCategory = findViewById(R.id.add_spn_category);
        addspnStorage = findViewById(R.id.add_spn_storage);


        btnScanQr = findViewById(R.id.btn_scan_qr);
        btnViewQr = findViewById(R.id.btn_view_qr);
        addbtnSave = findViewById(R.id.btn_add_save);
        addbtnCancel = findViewById(R.id.btn_add_cancel);

        btnViewQr.setEnabled(false);

        addProductWorkId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnViewQr.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        companyId = sharedPreferences.getLong(Constants.KEY_COMPANY_ID, -1);
        Log.d("ManageStoragesActivity", "companyId: " + companyId);

        if (companyId == -1) {
            Toast.makeText(this, "Не вдалося отримати компанію користувача", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        productService = new ProductService();
        categoryService = new CategoryService();
        storageService = new StorageService();
        transactionService = new TransactionService();
        inventoryService = new InventoryService();

        categoryList = new ArrayList<>();
        storageList = new ArrayList<>();

        loadCategories();
        loadStorages();
        setupSpinnerListeners();

        // Обробник натискання кнопки сканування QR
        btnScanQr.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivityForResult(intent, QR_SCAN_REQUEST_CODE);
        });

        // Обробник перегляду QR
        btnViewQr.setOnClickListener(v -> {
            String productWorkId = addProductWorkId.getText().toString().trim();
            if (!productWorkId.isEmpty()) {
                Intent intent = new Intent(this, ViewQrActivity.class);
                intent.putExtra(Constants.KEY_PRODUCT_WORK_ID, productWorkId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Спочатку введіть або відскануйте код", Toast.LENGTH_SHORT).show();
            }
        });
        addbtnSave.setOnClickListener(v -> saveProduct());
        addbtnCancel.setOnClickListener(v -> finish());
    }

    private void loadCategories() {
        categoryService.getAllCategories(new CategoryService.CategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                categoryList.clear();
                categoryList.addAll(categories);

                List<String> categoryNames = new ArrayList<>();
                for (Category c : categories) {
                    categoryNames.add(c.getName());
                }
                categoryNames.add(ADD_NEW_CATEGORY);
                categoryAdapter = new ArrayAdapter<>(AddProductActivity.this, R.layout.item_dropdown, categoryNames);
                categoryAdapter.setDropDownViewResource(R.layout.item_dropdown);
                addspnCategory.setAdapter(categoryAdapter);

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AddProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStorages() {
        storageService.getAllStorages(new StorageService.StorageCallback() {
            @Override
            public void onSuccess(List<Storage> storages) {
                storageList.clear();
                storageList.addAll(storages);

                List<String> storageNames = new ArrayList<>();
                for (Storage s : storages) {
                    storageNames.add(s.getName());
                }
                storageNames.add(ADD_NEW_STORAGE);
                storageAdapter = new ArrayAdapter<>(AddProductActivity.this, R.layout.item_dropdown, storageNames);
                storageAdapter.setDropDownViewResource(R.layout.item_dropdown);
                addspnStorage.setAdapter(storageAdapter);

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AddProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void scanQrCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Скануйте QR-код товару");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    private void enableManualCodeInput() {
        // Показуємо поле для ручного введення та кнопку перегляду QR
        findViewById(R.id.add_product_work_id).setVisibility(View.VISIBLE);
        btnViewQr.setVisibility(View.VISIBLE);
        // Приховуємо кнопку "Сканувати QR"
        btnScanQr.setVisibility(View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_SCAN_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String scannedCode = data.getStringExtra("SCANNED_CODE");
            if (scannedCode != null) {
                Log.d("AddProductActivity", "QR code from ScannerActivity: " + scannedCode);
                addProductWorkId.setText(scannedCode);
            } else {
                Log.d("AddProductActivity", "No scanned code found in result");
            }
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            // Додаємо 1 до місяця, бо він починається з 0
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            addExpirationDate.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }


    private void saveProduct() {
        String name = addName.getText().toString().trim();
        String description = addDescription.getText().toString().trim();
        String priceStr = addPrice.getText().toString().trim();
        String manufacturer = addManufacturer.getText().toString().trim();
        String expirationStr = addExpirationDate.getText().toString().trim(); // поле дати
        String weightStr = addWeight.getText().toString().trim();
        String dimensions = addDimensions.getText().toString().trim();
        String countStr = addCount.getText().toString().trim();
        String productWorkId = addProductWorkId.getText().toString().trim();

        // Перевірка обов'язкових полів
        if (name.isEmpty() || countStr.isEmpty() || productWorkId.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть обов'язкові поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Парсинг дати
        LocalDate expiration = null;
        if (!expirationStr.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                expiration = LocalDate.parse(expirationStr, formatter);
            } catch (DateTimeParseException e) {
                Toast.makeText(this, "Невірний формат дати (очікується yyyy-MM-dd)", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Парсинг ціни
        BigDecimal price = null;
        if (!priceStr.isEmpty()) {
            try {
                price = new BigDecimal(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Невірний формат ціни", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Парсинг ваги
        BigDecimal weight = null;
        if (!weightStr.isEmpty()) {
            try {
                weight = new BigDecimal(weightStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Невірний формат ваги", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Парсинг кількості
        Integer count = null;
        if (!countStr.isEmpty()) {
            try {
                count = Integer.parseInt(countStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Невірний формат кількості", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Вибір категорії
        Category selectedCategory = null;
        for (Category c : categoryList) {
            if (c.getName().equals(addspnCategory.getText().toString().trim())) {
                selectedCategory = c;
                break;
            }
        }

        // Вибір складу
        Storage selectedStorage = null;
        for (Storage s : storageList) {
            if (s.getName().equals(addspnStorage.getText().toString().trim())) {
                selectedStorage = s;
                break;
            }
        }

        if (selectedCategory == null || ADD_NEW_CATEGORY.equals(addspnCategory.getText().toString().trim())) {
            Toast.makeText(this, "Оберіть категорію зі списку", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedStorage == null || ADD_NEW_STORAGE.equals(addspnStorage.getText().toString().trim())) {
            Toast.makeText(this, "Оберіть склад зі списку", Toast.LENGTH_SHORT).show();
            return;
        }

        // Створення продукту
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setProductWorkId(productWorkId);
        newProduct.setPrice(price);
        newProduct.setCount(count);
        newProduct.setManufacturer(manufacturer);
        newProduct.setExpirationDate(expiration);
        newProduct.setWeight(weight);
        newProduct.setDimensions(dimensions);

        if (selectedCategory != null) newProduct.setCategoryId(selectedCategory.getCategoryId());
        if (selectedStorage != null) newProduct.setStorageId(selectedStorage.getStorageId());


        inventoryService.getActiveSessions(new InventoryService.InventorySessionListCallback() {
            @Override
            public void onSuccess(List<InventorySession> sessions) {
                Log.d(TAG, "getActiveSessions success, sessions count: " + (sessions != null ? sessions.size() : "null"));

                if (sessions != null && !sessions.isEmpty()) {
                    InventorySession activeSession = sessions.get(0); // беремо першу активну
                    Log.d(TAG, "Active session found: " + activeSession.getInventorySessionId());

                    showInventoryDialog(newProduct, activeSession);
                } else {
                    Log.d(TAG, "No active sessions found");

                    createAndSaveProduct(newProduct, null); // без інвентаризації
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "getActiveSessions failed: " + errorMessage);

                Toast.makeText(AddProductActivity.this, "Помилка перевірки інвентаризації", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showInventoryDialog(Product product, InventorySession session) {

        new AlertDialog.Builder(this)
                .setTitle("Активна інвентаризація")
                .setMessage("Зараз триває інвентаризація. Доданий товар також буде врахований у ній зі статусом 'Додано'. Продовжити?")
                .setPositiveButton("Так", (dialog, which) -> {
                    createAndSaveProduct(product, session); // з інвентаризацією
                })
                .setNegativeButton("Скасувати", null)
                .show();
    }

    private void createAndSaveProduct(Product newProduct, InventorySession session) {
        Log.d(TAG, "createAndSaveProduct called, session: " + (session != null ? session.getInventorySessionId() : "null"));

        productService.createProduct(newProduct, (success, createdProduct) -> {
            Log.d(TAG, "createProduct callback, success=" + success + ", productId=" + (createdProduct != null ? createdProduct.getProductId() : "null"));

            if (success && createdProduct != null) {
                addTransaction(createdProduct, TransactionTypeEnum.ARRIVAL, createdProduct.getCount());

                if (session != null) {
                    saveToInventoryRecords(createdProduct, session.getInventorySessionId());
                } else {
                    Toast.makeText(this, "Товар додано", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Log.e(TAG, "Failed to create product");

                Toast.makeText(this, "Помилка збереження товару", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToInventoryRecords(Product product, Long sessionId) {
        Log.d(TAG, "saveToInventoryRecords called with productId=" + product.getProductId() + ", sessionId=" + sessionId);
        if (sessionId == null) {
            Log.e(TAG, "SessionId is null in saveToInventoryRecords");

            Toast.makeText(this, "Не вдалося отримати ID інвентаризаційної сесії", Toast.LENGTH_SHORT).show();
            return;
        }
        // 1. InventoryResult
        InventoryResult inventoryResult = new InventoryResult();
        inventoryResult.setSession(new InventorySession(sessionId));
        Log.d(TAG, "Creating InventoryResult for sessionId=" + sessionId);
        inventoryResult.setProduct(new Product(product.getProductId()));
        inventoryResult.setStatusName(InventoryProductStatusEnum.ADDED);
        Log.d(TAG, "Setting InventoryResult status to ADDED for productId=" + product.getProductId());
        inventoryResult.setScannedBy(new Employee(getCurrentEmployee().getEmployeeId()));
        Log.d(TAG, "Setting InventoryResult scannedBy to employeeId=" + getCurrentEmployee().getEmployeeId());
        inventoryResult.setDescription("Додано під час активної інвентаризації");
        Log.d(TAG, "Adding InventoryResult to inventoryService");
        inventoryResult.setScanTime(LocalDateTime.now());

        inventoryService.addResult(inventoryResult, new InventoryService.InventoryActionCallback() {
            @Override
            public void onSuccess(boolean resultSuccess) {
                Log.d(TAG, "addResult success: " + resultSuccess);

                if (!resultSuccess) {
                    Log.e(TAG, "addResult returned false success");

                    Toast.makeText(AddProductActivity.this, "Помилка збереження InventoryResult", Toast.LENGTH_SHORT).show();
                    return;
                }

                // DTO частина
                InventoryProductResultDto dto = new InventoryProductResultDto();
                dto.setInventorySessionId(sessionId);
                dto.setProductId(product.getProductId());
                dto.setProductName(product.getName());
                dto.setProductDescription(product.getDescription());
                dto.setProductWorkId(product.getProductWorkId());
                dto.setProductCount(product.getCount());
                dto.setCategoryName(getCategoryNameById(product.getCategoryId()));
                dto.setStorageName(getStorageNameById(product.getStorageId()));
                dto.setPrice(product.getPrice());
                dto.setCount(product.getCount());
                dto.setManufacturer(product.getManufacturer());
                dto.setExpirationDate(product.getExpirationDate());
                dto.setWeight(product.getWeight());
                dto.setDimensions(product.getDimensions());
                dto.setStatus(InventoryProductStatusEnum.ADDED);
                dto.setScannedBy(getCurrentEmployee().getEmployeeId());
                dto.setScanTime(LocalDateTime.now());
                dto.setDescription("Новий товар під час інвентаризації");
                Log.d(TAG, "Saving DTO via inventoryService");

                inventoryService.saveOrUpdateInventoryProductResultDto(sessionId, dto, new InventoryService.InventoryActionCallback() {
                    @Override
                    public void onSuccess(boolean dtoSuccess) {
                        Log.d(TAG, "saveOrUpdateInventoryProductResultDto success: " + dtoSuccess);

                        if (dtoSuccess) {
                            Toast.makeText(AddProductActivity.this, "Товар додано з урахуванням інвентаризації", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.e(TAG, "saveOrUpdateInventoryProductResultDto returned false success");

                            Toast.makeText(AddProductActivity.this, "Помилка збереження DTO", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e(TAG, "saveOrUpdateInventoryProductResultDto failed: " + errorMessage);

                        Toast.makeText(AddProductActivity.this, "Помилка збереження DTO: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "addResult failed: " + errorMessage);

                Toast.makeText(AddProductActivity.this, "Помилка збереження InventoryResult: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getCategoryNameById(Long id) {
        for (Category c : categoryList) {
            if (c.getCategoryId().equals(id)) return c.getName();
        }
        return "Категорія";
    }

    private String getStorageNameById(Long id) {
        for (Storage s : storageList) {
            if (s.getStorageId().equals(id)) return s.getName();
        }
        return "Склад";
    }




    private void addTransaction(Product product, TransactionTypeEnum action, int quantity) {
        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setType(action); // без .valueOf
        transaction.setEmployee(getCurrentEmployee());
        transaction.setQuantity(quantity);
        transaction.setTransactionDate(LocalDateTime.now());

        transactionService.createTransaction(transaction, success -> {
            if (!success) {
                Toast.makeText(this, "Помилка запису в історію руху товарів", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Транзакція збережена", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Employee getCurrentEmployee() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long employeeId = prefs.getLong(Constants.KEY_EMPLOYEE_ID, -1);
        if (employeeId == -1) return null;

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        return employee;
    }


    private void setupSpinnerListeners() {
        addspnCategory.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            if (ADD_NEW_CATEGORY.equals(selected)) {
                showAddCategoryDialog();
            }
        });

        addspnStorage.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            if (ADD_NEW_STORAGE.equals(selected)) {
                showAddStorageDialog();
            }
        });
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Нова категорія");

        View dialogView = getLayoutInflater().inflate(R.layout.add_category_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        EditText editTextCategoryName = dialogView.findViewById(R.id.editTextCategoryName);
        EditText editTextCategoryDescription = dialogView.findViewById(R.id.editTextCategoryDescription);
        Button btnSave = dialogView.findViewById(R.id.btn_save_category);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel_category);

        btnSave.setOnClickListener(v -> {
            String name = editTextCategoryName.getText().toString().trim();
            String description = editTextCategoryDescription.getText().toString().trim();

            if (name.isEmpty()) {
                editTextCategoryName.setError("Назва обов'язкова");
                return;
            }

            Category category = new Category(name, description);
            categoryService.createCategory(category, new CategoryService.SingleCategoryCallback() {
                @Override
                public void onSuccess(Category category) {
                    Toast.makeText(AddProductActivity.this, "Категорію створено", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadCategories();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(AddProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void showAddStorageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Новий склад");

        View dialogView = getLayoutInflater().inflate(R.layout.add_storage_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        EditText editTextLocation = dialogView.findViewById(R.id.editTextLocation);
        Button btnSave = dialogView.findViewById(R.id.btn_save_storage);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel_storage);

        btnSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String details = editTextDescription.getText().toString().trim();
            String location = editTextLocation.getText().toString().trim();

            if (name.isEmpty()) {
                editTextName.setError("Назва обов'язкова");
                return;
            }

            Company company = new Company();
            company.setCompanyId(companyId);

            Storage storage = new Storage(company, name, location, details);
            storageService.createStorage(storage, new StorageService.SingleStorageCallback() {
                @Override
                public void onSuccess(Storage storage) {
                    Toast.makeText(AddProductActivity.this, "Склад створено", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadStorages();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(AddProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

}