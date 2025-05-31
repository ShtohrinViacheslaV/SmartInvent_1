package com.smartinvent.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.smartinvent.R;
import com.smartinvent.model.*;
import com.smartinvent.service.*;
import com.smartinvent.utils.QrCodeUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;




public class AddProductActivity extends AppCompatActivity {

    private EditText addName, addDescription, addProductWorkId, addPrice, addCount, addManufacturer, addExpirationDate, addWeight, addDimensions;
    private MaterialAutoCompleteTextView addspnCategory, addspnStorage;
    private Button btnScanQr, btnViewQr, addbtnSave, addbtnCancel;

    private ProductService productService;
    private CategoryService categoryService;
    private StorageService storageService;
    private TransactionService transactionService;

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

        companyId = sharedPreferences.getLong("companyId", -1);
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
            String productCode = addProductWorkId.getText().toString().trim();
            if (!productCode.isEmpty()) {
                Intent intent = new Intent(this, ViewQrActivity.class);
                intent.putExtra("PRODUCT_CODE", productCode);
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

//                categoryAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_item, categoryNames);
//                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                addspnCategory.setAdapter(categoryAdapter);
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

//                storageAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_item, storageNames);
//                storageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                addspnStorage.setAdapter(storageAdapter);

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


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null && result.getContents() != null) {
//            Log.d("AddProductActivity", "QR code scanned: " + result.getContents());
//            addProductWorkId.setText(result.getContents());
//        }
//    else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

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


    private void saveProduct() {
        String name = addName.getText().toString().trim();
        String description = addDescription.getText().toString().trim();
        String priceStr = addPrice.getText().toString().trim();
        String manufacturer = addManufacturer.getText().toString().trim();
        String expirationDate = addExpirationDate.getText().toString().trim();
        String weightStr = addWeight.getText().toString().trim();
        String dimensions = addDimensions.getText().toString().trim();
        String countStr = addCount.getText().toString().trim();
        String productWorkId = addProductWorkId.getText().toString().trim();

        // Перевірка обов'язкових полів
        if (name.isEmpty() || countStr.isEmpty() || productWorkId.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть обов'язкові поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Якщо ціна заповнена, конвертуємо її в BigDecimal
        BigDecimal price = null;
        if (!priceStr.isEmpty()) {
            try {
                price = new BigDecimal(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Невірний формат ціни", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Якщо вага заповнена, конвертуємо її в BigDecimal
        BigDecimal weight = null;
        if (!weightStr.isEmpty()) {
            try {
                weight = new BigDecimal(weightStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Невірний формат ваги", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Якщо кількість заповнена, конвертуємо її в Integer
        Integer count = null;
        if (!countStr.isEmpty()) {
            try {
                count = new Integer(countStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Невірний формат ціни", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Category selectedCategory = null;
        Storage selectedStorage = null;

        // Перевірка та вибір категорії
        for (Category c : categoryList) {
            if (c.getName().equals(addspnCategory.getText().toString().trim())) {
                selectedCategory = c;
                break;
            }
        }

        // Перевірка та вибір складу
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


        // Створення нового продукту
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setProductWorkId(productWorkId);
        newProduct.setPrice(price);
        newProduct.setCount(count);
        newProduct.setManufacturer(manufacturer);
        newProduct.setExpirationDate(expirationDate);
        newProduct.setWeight(weight);
        newProduct.setDimensions(dimensions);

        // Призначення категорії та складу, якщо вибрано
        if (selectedCategory != null) newProduct.setCategoryId(selectedCategory.getCategoryId());
        if (selectedStorage != null) newProduct.setStorageId(selectedStorage.getStorageId());

        // Створення товару через сервіс
        productService.createProduct(newProduct, (success, createdProduct) -> {
            if (success && createdProduct != null) {
                addTransaction(createdProduct, TransactionTypeEnum.ARRIVAL, createdProduct.getCount());
                Toast.makeText(this, "Товар додано!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
            }
        });
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
        long employeeId = prefs.getLong("employeeId", -1);
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

//    private void showAddCategoryDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Нова категорія");
//
//        View dialogView = getLayoutInflater().inflate(R.layout.add_category_dialog, null);
//        builder.setView(dialogView);
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//        EditText editTextCategoryName = dialogView.findViewById(R.id.editTextCategoryName);
//        EditText editTextCategoryDescription = dialogView.findViewById(R.id.editTextCategoryDescription);
//
//        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Створити", (d, which) -> {
//        });
//        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Скасувати", (d, which) -> dialog.dismiss());
//
//        dialog.setOnShowListener(d -> {
//            Button createButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//            createButton.setOnClickListener(v -> {
//                String name = editTextCategoryName.getText().toString().trim();
//                String description = editTextCategoryDescription.getText().toString().trim();
//
//                if (name.isEmpty()) {
//                    editTextCategoryName.setError("Назва обов'язкова");
//                    return;
//                }
//
//                Category category = new Category(name, description);
//                categoryService.createCategory(category, new CategoryService.SingleCategoryCallback() {
//                    @Override
//                    public void onSuccess(Category category) {
//                        Toast.makeText(AddProductActivity.this, "Категорію створено", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                        loadCategories();
//                    }
//
//                    @Override
//                    public void onFailure(String errorMessage) {
//                        Toast.makeText(AddProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            });
//        });
//    }
//
//    private void showAddStorageDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Новий склад");
//
//        View dialogView = getLayoutInflater().inflate(R.layout.add_storage_dialog, null);
//        builder.setView(dialogView);
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//        EditText editTextName = dialogView.findViewById(R.id.editTextName);
//        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
//        EditText editTextLocation = dialogView.findViewById(R.id.editTextLocation);
//
//        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Створити", (d, which) -> {
//        });
//        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Скасувати", (d, which) -> dialog.dismiss());
//
//        dialog.setOnShowListener(d -> {
//            Button createButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//            createButton.setOnClickListener(v -> {
//                String name = editTextName.getText().toString().trim();
//                String details = editTextDescription.getText().toString().trim();
//                String location = editTextLocation.getText().toString().trim();
//
//                if (name.isEmpty()) {
//                    editTextName.setError("Назва обов'язкова");
//                    return;
//                }
//
//
//                Company company = new Company();
//                company.setCompanyId(companyId);
//
//
//                Storage storage = new Storage(company, name, location, details);
//
//                storageService.createStorage(storage, new StorageService.SingleStorageCallback() {
//                    @Override
//                    public void onSuccess(Storage storage) {
//                        Toast.makeText(AddProductActivity.this, "Склад створено", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                        loadCategories();
//                    }
//
//                    @Override
//                    public void onFailure(String errorMessage) {
//                        Toast.makeText(AddProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            });
//        });
//    }




