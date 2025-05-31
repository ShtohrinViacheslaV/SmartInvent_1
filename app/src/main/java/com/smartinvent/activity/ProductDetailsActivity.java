package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.R;
import com.smartinvent.model.Category;
import com.smartinvent.model.Product;
import com.smartinvent.service.CategoryService;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView txtName, txtDescription, txtProductWorkId, txtCount, txtCategory, txtStorage;
    private ImageView imgQrCode;
    private Button btnEditProduct, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Ініціалізація елементів UI
        txtName = findViewById(R.id.txt_name);
        txtDescription = findViewById(R.id.txt_description);
        txtProductWorkId = findViewById(R.id.txt_product_work_id);
        txtCount = findViewById(R.id.txt_count);
        txtCategory = findViewById(R.id.txt_category);
        txtStorage = findViewById(R.id.txt_storage);
        imgQrCode = findViewById(R.id.img_qr_code);

        btnEditProduct = findViewById(R.id.btn_edit_product);
        btnBack = findViewById(R.id.btn_back);

        // Отримання переданого товару
        Product product = getIntent().getParcelableExtra("product");
        if (product != null) {
            txtName.setText(product.getName());
            txtDescription.setText(product.getDescription());
            txtProductWorkId.setText("Код роботи: " + product.getProductWorkId());
            txtCount.setText("Кількість: " + product.getCount());
            txtCategory.setText("Категорія: " + (product.getCategory() != null ? product.getCategory().getName() : "Немає"));
            txtStorage.setText("Склад: " + (product.getStorage() != null ? product.getStorage().getName() : "Немає"));

        }


        btnEditProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProductActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }

}

