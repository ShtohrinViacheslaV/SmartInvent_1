package com.smartinvent.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.smartinvent.R;
import com.smartinvent.model.*;
import com.smartinvent.service.CategoryService;
import com.smartinvent.service.ProductService;
import com.smartinvent.service.StorageService;
import com.smartinvent.service.TransactionService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class EditProductActivity extends AppCompatActivity {

    private EditText editName, editDescription, editProductWorkId, editPrice, editCount, editManufacturer, editExpirationDate, editWeight, editDimensions;
    private MaterialAutoCompleteTextView edtspnCategory, edtspnStorage;
    private Button edtbtnSave, edtbtnCancel, btnScanQr, btnViewQr;

    private ProductService productService;
    private CategoryService categoryService;
    private StorageService storageService;
    private TransactionService transactionService;

    private static final String ADD_NEW_CATEGORY = "➕ Додати нову категорію";
    private static final String ADD_NEW_STORAGE = "➕ Додати новий склад";

    private List<Category> categoryList;
    private List<Storage> storageList;
    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> storageAdapter;

    private static final int QR_SCAN_REQUEST_CODE = 1001;


    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        editName = findViewById(R.id.edt_name);
        editDescription = findViewById(R.id.edt_description);
        editProductWorkId = findViewById(R.id.edt_product_work_id);
        editPrice = findViewById(R.id.edt_price);
        editManufacturer = findViewById(R.id.edt_manufacturer);
        editExpirationDate = findViewById(R.id.edt_expiration_date);
        editExpirationDate.setInputType(InputType.TYPE_NULL); // Забороняє клавіатуру
        editExpirationDate.setOnClickListener(v -> showDatePicker());
        editWeight = findViewById(R.id.edt_weight);
        editDimensions = findViewById(R.id.edt_dimensions);
        editCount = findViewById(R.id.edt_count);
        edtspnCategory = findViewById(R.id.edt_spn_category);
        edtspnStorage = findViewById(R.id.edt_spn_storage);

        btnScanQr = findViewById(R.id.btn_scan_qr);
        btnViewQr  = findViewById(R.id.btn_view_qr);
        edtbtnSave = findViewById(R.id.btn_edt_save);
        edtbtnCancel = findViewById(R.id.btn_edt_cancel);

        btnViewQr.setEnabled(false);


        editProductWorkId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnViewQr.setEnabled(!s.toString().trim().isEmpty());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        productService = new ProductService();
        categoryService = new CategoryService();
        storageService = new StorageService();
        transactionService = new TransactionService();


        categoryList = new ArrayList<>();
        storageList = new ArrayList<>();


        loadCategories();
        loadStorages();
        setupSpinnerListeners();


        btnScanQr.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivityForResult(intent, QR_SCAN_REQUEST_CODE);
        });

        btnViewQr.setOnClickListener(v -> {
            String productWorkId = editProductWorkId.getText().toString().trim();
            if (!productWorkId.isEmpty()) {
                Intent intent = new Intent(this, ViewQrActivity.class);
                intent.putExtra(Constants.KEY_PRODUCT_WORK_ID, productWorkId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Спочатку введіть або відскануйте код", Toast.LENGTH_SHORT).show();
            }
        });

        edtbtnSave.setOnClickListener(v -> updateProduct());
        edtbtnCancel.setOnClickListener(v -> finish());


        currentProduct = getIntent().getParcelableExtra("product");
        if (currentProduct != null) {
            populateFields(currentProduct);
        } else{
                Toast.makeText(this, "❌ Дані продукту не передані", Toast.LENGTH_SHORT).show();
                finish();
        }
    }

    private void populateFields(Product product) {
        editName.setText(product.getName());
        editDescription.setText(product.getDescription());
        editProductWorkId.setText(product.getProductWorkId());
        editPrice.setText(product.getPrice() != null ? product.getPrice().toString() : "");
        editManufacturer.setText(product.getManufacturer());
        editExpirationDate.setText(product.getExpirationDate() != null ? product.getExpirationDate().toString() : "");
        editWeight.setText(product.getWeight() != null ? product.getWeight().toString() : "");
        editDimensions.setText(product.getDimensions());
        editCount.setText(product.getCount() != null ? String.valueOf(product.getCount()) : "");
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        String currentDate = editExpirationDate.getText().toString();
        if (!currentDate.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = sdf.parse(currentDate);
                if (date != null) {
                    calendar.setTime(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            editExpirationDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
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

                categoryAdapter = new ArrayAdapter<>(EditProductActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edtspnCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(EditProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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

                storageAdapter = new ArrayAdapter<>(EditProductActivity.this, android.R.layout.simple_spinner_item, storageNames);
                storageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edtspnStorage.setAdapter(storageAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(EditProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            editProductWorkId.setText(result.getContents());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateProduct() {
        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String priceStr = editPrice.getText().toString().trim();
        String manufacturer = editManufacturer.getText().toString().trim();
        String expirationStr = editExpirationDate.getText().toString().trim(); // поле дати

        String weightStr = editWeight.getText().toString().trim();
        String dimensions = editDimensions.getText().toString().trim();
        String countStr = editCount.getText().toString().trim();
        String productWorkId = editProductWorkId.getText().toString().trim();

        // Перевірка обов'язкових полів
        if (name.isEmpty() || countStr.isEmpty() || productWorkId.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть обов'язкові поля", Toast.LENGTH_SHORT).show();
            return;
        }


        BigDecimal price = null;
        if (!priceStr.isEmpty()) {
            try {
                price = new BigDecimal(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Невірний формат ціни", Toast.LENGTH_SHORT).show();
                return;
            }
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

        BigDecimal weight = null;
        if (!weightStr.isEmpty()) {
            try {
                weight = new BigDecimal(weightStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Невірний формат ваги", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Integer count = null;
        if (!countStr.isEmpty()) {
            try {
                count = Integer.parseInt(countStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Невірний формат кількості", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Category selectedCategory = null;
        Storage selectedStorage = null;

        for (Category c : categoryList) {
            if (c.getName().equals(edtspnCategory.getText().toString().trim())) {
                selectedCategory = c;
                break;
            }
        }

        for (Storage s : storageList) {
            if (s.getName().equals(edtspnStorage.getText().toString().trim())) {
                selectedStorage = s;
                break;
            }
        }

        // Оновлення поточного продукту
        currentProduct.setName(name);
        currentProduct.setDescription(description);
        currentProduct.setProductWorkId(productWorkId);
        currentProduct.setPrice(price);
        currentProduct.setCount(count);
        currentProduct.setManufacturer(manufacturer);
        currentProduct.setExpirationDate(expiration);
        currentProduct.setWeight(weight);
        currentProduct.setDimensions(dimensions);


        if (selectedCategory != null) currentProduct.setCategoryId(selectedCategory.getCategoryId());
        if (selectedStorage != null) currentProduct.setStorageId(selectedStorage.getStorageId());

        productService.updateProduct(currentProduct, (success, updatedProduct) -> {
            if (success  && currentProduct != null) {
                addTransaction(currentProduct, TransactionTypeEnum.UPDATE, currentProduct.getCount());
                Toast.makeText(this, "Товар оновлено!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Помилка при оновленні", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addTransaction(Product product, TransactionTypeEnum action, int quantity) {
        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setType(action);
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
        edtspnStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (ADD_NEW_CATEGORY.equals(selected)) {
                    showCreateCategoryDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        edtspnStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (ADD_NEW_STORAGE.equals(selected)) {
                    showCreateStorageDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void showCreateCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Нова категорія");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Додати", (dialog, which) -> {
            String name = input.getText().toString().trim();
            if (!name.isEmpty()) {
                Category category = new Category();
                category.setName(name);
                categoryService.createCategory(category, new CategoryService.SingleCategoryCallback() {
                    @Override
                    public void onSuccess(Category createdCategory) {
                        loadCategories(); // оновити список
                        Toast.makeText(EditProductActivity.this, "Категорію створено", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(EditProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Скасувати", (dialog, which) -> dialog.cancel());
        builder.show();
    }


    private void showCreateStorageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Новий склад");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Додати", (dialog, which) -> {
            String name = input.getText().toString().trim();
            if (!name.isEmpty()) {
                Storage storage = new Storage();
                storage.setName(name);
                storageService.createStorage(storage, new StorageService.SingleStorageCallback() {
                    @Override
                    public void onSuccess(Storage createdStorage) {
                        loadStorages(); // оновити список
                        Toast.makeText(EditProductActivity.this, "Склад створено", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(EditProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Скасувати", (dialog, which) -> dialog.cancel());
        builder.show();
    }

}







