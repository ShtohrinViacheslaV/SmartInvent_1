package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.R;
import com.smartinvent.model.Category;
import com.smartinvent.model.Product;
import com.smartinvent.model.Storage;
import com.smartinvent.service.ProductService;
import com.smartinvent.utils.QrCodeUtils;
import java.util.List;

public class EditProductActivity extends AppCompatActivity {

    private EditText edtName, edtDescription, edtCount, edtProductWorkId;
    private Spinner spinnerCategory, spinnerStorage;
    private Button btnSave, btnCancel, btnViewQrCode;
    private ImageView imgQrCode;
    private Product product;
    private List<Category> categoryList;
    private List<Storage> storageList;
    private ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        edtName = findViewById(R.id.edt_name);
        edtDescription = findViewById(R.id.edt_description);
        edtProductWorkId = findViewById(R.id.edt_product_work_id);
        edtCount = findViewById(R.id.edt_count);
        spinnerCategory = findViewById(R.id.edt_spn_category);
        spinnerStorage = findViewById(R.id.edt_spn_storage);
        btnSave = findViewById(R.id.btn_edt_save);
        btnCancel = findViewById(R.id.btn_edt_cancel);
        btnViewQrCode = findViewById(R.id.btn_view_qr_code);
        //imgQrCode = findViewById(R.id.img_qr_code);

        productService = new ProductService();

        // Отримуємо передані дані
        product = getIntent().getParcelableExtra("product");
        categoryList = getIntent().getParcelableArrayListExtra("categories");
        storageList = getIntent().getParcelableArrayListExtra("storage");

        if (product != null) {
            edtName.setText(product.getName());
            edtDescription.setText(product.getDescription());
            edtProductWorkId.setText(product.getProductWorkId());
            edtCount.setText(String.valueOf(product.getCount()));

            setupSpinner(spinnerCategory, categoryList, product.getCategoryId());
            setupSpinner(spinnerStorage, storageList, product.getStorageId());
        }

        btnViewQrCode.setOnClickListener(v -> showQrCode());
        btnSave.setOnClickListener(v -> saveProduct());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupSpinner(Spinner spinner, List<?> list, Long selectedId) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        int selectedIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof Category) {
                Category category = (Category) list.get(i);
                adapter.add(category.getName());
                if (category.getCategoryId().equals(selectedId)) {
                    selectedIndex = i;
                }
            } else if (list.get(i) instanceof Storage) {
                Storage storage = (Storage) list.get(i);
                adapter.add(storage.getName());
                if (storage.getStorageId().equals(selectedId)) {
                    selectedIndex = i;
                }
            }
        }

        spinner.setAdapter(adapter);
        if (selectedIndex != -1) {
            spinner.setSelection(selectedIndex);
        }
    }

    private void showQrCode() {
        if (product != null) {
            String qrData = product.getProductWorkId();
            imgQrCode.setImageBitmap(QrCodeUtils.generateQrBitmap(qrData));
        } else {
            Toast.makeText(this, "Товар не знайдено", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProduct() {
        String name = edtName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String productWorkId = edtProductWorkId.getText().toString().trim();
        String countStr = edtCount.getText().toString().trim();

        if (name.isEmpty() || countStr.isEmpty() || productWorkId.isEmpty()) {
            Toast.makeText(this, "Заповніть всі поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        int count = Integer.parseInt(countStr);
        Category selectedCategory = categoryList.get(spinnerCategory.getSelectedItemPosition());
        Storage selectedStorage = storageList.get(spinnerStorage.getSelectedItemPosition());

        product.setName(name);
        product.setDescription(description);
        product.setProductWorkId(productWorkId);
        product.setCount(count);
        product.setCategoryId(selectedCategory.getCategoryId());
        product.setStorageId(selectedStorage.getStorageId());

        productService.updateProduct(product, success -> {
            if (success) {
                Toast.makeText(this, "Зміни збережено!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("product", product);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
