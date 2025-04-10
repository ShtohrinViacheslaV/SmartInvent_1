package com.smartinvent.activity;

import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ArrayAdapter;import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
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
import java.util.ArrayList;
import java.util.List;

public class EditProductActivity extends AppCompatActivity {

    private EditText edtName, edtDescription, edtCount, edtProductWorkId;
    private MaterialAutoCompleteTextView editspnCategory, editspnStorage;
    private Button btnSave, btnCancel, btnViewQrCode;
    private ImageView imgQrCode;

    private Product product;
    private ProductService productService;
    private CategoryService categoryService;
    private StorageService storageService;
    private TransactionService transactionService;

    private List<Category> categoryList = new ArrayList<>();
    private List<Storage> storageList = new ArrayList<>();
    private byte[] qrCodeBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        edtName = findViewById(R.id.edt_name);
        edtDescription = findViewById(R.id.edt_description);
        edtProductWorkId = findViewById(R.id.edt_product_work_id);
        edtCount = findViewById(R.id.edt_count);
        editspnCategory = findViewById(R.id.edt_spn_category);
        editspnStorage = findViewById(R.id.edt_spn_storage);
        btnSave = findViewById(R.id.btn_edt_save);
        btnCancel = findViewById(R.id.btn_edt_cancel);
        btnViewQrCode = findViewById(R.id.btn_view_qr_code);

        productService = new ProductService();
        categoryService = new CategoryService();
        storageService = new StorageService();
        transactionService = new TransactionService();

        product = getIntent().getParcelableExtra("product");

        if (product != null) {
            fillProductDetails();
        }

        loadCategories();
        loadStorages();

        btnViewQrCode.setOnClickListener(v -> showQrCode());
        btnSave.setOnClickListener(v -> saveProduct());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void fillProductDetails() {
        edtName.setText(product.getName());
        edtDescription.setText(product.getDescription());
        edtProductWorkId.setText(product.getProductWorkId());
        edtCount.setText(String.valueOf(product.getCount()));
        qrCodeBytes = Base64.decode(product.getQrCode(), Base64.DEFAULT);
    }

    private void loadCategories() {
        categoryService.getAllCategories(new CategoryService.CategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                categoryList.clear();
                if (categories != null) {
                    categoryList.addAll(categories);
                    final List<String> categoryNames = new ArrayList<>();
                    for (Category category : categories) {
                        categoryNames.add(category.getName());
                    }

                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProductActivity.this,
                            android.R.layout.simple_dropdown_item_1line, categoryNames);
                    editspnCategory.setAdapter(adapter);

                    if (product != null) {
                        for (Category category : categoryList) {
                            if (category.getCategoryId().equals(product.getCategoryId())) {
                                editspnCategory.setText(category.getName(), false);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(EditProductActivity.this, "Помилка завантаження категорій", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStorages() {
        storageService.getAllStorages(new StorageService.StorageCallback() {
            @Override
            public void onSuccess(List<Storage> storages) {
                storageList.clear();
                if (storages != null) {
                    storageList.addAll(storages);
                    final List<String> storageNames = new ArrayList<>();
                    for (Storage storage : storages) {
                        storageNames.add(storage.getName());
                    }

                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProductActivity.this,
                            android.R.layout.simple_dropdown_item_1line, storageNames);
                    editspnStorage.setAdapter(adapter);

                    if (product != null) {
                        for (Storage storage : storageList) {
                            if (storage.getStorageId().equals(product.getStorageId())) {
                                editspnStorage.setText(storage.getName(), false);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(EditProductActivity.this, "Помилка завантаження складів", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQrCode() {
        if (product != null) {
            imgQrCode.setImageBitmap(QrCodeUtils.generateQrBitmap(product.getProductWorkId()));
        } else {
            Toast.makeText(this, "QR-код недоступний", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProduct() {
        final String name = edtName.getText().toString().trim();
        final String description = edtDescription.getText().toString().trim();
        final String productWorkId = edtProductWorkId.getText().toString().trim();
        final String countStr = edtCount.getText().toString().trim();

        if (name.isEmpty() || countStr.isEmpty() || productWorkId.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть всі обов'язкові поля", Toast.LENGTH_SHORT).show();
            return;
        }

        final int count = Integer.parseInt(countStr);
        Category selectedCategory = null;
        Storage selectedStorage = null;

        final String selectedCategoryName = editspnCategory.getText().toString().trim();
        if (!selectedCategoryName.isEmpty()) {
            for (Category category : categoryList) {
                if (category.getName().equals(selectedCategoryName)) {
                    selectedCategory = category;
                    break;
                }
            }
        }

        final String selectedStorageName = editspnStorage.getText().toString().trim();
        if (!selectedStorageName.isEmpty()) {
            for (Storage storage : storageList) {
                if (storage.getName().equals(selectedStorageName)) {
                    selectedStorage = storage;
                    break;
                }
            }
        }

        product.setName(name);
        product.setDescription(description);
        product.setProductWorkId(productWorkId);
        product.setCount(count);

        if (selectedCategory != null) {
            product.setCategoryId(selectedCategory.getCategoryId());
        }

        if (selectedStorage != null) {
            product.setStorageId(selectedStorage.getStorageId());
        }

        if (qrCodeBytes == null) {
            qrCodeBytes = productWorkId.getBytes(StandardCharsets.UTF_8);
        }
        product.setQrCode(Base64.encodeToString(qrCodeBytes, Base64.DEFAULT));

        productService.updateProduct(product, new ProductService.ProductCallback() {
            @Override
            public void onSuccess(boolean success, Product updatedProduct) {
                if (success) {
                    addTransaction(product.getProductId(), "Редагування");
                    Toast.makeText(EditProductActivity.this, "Товар оновлено!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProductActivity.this, "Помилка оновлення!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addTransaction(Long productId, String action) {
        final Transaction transaction = new Transaction(productId, action, System.currentTimeMillis());
        transactionService.createTransaction(transaction, success -> {
            if (!success) {
                Toast.makeText(this, "Помилка запису в історію руху товарів", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
