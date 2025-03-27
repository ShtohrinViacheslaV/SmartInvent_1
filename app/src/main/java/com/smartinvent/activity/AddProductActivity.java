//package com.smartinvent.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.*;
//import androidx.appcompat.app.AppCompatActivity;
//import com.smartinvent.R;
//import com.smartinvent.model.Category;
//import com.smartinvent.model.Product;
//import com.smartinvent.model.Storage;
//import com.smartinvent.service.ProductService;
//import com.smartinvent.utils.QrCodeUtils;
//import java.util.List;
//
//public class EditProductActivity extends AppCompatActivity {
//
//    private EditText edtName, edtDescription, edtCount, edtProductWorkId;
//    private Spinner spinnerCategory, spinnerStorage;
//    private Button btnSave, btnCancel, btnViewQrCode;
//    private ImageView imgQrCode;
//    private Product product;
//    private List<Category> categoryList;
//    private List<Storage> storageList;
//    private ProductService productService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_product);
//
//        edtName = findViewById(R.id.edt_name);
//        edtDescription = findViewById(R.id.edt_description);
//        edtProductWorkId = findViewById(R.id.edt_product_work_id);
//        edtCount = findViewById(R.id.edt_count);
//        spinnerCategory = findViewById(R.id.edt_spn_category);
//        spinnerStorage = findViewById(R.id.edt_spn_storage);
//        btnSave = findViewById(R.id.btn_edt_save);
//        btnCancel = findViewById(R.id.btn_edt_cancel);
//        btnViewQrCode = findViewById(R.id.btn_view_qr_code);
//        imgQrCode = findViewById(R.id.img_qr_code);
//
//        productService = new ProductService();
//
//        // Отримуємо передані дані
//        product = getIntent().getParcelableExtra("product");
//        categoryList = getIntent().getParcelableArrayListExtra("categories");
//        storageList = getIntent().getParcelableArrayListExtra("storage");
//
//        if (product != null) {
//            edtName.setText(product.getName());
//            edtDescription.setText(product.getDescription());
//            edtProductWorkId.setText(product.getProductWorkId());
//            edtCount.setText(String.valueOf(product.getCount()));
//
//            setupSpinner(spinnerCategory, categoryList, product.getCategoryId());
//            setupSpinner(spinnerStorage, storageList, product.getStorageId());
//        }
//
//        btnViewQrCode.setOnClickListener(v -> showQrCode());
//        btnSave.setOnClickListener(v -> saveProduct());
//        btnCancel.setOnClickListener(v -> finish());
//    }
//
//    private void setupSpinner(Spinner spinner, List<?> list, Long selectedId) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        int selectedIndex = -1;
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i) instanceof Category) {
//                Category category = (Category) list.get(i);
//                adapter.add(category.getName());
//                if (category.getCategoryId().equals(selectedId)) {
//                    selectedIndex = i;
//                }
//            } else if (list.get(i) instanceof Storage) {
//                Storage storage = (Storage) list.get(i);
//                adapter.add(storage.getName());
//                if (storage.getStorageId().equals(selectedId)) {
//                    selectedIndex = i;
//                }
//            }
//        }
//
//        spinner.setAdapter(adapter);
//        if (selectedIndex != -1) {
//            spinner.setSelection(selectedIndex);
//        }
//    }
//
//    private void showQrCode() {
//        if (product != null) {
//            String qrData = product.getProductWorkId();
//            imgQrCode.setImageBitmap(QrCodeUtils.generateQrBitmap(qrData));
//        } else {
//            Toast.makeText(this, "Товар не знайдено", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void saveProduct() {
//        String name = edtName.getText().toString().trim();
//        String description = edtDescription.getText().toString().trim();
//        String productWorkId = edtProductWorkId.getText().toString().trim();
//        String countStr = edtCount.getText().toString().trim();
//
//        if (name.isEmpty() || countStr.isEmpty() || productWorkId.isEmpty()) {
//            Toast.makeText(this, "Заповніть всі поля!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        int count = Integer.parseInt(countStr);
//        Category selectedCategory = categoryList.get(spinnerCategory.getSelectedItemPosition());
//        Storage selectedStorage = storageList.get(spinnerStorage.getSelectedItemPosition());
//
//        product.setName(name);
//        product.setDescription(description);
//        product.setProductWorkId(productWorkId);
//        product.setCount(count);
//        product.setCategoryId(selectedCategory.getCategoryId());
//        product.setStorageId(selectedStorage.getStorageId());
//
//        productService.updateProduct(product, success -> {
//            if (success) {
//                Toast.makeText(this, "Зміни збережено!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.putExtra("product", product);
//                setResult(RESULT_OK, intent);
//                finish();
//            } else {
//                Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}



package com.smartinvent.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.smartinvent.R;
import com.smartinvent.model.Category;
import com.smartinvent.model.Product;
import com.smartinvent.model.Storage;
import com.smartinvent.model.Transaction;
import com.smartinvent.service.*;
import com.smartinvent.utils.QrCodeUtils;
import retrofit2.Callback;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private EditText addName, addDescription, addCount, addProductWorkId;
    private ImageView imgQrCode;
    private MaterialAutoCompleteTextView addspnCategory, addspnStorage;
    private Button btnScanQr, btnGenerateQr, addbtnSave, addbtnCancel;

    private byte[] qrCodeBytes;
    private Product product;
    private ProductService productService;
    private CategoryService categoryService;
    private StorageService storageService;
    private TransactionService transactionService;
    private StorageApi storageApi;

    private List<Category> categoryList;
    private List<Storage> storageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        addName = findViewById(R.id.add_name);
        addDescription = findViewById(R.id.add_description);
        addCount = findViewById(R.id.add_count);
        addProductWorkId = findViewById(R.id.add_product_work_id);
        imgQrCode = findViewById(R.id.img_qr_code);
        addspnCategory = findViewById(R.id.add_spn_category);
        addspnStorage = findViewById(R.id.add_spn_storage);
        btnScanQr = findViewById(R.id.btn_scan_qr);
        btnGenerateQr = findViewById(R.id.btn_generate_qr);
        addbtnSave = findViewById(R.id.btn_add_save);
        addbtnCancel = findViewById(R.id.btn_add_cancel);

        productService = new ProductService();
        categoryService = new CategoryService();
        storageService = new StorageService();
        transactionService = new TransactionService();


        categoryList = new ArrayList<>();
        storageList = new ArrayList<>();

        loadCategories();
        loadStorages();

        btnScanQr.setOnClickListener(v -> scanQrCode());
        btnGenerateQr.setOnClickListener(v -> generateQrCode());
        addbtnSave.setOnClickListener(v -> saveProduct());
        addbtnCancel.setOnClickListener(v -> finish());
    }


    private void loadCategories() {
        Log.d("DEBUG", "Запит на отримання категорій...");

        categoryService.getAllCategories(new CategoryService.CategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                Log.d("DEBUG", "Отримано категорії: " + categories.size());

                categoryList.clear(); // Очищаємо, щоб не було дублікатів

                if (categories != null && !categories.isEmpty()) {
                    categoryList.addAll(categories);
                    List<String> categoryNames = new ArrayList<>();
                    for (Category category : categories) {
                        categoryNames.add(category.getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            AddProductActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            categoryNames
                    );
                    addspnCategory.setThreshold(1);
                    addspnCategory.setAdapter(adapter);
                } else {
                    Log.w("AddProductActivity", "⚠️ Категорії відсутні");
                    addspnCategory.setAdapter(null); // Очищаємо адаптер, якщо категорій немає
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("API_ERROR", "❌ Помилка завантаження категорій" + errorMessage);
                Toast.makeText(AddProductActivity.this, "Помилка завантаження категорій", Toast.LENGTH_SHORT).show();
                categoryList.clear();
                addspnCategory.setAdapter(null);
            }
        });
    }


    private void loadStorages() {
        Log.d("DEBUG", "Запит на отримання складів...");
        storageService.getAllStorages(new StorageService.StorageCallback() {
            @Override
            public void onSuccess(List<Storage> storages) {
                Log.d("DEBUG", "Отримано склади: " + storages.size());

                storageList.clear();
                Log.d("DEBUG", "storageList.clear()...");

                if (storages != null && !storages.isEmpty()) {
                    Log.d("DEBUG", "storages != null && !storages.isEmpty()...");

                    storageList.addAll(storages);
                    List<String> storageNames = new ArrayList<>();
                    for (Storage storage : storages) {
                        storageNames.add(storage.getName());
                    }
                    Log.d("DEBUG", "ArrayAdapter...");

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            AddProductActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            storageNames
                    );
                    Log.d("DEBUG", "setAdapter...");

                    addspnStorage.setThreshold(1);
                    addspnStorage.setAdapter(adapter);
                } else {
                    Log.w("AddProductActivity", "⚠️ Склади відсутні");
                    addspnStorage.setAdapter(null);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("API_ERROR", "❌ Помилка завантаження складів" + errorMessage);
                Toast.makeText(AddProductActivity.this, "Помилка завантаження складів", Toast.LENGTH_SHORT).show();
                storageList.clear();
                addspnStorage.setAdapter(null);
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

    private void generateQrCode() {
        String productWorkId = addProductWorkId.getText().toString().trim();

        if (productWorkId.isEmpty()) {
            Toast.makeText(this, "Спочатку введіть код товару!", Toast.LENGTH_SHORT).show();
            return;
        }

        qrCodeBytes = productWorkId.getBytes(StandardCharsets.UTF_8);
        Bitmap qrBitmap = QrCodeUtils.generateQrBitmap(productWorkId);
        imgQrCode.setImageBitmap(qrBitmap);
    }

    private void saveProduct() {
        String name = addName.getText().toString().trim();
        String description = addDescription.getText().toString().trim();
        String productWorkId = addProductWorkId.getText().toString().trim();
        String countStr = addCount.getText().toString().trim();

        if (name.isEmpty() || countStr.isEmpty() || productWorkId.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть всі обов'язкові поля", Toast.LENGTH_SHORT).show();
            return;
        }

        int count = Integer.parseInt(countStr);
        Category selectedCategory = null;
        Storage selectedStorage = null;

        String selectedCategoryName = addspnCategory.getText().toString().trim();
        if (!selectedCategoryName.isEmpty() && categoryList != null) {
            for (Category category : categoryList) {
                if (category.getName().equals(selectedCategoryName)) {
                    selectedCategory = category;
                    break;
                }
            }
        }

        String selectedStorageName = addspnStorage.getText().toString().trim();
        if (!selectedStorageName.isEmpty() && storageList != null) {
            for (Storage storage : storageList) {
                if (storage.getName().equals(selectedStorageName)) {
                    selectedStorage = storage;
                    break;
                }
            }
        }

        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setProductWorkId(productWorkId);
        newProduct.setCount(count);

        if (selectedCategory != null) {
            newProduct.setCategoryId(selectedCategory.getCategoryId());
        } else {
            Log.w("saveProduct", "⚠️ Категорія не вибрана, буде null");
        }

        if (selectedStorage != null) {
            newProduct.setStorageId(selectedStorage.getStorageId());
        } else {
            Log.w("saveProduct", "⚠️ Склад не вибраний, буде null");
        }

        if (qrCodeBytes == null) {
            qrCodeBytes = productWorkId.getBytes(StandardCharsets.UTF_8);
        }
        newProduct.setQrCode(Base64.encodeToString(qrCodeBytes, Base64.DEFAULT));

        productService.createProduct(newProduct, new ProductService.ProductCallback() {
            @Override
            public void onSuccess(boolean success, Product createdProduct) {
                if (success && createdProduct != null) {
                    addTransaction(createdProduct.getProductId(), "Додавання");
                    Toast.makeText(AddProductActivity.this, "Товар додано!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddProductActivity.this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



        private void addTransaction(Long productId, String action) {
        Transaction transaction = new Transaction(productId, action, System.currentTimeMillis());
        transactionService.createTransaction(transaction, success -> {
            if (!success) {
                Toast.makeText(this, "Помилка запису в історію руху товарів", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



//
//
////package com.smartinvent.activity;
////
////import android.content.Intent;
////import android.os.Bundle;
////import android.util.Base64;
////import android.widget.*;
////import androidx.appcompat.app.AppCompatActivity;
////import com.google.zxing.integration.android.IntentIntegrator;
////import com.google.zxing.integration.android.IntentResult;
////import com.smartinvent.R;
////import com.smartinvent.model.Category;
////import com.smartinvent.model.Product;
////import com.smartinvent.model.Storage;
////import com.smartinvent.model.Transaction;
////import com.smartinvent.service.CategoryService;
////import com.smartinvent.service.ProductService;
////import com.smartinvent.service.StorageService;
////import com.smartinvent.service.TransactionService;
////import com.smartinvent.utils.QrCodeUtils;
////import java.nio.charset.StandardCharsets;
////import java.util.List;
////
////public class AddProductActivity extends AppCompatActivity {
////
////    private EditText edtName, edtDescription, edtCount;
////    private ImageView imgQrCode;
////    private Spinner spnCategory, spnStorage;
////    private Button btnScanQr, btnGenerateQr, btnSaveProduct, btnCancel;
////    private byte[] qrCodeBytes;
////    private ProductService productService;
////    private CategoryService categoryService;
////    private StorageService storageService;
////    private TransactionService transactionService;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_add_product);
////
////        // Ініціалізація елементів
////        edtName = findViewById(R.id.edt_name);
////        edtDescription = findViewById(R.id.edt_description);
////        edtCount = findViewById(R.id.edt_count);
////        imgQrCode = findViewById(R.id.img_qr_code);
////        spnCategory = findViewById(R.id.spn_category);
////        spnStorage = findViewById(R.id.spn_storage);
////        btnScanQr = findViewById(R.id.btn_scan_qr);
////        btnGenerateQr = findViewById(R.id.btn_generate_qr);
////        btnSaveProduct = findViewById(R.id.btn_save_product);
////        btnCancel = findViewById(R.id.btn_cancel);
////
////        productService = new ProductService();
////        categoryService = new CategoryService();
////        storageService = new StorageService();
////        transactionService = new TransactionService();
////
////        loadCategories();
////        loadStorages();
////
////        btnScanQr.setOnClickListener(v -> scanQrCode());
////        btnGenerateQr.setOnClickListener(v -> generateQrCode());
////        btnSaveProduct.setOnClickListener(v -> saveProduct());
////        btnCancel.setOnClickListener(v -> finish());
////    }
////
////    private void loadCategories() {
////        categoryService.getAllCategories(new CategoryService.CategoryCallback() {
////            @Override
////            public void onSuccess(List<Category> categories) {
////                ArrayAdapter<Category> adapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_item, categories);
////                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                spnCategory.setAdapter(adapter);
////            }
////
////            @Override
////            public void onFailure(String errorMessage) {
////                Toast.makeText(AddProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
////
////    private void loadStorages() {
////        storageService.getAllStorages(new StorageService.StorageCallback() {
////            @Override
////            public void onSuccess(List<Storage> storages) {
////                ArrayAdapter<Storage> adapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_item, storages);
////                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                spnStorage.setAdapter(adapter);
////            }
////
////            @Override
////            public void onFailure(String errorMessage) {
////                Toast.makeText(AddProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
////
////    private void scanQrCode() {
////        IntentIntegrator integrator = new IntentIntegrator(this);
////        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
////        integrator.setPrompt("Скануйте QR-код товару");
////        integrator.setCameraId(0);
////        integrator.setBeepEnabled(false);
////        integrator.initiateScan();
////    }
////
////    private void generateQrCode() {
////        String generatedQr = "QR-" + System.currentTimeMillis();
////        qrCodeBytes = generatedQr.getBytes(StandardCharsets.UTF_8);
////
////        if (qrCodeBytes == null || qrCodeBytes.length == 0) {
////            Toast.makeText(this, "Помилка генерації QR-коду!", Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        imgQrCode.setImageBitmap(QrCodeUtils.generateQrBitmap(generatedQr));
////        Toast.makeText(this, "QR-код згенеровано!", Toast.LENGTH_SHORT).show();
////    }
////
////    private void saveProduct() {
////        String name = edtName.getText().toString().trim();
////        String description = edtDescription.getText().toString().trim();
////        String countStr = edtCount.getText().toString().trim();
////
////        if (name.isEmpty() || countStr.isEmpty() || qrCodeBytes == null) {
////            Toast.makeText(this, "Заповніть всі поля та додайте QR-код!", Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        int count = Integer.parseInt(countStr);
////        Category selectedCategory = (Category) spnCategory.getSelectedItem();
////        Storage selectedStorage = (Storage) spnStorage.getSelectedItem();
////
////        if (selectedCategory == null || selectedStorage == null) {
////            Toast.makeText(this, "Оберіть категорію та склад!", Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        String qrCodeString = Base64.encodeToString(qrCodeBytes, Base64.DEFAULT);
////        Product product = new Product(name, description, qrCodeString, count, selectedCategory.getCategoryId(), selectedStorage.getStorageId());
////
////        productService.isQrCodeUnique(qrCodeString, isUnique -> {
////            if (!isUnique) {
////                qrCodeBytes = null; // Очищення QR-коду, щоб змусити користувача згенерувати новий
////                Toast.makeText(this, "QR-код вже використовується! Згенеруйте інший", Toast.LENGTH_SHORT).show();
////                return;
////            }
////
////            productService.createProduct(product, success -> {
////                if (success) {
////                    Toast.makeText(this, "Товар додано!", Toast.LENGTH_SHORT).show();
////                    addTransaction(product.getProductId(), "Створення");
////                    finish();
////                } else {
////                    Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
////                }
////            });
////        });
////    }
////
////    private void addTransaction(Long productId, String action) {
////        Transaction transaction = new Transaction(productId, action, System.currentTimeMillis());
////
////        transactionService.createTransaction(transaction, success -> {
////            if (!success) {
////                Toast.makeText(this, "Помилка запису в історію руху товарів", Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
////}
////
////
////
//////package com.smartinvent.activity;
//////
//////import android.content.Intent;
//////import android.os.Bundle;
//////import android.widget.*;
//////import androidx.appcompat.app.AppCompatActivity;
//////import com.google.zxing.integration.android.IntentIntegrator;
//////import com.google.zxing.integration.android.IntentResult;
//////import com.smartinvent.R;
//////import com.smartinvent.model.Category;
//////import com.smartinvent.model.Product;
//////import com.smartinvent.model.Storage;
//////import com.smartinvent.model.Transaction;
//////import com.smartinvent.service.CategoryService;
//////import com.smartinvent.service.ProductService;
//////import com.smartinvent.service.StorageService;
//////import com.smartinvent.service.TransactionService;
//////import com.smartinvent.utils.QrCodeUtils;
//////import java.nio.charset.StandardCharsets;
//////import java.util.List;
//////
//////public class AddProductActivity extends AppCompatActivity {
//////
//////    private EditText edtName, edtDescription, edtCount;
//////    private ImageView imgQrCode;
//////    private Spinner spnCategory, spnStorage;
//////    private Button btnScanQr, btnGenerateQr, btnSaveProduct, btnCancel;
//////    private byte[] qrCodeBytes;
//////    private ProductService productService;
//////    private CategoryService categoryService;
//////    private StorageService storageService;
//////    private TransactionService transactionService;
//////
//////    @Override
//////    protected void onCreate(Bundle savedInstanceState) {
//////        super.onCreate(savedInstanceState);
//////        setContentView(R.layout.activity_add_product);
//////
//////        // Ініціалізація елементів
//////        edtName = findViewById(R.id.edt_name);
//////        edtDescription = findViewById(R.id.edt_description);
//////        edtCount = findViewById(R.id.edt_count);
//////        imgQrCode = findViewById(R.id.img_qr_code);
//////        spnCategory = findViewById(R.id.spn_category);
//////        spnStorage = findViewById(R.id.spn_storage);
//////        btnScanQr = findViewById(R.id.btn_scan_qr);
//////        btnGenerateQr = findViewById(R.id.btn_generate_qr);
//////        btnSaveProduct = findViewById(R.id.btn_save_product);
//////        btnCancel = findViewById(R.id.btn_cancel);
//////
//////        productService = new ProductService();
//////        categoryService = new CategoryService();
//////        storageService = new StorageService();
//////        transactionService = new TransactionService();
//////
//////        loadCategories();
//////        loadStorages();
//////
//////        btnScanQr.setOnClickListener(v -> {
//////            IntentIntegrator integrator = new IntentIntegrator(this);
//////            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
//////            integrator.setPrompt("Скануйте QR-код товару");
//////            integrator.setCameraId(0);
//////            integrator.setBeepEnabled(false);
//////            integrator.initiateScan();
//////        });
//////
//////        btnGenerateQr.setOnClickListener(v -> generateQrCode());
//////        btnSaveProduct.setOnClickListener(v -> saveProduct());
//////        btnCancel.setOnClickListener(v -> finish());
//////    }
//////
//////    private void loadCategories() {
//////        categoryService.getAllCategories(new CategoryService.CategoryCallback() {
//////            @Override
//////            public void onSuccess(List<Category> categories) {
//////                ArrayAdapter<Category> adapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_item, categories);
//////                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//////                spnCategory.setAdapter(adapter);
//////            }
//////
//////            @Override
//////            public void onFailure(String errorMessage) {
//////                Toast.makeText(AddProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//////            }
//////        });
//////    }
//////
//////    private void loadStorages() {
//////        storageService.getAllStorages(new StorageService.StorageCallback() {
//////            @Override
//////            public void onSuccess(List<Storage> storages) {
//////                ArrayAdapter<Storage> adapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_item, storages);
//////                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//////                spnStorage.setAdapter(adapter);
//////            }
//////
//////            @Override
//////            public void onFailure(String errorMessage) {
//////                Toast.makeText(AddProductActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//////            }
//////        });
//////    }
//////
//////    private void saveProduct() {
//////        String name = edtName.getText().toString().trim();
//////        String description = edtDescription.getText().toString().trim();
//////        String countStr = edtCount.getText().toString().trim();
//////
//////        if (name.isEmpty() || countStr.isEmpty() || qrCodeBytes == null) {
//////            Toast.makeText(this, "Заповніть всі поля та додайте QR-код!", Toast.LENGTH_SHORT).show();
//////            return;
//////        }
//////
//////        int count = Integer.parseInt(countStr);
//////        Category selectedCategory = (Category) spnCategory.getSelectedItem();
//////        Storage selectedStorage = (Storage) spnStorage.getSelectedItem();
//////
//////        Product product = new Product(name, description, qrCodeBytes, count, selectedCategory.getCategoryId(), selectedStorage.getStorageId());
//////
//////        productService.isQrCodeUnique(new String(qrCodeBytes, StandardCharsets.UTF_8), isUnique -> {
//////            if (!isUnique) {
//////                Toast.makeText(this, "QR-код вже використовується!", Toast.LENGTH_SHORT).show();
//////                return;
//////            }
//////
//////            productService.createProduct(product, success -> {
//////                if (success) {
//////                    Toast.makeText(this, "Товар додано!", Toast.LENGTH_SHORT).show();
//////                    addTransaction(product.getProductId(), "Створення");
//////                    finish();
//////                } else {
//////                    Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
//////                }
//////            });
//////        });
//////    }
//////
//////
//////    // Генерація унікального QR-коду
//////    private void generateQrCode() {
//////        String generatedQr = "QR-" + System.currentTimeMillis();
//////        qrCodeBytes = generatedQr.getBytes(StandardCharsets.UTF_8);
//////
//////        imgQrCode.setImageBitmap(QrCodeUtils.generateQrBitmap(generatedQr));
//////        Toast.makeText(this, "QR-код згенеровано!", Toast.LENGTH_SHORT).show();
//////    }
//////
//////
//////    private void addTransaction(Long productId, String action) {
//////        Transaction transaction = new Transaction(productId, action, System.currentTimeMillis());
//////
//////        transactionService.createTransaction(transaction, success -> {
//////            if (!success) {
//////                Toast.makeText(this, "Помилка запису в історію руху товарів", Toast.LENGTH_SHORT).show();
//////            }
//////        });
//////    }
//////}