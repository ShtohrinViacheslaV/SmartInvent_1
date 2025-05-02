package com.smartinvent.activity;





import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smartinvent.R;
import com.smartinvent.adapter.InventorySessionProductAdapter;
import com.smartinvent.model.InventorySessionProduct;
import com.smartinvent.network.ApiClient;
import com.smartinvent.service.InventoryApi;
import com.smartinvent.service.InventoryService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class InventorySessionDetailsActivity extends AppCompatActivity {

    private Button btnScanProduct, btnManualSearch;
    private Spinner spinnerStatus, spinnerStorage, spinnerCategory;
    private RecyclerView recyclerViewProducts;

    private InventorySessionProductAdapter productAdapter;
    private InventoryService inventoryService;

    private Long sessionId;

    private List<InventorySessionProduct> allProducts = new ArrayList<>(); // Повний список

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_session_details);

        btnScanProduct = findViewById(R.id.btnScanProduct);
        btnManualSearch = findViewById(R.id.btnManualSearch);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerStorage = findViewById(R.id.spinnerStorage);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

        sessionId = getIntent().getLongExtra("SESSION_ID", -1);
        if (sessionId == -1) {
            Toast.makeText(this, "Невірна сесія інвентаризації", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        inventoryService = new InventoryService();

        setupRecyclerView();
        setupListeners();
        loadInventorySessionProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInventorySessionProducts();
    }

    private void setupRecyclerView() {
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new InventorySessionProductAdapter(this::onProductClicked, inventoryService);
        recyclerViewProducts.setAdapter(productAdapter);
    }

    private void setupListeners() {
        btnScanProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductScannerActivity.class);
            intent.putExtra("SESSION_ID", sessionId);
            startActivity(intent);
        });

        btnManualSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManualProductSearchActivity.class);
            intent.putExtra("SESSION_ID", sessionId);
            startActivity(intent);
        });

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        };

        spinnerStatus.setOnItemSelectedListener(filterListener);
        spinnerStorage.setOnItemSelectedListener(filterListener);
        spinnerCategory.setOnItemSelectedListener(filterListener);
    }

    private void onProductClicked(InventorySessionProduct product) {
        if (product.isLocked()) {
            Toast.makeText(this, "Товар зараз сканується іншим користувачем", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ProductInventoryActivity.class);
        intent.putExtra("SESSION_ID", sessionId);
        intent.putExtra("PRODUCT_ID", product.getProductId());
        startActivity(intent);
    }

    private void loadInventorySessionProducts() {
        InventoryApi api = ApiClient.getClient().create(InventoryApi.class);
        api.getProductsForSession(sessionId).enqueue(new Callback<List<InventorySessionProduct>>() {
            @Override
            public void onResponse(Call<List<InventorySessionProduct>> call, Response<List<InventorySessionProduct>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allProducts = response.body();
                    setupFilterSpinners(allProducts);
                    applyFilters();
                } else {
                    Toast.makeText(InventorySessionDetailsActivity.this, "Не вдалося завантажити товари", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<InventorySessionProduct>> call, Throwable t) {
                Toast.makeText(InventorySessionDetailsActivity.this, "Помилка з'єднання: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFilterSpinners(List<InventorySessionProduct> products) {
        Set<String> statuses = new TreeSet<>();
        Set<String> storages = new TreeSet<>();
        Set<String> categories = new TreeSet<>();

        statuses.add("Усі");
        storages.add("Усі");
        categories.add("Усі");

        for (InventorySessionProduct product : products) {
            if (product.getStatus() != null) statuses.add(product.getStatus());
            if (product.getStorageName() != null) storages.add(product.getStorageName());
            if (product.getCategoryName() != null) categories.add(product.getCategoryName());
        }

        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(statuses)));
        spinnerStorage.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(storages)));
        spinnerCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(categories)));
    }

    private void applyFilters() {
        String selectedStatus = spinnerStatus.getSelectedItem().toString();
        String selectedStorage = spinnerStorage.getSelectedItem().toString();
        String selectedCategory = spinnerCategory.getSelectedItem().toString();

        List<InventorySessionProduct> filtered = new ArrayList<>();
        for (InventorySessionProduct product : allProducts) {
            boolean matchStatus = selectedStatus.equals("Усі") || selectedStatus.equalsIgnoreCase(product.getStatus());
            boolean matchStorage = selectedStorage.equals("Усі") || selectedStorage.equalsIgnoreCase(product.getStorageName());
            boolean matchCategory = selectedCategory.equals("Усі") || selectedCategory.equalsIgnoreCase(product.getCategoryName());

            if (matchStatus && matchStorage && matchCategory) {
                filtered.add(product);
            }
        }

        productAdapter.setProducts(filtered);
    }
}

