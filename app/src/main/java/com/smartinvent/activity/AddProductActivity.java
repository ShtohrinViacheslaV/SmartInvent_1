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
import android.os.Bundle;
import android.util.Base64;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.smartinvent.R;
import com.smartinvent.model.Category;
import com.smartinvent.model.Product;
import com.smartinvent.model.Storage;
import com.smartinvent.model.Transaction;
import com.smartinvent.service.CategoryService;
import com.smartinvent.service.ProductService;
import com.smartinvent.service.StorageService;
import com.smartinvent.service.TransactionService;
import com.smartinvent.utils.QrCodeUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private EditText addName, addDescription, addCount, addProductWorkId;
    private ImageView imgQrCode;
    private Spinner addspnCategory, addspnStorage;
    private Button btnScanQr, btnGenerateQr, addbtnSave, addbtnCancel;
    private byte[] qrCodeBytes;
    private Product product;
    private ProductService productService;
    private CategoryService categoryService;
    private StorageService storageService;
    private TransactionService transactionService;

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

        loadCategories();
        loadStorages();

        btnScanQr.setOnClickListener(v -> scanQrCode());
        btnGenerateQr.setOnClickListener(v -> generateQrCode());
        addbtnSave.setOnClickListener(v -> saveProduct());
        addbtnCancel.setOnClickListener(v -> finish());
    }

    private void loadCategories() {
        categoryService.getAllCategories(new CategoryService.CategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                        AddProductActivity.this,
                        android.R.layout.simple_spinner_item,
                        categories
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addspnCategory.setAdapter(adapter);
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
                ArrayAdapter<Storage> adapter = new ArrayAdapter<>(
                        AddProductActivity.this,
                        android.R.layout.simple_spinner_item,
                        storages
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addspnStorage.setAdapter(adapter);
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

    private void generateQrCode() {
        String generatedQr = "QR-" + System.currentTimeMillis();
        qrCodeBytes = generatedQr.getBytes(StandardCharsets.UTF_8);
        imgQrCode.setImageBitmap(QrCodeUtils.generateQrBitmap(generatedQr));
    }

    private void saveProduct() {
        String name = addName.getText().toString().trim();
        String description = addDescription.getText().toString().trim();
        String productWorkId = "PW-" + System.currentTimeMillis(); // Можливо, потрібно запитати у користувача
        String countStr = addCount.getText().toString().trim();

        if (name.isEmpty() || countStr.isEmpty() || productWorkId.isEmpty()) {
            Toast.makeText(this, "Заповніть всі поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        int count = Integer.parseInt(countStr);
        Category selectedCategory = (Category) addspnCategory.getSelectedItem();
        Storage selectedStorage = (Storage) addspnStorage.getSelectedItem();

        if (selectedCategory == null || selectedStorage == null) {
            Toast.makeText(this, "Оберіть категорію та склад!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Product product = new Product(name, description, productWorkId, count, qrCodeBytes,selectedCategory.getCategoryId(), selectedStorage.getStorageId());
        product.setName(name);
        product.setDescription(description);
        product.setProductWorkId(productWorkId);
        product.setCount(count);
        product.setCategoryId(selectedCategory.getCategoryId());
        product.setStorageId(selectedStorage.getStorageId());


        productService.createProduct(product, success -> {
            if (success) {
                Toast.makeText(this, "Товар додано!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
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