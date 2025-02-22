package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.smartinvent.R;
import com.smartinvent.adapter.ProductAdapter;
import com.smartinvent.model.Product;
import com.smartinvent.network.ApiClient;
import com.smartinvent.service.ProductApi;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProductApi productApi;
    private EditText searchInput;
    private Button btnScanQr;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerView = findViewById(R.id.recyclerView);
        searchInput = findViewById(R.id.search_input);
        btnScanQr = findViewById(R.id.btn_scan_qr);
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(null, product ->
                Toast.makeText(ProductActivity.this, "Вибрано: " + product.getName(), Toast.LENGTH_SHORT).show()
        );
        recyclerView.setAdapter(productAdapter);

        productApi = ApiClient.getClient().create(ProductApi.class);
        loadProducts();

        // Обробник натискання кнопки сканування
        btnScanQr.setOnClickListener(v -> startQrScanner());

        // Обробка натискання на панель навігації
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_products) {
                return true; // Ми вже тут
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(ProductActivity.this, UserHomeActivity.class));
                return true;
            } else if (itemId == R.id.nav_inventory) {
                startActivity(new Intent(ProductActivity.this, InventoryActivity.class));
                return true;
            } else if (itemId == R.id.nav_scan) {
                startQrScanner();
                return true;
            } else if (itemId == R.id.nav_more) {
                startActivity(new Intent(ProductActivity.this, MoreActivity.class));
                return true;
            }
            return false;
        });
    }

    private void loadProducts() {
        productApi.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productAdapter.setProducts(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductActivity.this, "Помилка завантаження", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startQrScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Наведіть камеру на QR-код товару");
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedCode = result.getContents();
                searchInput.setText(scannedCode);
                searchProduct(scannedCode);
            } else {
                Toast.makeText(this, "Сканування скасовано", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void searchProduct(String productId) {
        productApi.searchProducts(productId).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    productAdapter.setProducts(response.body());
                } else {
                    Toast.makeText(ProductActivity.this, "Товар не знайдено", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductActivity.this, "Помилка пошуку товару", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



//package com.smartinvent.activity;
//
//import android.os.Bundle;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.smartinvent.R;
//import com.smartinvent.adapter.ProductAdapter;
//import com.smartinvent.model.Product;
//import com.smartinvent.network.ApiClient;
//import com.smartinvent.service.ProductService;
//import java.util.List;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ProductActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private ProductAdapter productAdapter;
//    private ProductService productService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        productAdapter = new ProductAdapter(null, product -> {
//            // Дії при натисканні на товар
//            Toast.makeText(ProductActivity.this, "Вибрано: " + product.getName(), Toast.LENGTH_SHORT).show();
//        });
//        recyclerView.setAdapter(productAdapter);
//
//        productService = ApiClient.getClient().create(ProductService.class);
//        loadProducts();
//    }
//
//    private void loadProducts() {
//        productService.getAllProducts().enqueue(new Callback<List<Product>>() {
//            @Override
//            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    productAdapter.setProducts(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Product>> call, Throwable t) {
//                Toast.makeText(ProductActivity.this, "Помилка завантаження", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