//
//public class InventorySessionDetailsActivity extends AppCompatActivity {
//
//    private Button btnScanProduct, btnManualSearch;
//    private Spinner spinnerStatus, spinnerStorage, spinnerCategory;
//    private RecyclerView recyclerViewProducts;
//    private InventorySessionProductAdapter productAdapter;
//    private InventoryService inventoryService;  // Додаємо InventoryService
//
//    private Long sessionId; // ID сесії інвентаризації
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_inventory_session_details);
//
//        btnScanProduct = findViewById(R.id.btnScanProduct);
//        btnManualSearch = findViewById(R.id.btnManualSearch);
//        spinnerStatus = findViewById(R.id.spinnerStatus);
//        spinnerStorage = findViewById(R.id.spinnerStorage);
//        spinnerCategory = findViewById(R.id.spinnerCategory);
//        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
//
//        sessionId = getIntent().getLongExtra("SESSION_ID", -1);
//        if (sessionId == -1) {
//            Toast.makeText(this, "Невірна сесія інвентаризації", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // Ініціалізація inventoryService
//        inventoryService = new InventoryService();
//
//        setupRecyclerView();
//        setupListeners();
//        loadInventorySessionProducts();
//    }
//
//    private void setupRecyclerView() {
//        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
//
//        // Передача inventoryService разом з OnProductClickListener
//        productAdapter = new InventorySessionProductAdapter(this::onProductClicked, inventoryService);
//        recyclerViewProducts.setAdapter(productAdapter);
//    }
//
//    private void setupListeners() {
//        btnScanProduct.setOnClickListener(v -> {
//            Intent intent = new Intent(this, ProductScannerActivity.class);
//            intent.putExtra("SESSION_ID", sessionId);
//            startActivity(intent);
//        });
//
//        btnManualSearch.setOnClickListener(v -> {
//            Intent intent = new Intent(this, ManualProductSearchActivity.class);
//            intent.putExtra("SESSION_ID", sessionId);
//            startActivity(intent);
//        });
//    }
//
//    private void onProductClicked(InventorySessionProduct product) {
//        if (inventoryService.isLockedByAnotherUser(product)) {
//            Toast.makeText(this, "Товар зараз сканується іншим користувачем", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Intent intent = new Intent(this, ProductInventoryActivity.class);
//        intent.putExtra("SESSION_ID", sessionId);
//        intent.putExtra("PRODUCT_ID", product.getProductId());
//        startActivity(intent);
//    }
//
//    private void loadInventorySessionProducts() {
//        InventoryApi api = ApiClient.getClient().create(InventoryApi.class);
//        api.getProductsForSession(sessionId).enqueue(new Callback<List<InventorySessionProduct>>() {
//            @Override
//            public void onResponse(Call<List<InventorySessionProduct>> call, Response<List<InventorySessionProduct>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    productAdapter.setProducts(response.body());
//                } else {
//                    Toast.makeText(InventorySessionDetailsActivity.this, "Не вдалося завантажити товари", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<InventorySessionProduct>> call, Throwable t) {
//                Toast.makeText(InventorySessionDetailsActivity.this, "Помилка з'єднання: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
