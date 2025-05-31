package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.smartinvent.R;
import com.smartinvent.adapter.InventorySessionProductAdapter;
import com.smartinvent.fragment.ManualProductSearchDialogFragment;
import com.smartinvent.model.*;
import com.smartinvent.network.ApiClient;
import com.smartinvent.service.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class InventoryProductListActivity extends AppCompatActivity {

    private MaterialButton btnScanProduct, btnManualSearch;
    private MaterialAutoCompleteTextView spinnerStatus, spinnerStorage, spinnerCategory;
    private RecyclerView recyclerViewProducts;
    private SwipeRefreshLayout swipeRefreshLayout;

    private InventorySessionProductAdapter adapter;
    private List<InventoryProductResultDto> allProducts = new ArrayList<>();

    private InventoryService inventoryService;
    private CategoryService categoryService;
    private StorageService storageService;

    private List<Category> categoryList = new ArrayList<>();
    private List<Storage> storageList = new ArrayList<>();

    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> storageAdapter;
    private ArrayAdapter<String> statusAdapter;


    private Long inventorySessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_product_list);

        inventorySessionId = getIntent().getLongExtra(Constants.KEY_INVENTORY_SESSION_ID, -1);
        Log.d("SessionID InventoryProductListActivity", inventorySessionId + "");

        Log.d("IntentDebug", "Received intent: " + getIntent().toString());

        categoryService = new CategoryService();
        storageService = new StorageService();
        inventoryService = new InventoryService();

        categoryList = new ArrayList<>();
        storageList = new ArrayList<>();

        initViews();
        setupListeners();
        loadFilters();  // Завантаження всіх фільтрів
        loadProducts(); // Початкове завантаження
        loadCategories(); // Завантаження категорій
        loadStorages(); // Завантаження складів

    }

    private void initViews() {
        btnScanProduct = findViewById(R.id.btnScanProduct);
        btnManualSearch = findViewById(R.id.btnManualSearch);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerStorage = findViewById(R.id.spinnerStorage);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        adapter = new InventorySessionProductAdapter(new ArrayList<>(), this);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducts.setAdapter(adapter);
    }

    private void setupListeners() {
        swipeRefreshLayout.setOnRefreshListener(this::loadProducts);

        btnScanProduct.setOnClickListener(v -> {
            Intent intent = new Intent(this, InventoryScannerActivity.class);
            intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, inventorySessionId);
            startActivity(intent);

        });

        btnManualSearch.setOnClickListener(v -> {
            ManualProductSearchDialogFragment dialog = ManualProductSearchDialogFragment.newInstance(inventorySessionId);
            dialog.setOnProductSelectedListener(product -> {
                // Передача productId і product в InventoryProductCheckActivity
                Intent intent = new Intent(InventoryProductListActivity.this, InventoryProductCheckActivity.class);
                intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, inventorySessionId);
                intent.putExtra(Constants.KEY_PRODUCT_ID, product.getProductId());
                intent.putExtra("product", product);  // якщо потрібен Parcelable
                startActivity(intent);
            });
            dialog.show(getSupportFragmentManager(), "manualSearchDialog");
        });


        spinnerStatus.setOnItemClickListener((parent, view, position, id) -> filterProducts());
        spinnerStorage.setOnItemClickListener((parent, view, position, id) -> filterProducts());
        spinnerCategory.setOnItemClickListener((parent, view, position, id) -> filterProducts());

    }

    private void loadFilters() {
        loadCategories();
        loadStorages();
        loadStatuses();
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

                categoryAdapter = new ArrayAdapter<>(InventoryProductListActivity.this, R.layout.item_dropdown, categoryNames);
                categoryAdapter.setDropDownViewResource(R.layout.item_dropdown);
                spinnerCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(InventoryProductListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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

                storageAdapter = new ArrayAdapter<>(InventoryProductListActivity.this, R.layout.item_dropdown, storageNames);
                storageAdapter.setDropDownViewResource(R.layout.item_dropdown);
                spinnerStorage.setAdapter(storageAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(InventoryProductListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStatuses() {
        List<String> statusNames = new ArrayList<>();
        for (InventoryProductStatusEnum status : InventoryProductStatusEnum.values()) {
            statusNames.add(status.getDescription());
        }

        statusAdapter = new ArrayAdapter<>(InventoryProductListActivity.this, R.layout.item_dropdown, statusNames);
        statusAdapter.setDropDownViewResource(R.layout.item_dropdown);
        spinnerStatus.setAdapter(statusAdapter);
    }

    private void loadProducts() {
        swipeRefreshLayout.setRefreshing(true);
        inventoryService.getInventoryResultsBySession(inventorySessionId, new InventoryService.InventoryProductResultCallback() {
            @Override
            public void onSuccess(List<InventoryProductResultDto> products) {
                swipeRefreshLayout.setRefreshing(false);
                allProducts = products;
                filterProducts(); // застосовуємо фільтр або оновлюємо UI
            }

            @Override
            public void onFailure(String errorMessage) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void filterProducts() {
        String status = spinnerStatus.getText().toString().trim();
        String storage = spinnerStorage.getText().toString().trim();
        String category = spinnerCategory.getText().toString().trim();

        List<InventoryProductResultDto> filtered = new ArrayList<>();
        for (InventoryProductResultDto p : allProducts) {
            // Фільтрація по статусу
            if (!TextUtils.isEmpty(status)) {
                InventoryProductStatusEnum productStatus = p.getStatus();
                if (productStatus == null || !productStatus.name().equalsIgnoreCase(status)) continue;
            }

            // Фільтрація по складу
            if (!TextUtils.isEmpty(storage)) {
                if (p.getStorageName() == null || !p.getStorageName().equalsIgnoreCase(storage)) continue;
            }

            // Фільтрація по категорії
            if (!TextUtils.isEmpty(category)) {
                if (p.getCategoryName() == null || !p.getCategoryName().equalsIgnoreCase(category)) continue;
            }

            filtered.add(p);
        }

        adapter.updateList(filtered);
    }

}
