package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.smartinvent.R;
import com.smartinvent.adapter.InventoryProductReviewAdapter;
import com.smartinvent.model.*;
import com.smartinvent.service.CategoryService;
import com.smartinvent.service.InventoryService;
import com.smartinvent.service.StorageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewInventoryResultsActivity extends AppCompatActivity {

    private MaterialButton btnNext;
    private MaterialAutoCompleteTextView spinnerStatus, spinnerStorage, spinnerCategory;
    private RecyclerView recyclerViewProducts;
    private SwipeRefreshLayout swipeRefreshLayout;

    private InventoryProductReviewAdapter adapter;
    private List<InventoryProductResultDto> allProducts = new ArrayList<>();

    private InventoryService inventoryService;
    private CategoryService categoryService;
    private StorageService storageService;

    private List<Category> categoryList = new ArrayList<>();
    private List<Storage> storageList = new ArrayList<>();

    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> storageAdapter;
    private ArrayAdapter<String> statusAdapter;
    private final Map<String, InventoryProductStatusEnum> statusMap = new HashMap<>();

    private Long inventorySessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_inventory_results);

        inventorySessionId = getIntent().getLongExtra(Constants.KEY_INVENTORY_SESSION_ID, -1);

        inventoryService = new InventoryService();
        categoryService = new CategoryService();
        storageService = new StorageService();

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
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerStorage = findViewById(R.id.spinnerStorage);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        btnNext = findViewById(R.id.btnSave);

        adapter = new InventoryProductReviewAdapter(new ArrayList<>(), this);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducts.setAdapter(adapter);
    }

    private void setupListeners() {
        swipeRefreshLayout.setOnRefreshListener(this::loadProducts);

        spinnerStatus.setOnItemClickListener((parent, view, position, id) -> filterProducts());
        spinnerStorage.setOnItemClickListener((parent, view, position, id) -> filterProducts());
        spinnerCategory.setOnItemClickListener((parent, view, position, id) -> filterProducts());

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, FinalInventorySummaryActivity.class);
            intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, inventorySessionId);
            startActivity(intent);
        });
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

                categoryAdapter = new ArrayAdapter<>(ReviewInventoryResultsActivity.this, R.layout.item_dropdown, categoryNames);
                categoryAdapter.setDropDownViewResource(R.layout.item_dropdown);
                spinnerCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ReviewInventoryResultsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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

                storageAdapter = new ArrayAdapter<>(ReviewInventoryResultsActivity.this, R.layout.item_dropdown, storageNames);
                storageAdapter.setDropDownViewResource(R.layout.item_dropdown);
                spinnerStorage.setAdapter(storageAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ReviewInventoryResultsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStatuses() {
        List<String> statusDescriptions = new ArrayList<>();
        for (InventoryProductStatusEnum status : InventoryProductStatusEnum.values()) {
            statusDescriptions.add(status.getDescription());
            statusMap.put(status.getDescription(), status);  // зв'язок опису з enum
        }

        statusAdapter = new ArrayAdapter<>(this, R.layout.item_dropdown, statusDescriptions);
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
        String statusText = spinnerStatus.getText().toString().trim();
        String storage = spinnerStorage.getText().toString().trim();
        String category = spinnerCategory.getText().toString().trim();

        InventoryProductStatusEnum selectedStatus = statusMap.get(statusText);


        List<InventoryProductResultDto> filtered = new ArrayList<>();
        for (InventoryProductResultDto p : allProducts) {
            if (selectedStatus != null && p.getStatus() != selectedStatus) continue;

            if (!TextUtils.isEmpty(storage)) {
                if (p.getStorageName() == null || !p.getStorageName().equalsIgnoreCase(storage)) continue;
            }

            if (!TextUtils.isEmpty(category)) {
                if (p.getCategoryName() == null || !p.getCategoryName().equalsIgnoreCase(category)) continue;
            }

            filtered.add(p);
        }

        adapter.updateList(filtered);
    }
}
