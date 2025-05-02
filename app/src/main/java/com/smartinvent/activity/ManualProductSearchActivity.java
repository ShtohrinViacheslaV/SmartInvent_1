package com.smartinvent.activity;


import android.content.Intent;
import android.os.Bundle;
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

public class ManualProductSearchActivity extends AppCompatActivity {

    private EditText editTextProductName;
    private Button btnSearchByName, btnSearchByProductId, btnSearchByCategory;
    private Button btnSortByCategory, btnSortByStorage, btnSearch;
    private RecyclerView recyclerViewResults;
    private InventorySessionProductAdapter productAdapter;
    private List<InventorySessionProduct> inventorySessionProductList = new ArrayList<>();

    private Long sessionId;
    private String searchCriteria = "name";
    private String sortBy = null;

    private InventoryService inventoryService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_product_search);

        editTextProductName = findViewById(R.id.editTextProductName);
        btnSearchByName = findViewById(R.id.btnSearchByName);
        btnSearchByProductId = findViewById(R.id.btnSearchByProductId);
        btnSearchByCategory = findViewById(R.id.btnSearchByCategory);
        btnSortByCategory = findViewById(R.id.btnSortByCategory);
        btnSortByStorage = findViewById(R.id.btnSortByStorage);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerViewResults = findViewById(R.id.recyclerViewResults);

        sessionId = getIntent().getLongExtra("SESSION_ID", -1);
        if (sessionId == -1) {
            Toast.makeText(this, "Невірна сесія інвентаризації", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ініціалізуй InventoryService
        inventoryService = new InventoryService();

        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new InventorySessionProductAdapter(product -> {
            // handle product click
        }, inventoryService);
        recyclerViewResults.setAdapter(productAdapter);

        btnSearchByName.setOnClickListener(v -> updateSearchCriteria("name"));
        btnSearchByProductId.setOnClickListener(v -> updateSearchCriteria("productId"));
        btnSearchByCategory.setOnClickListener(v -> updateSearchCriteria("category"));

        btnSortByCategory.setOnClickListener(v -> {
            sortBy = "category";
            Toast.makeText(this, "Сортування за категорією (ще не реалізовано)", Toast.LENGTH_SHORT).show();
        });

        btnSortByStorage.setOnClickListener(v -> {
            sortBy = "storage";
            Toast.makeText(this, "Сортування за складом (ще не реалізовано)", Toast.LENGTH_SHORT).show();
        });

        btnSearch.setOnClickListener(v -> performSearch());
    }

    private void updateSearchCriteria(String criteria) {
        searchCriteria = criteria;

        btnSearchByName.setBackgroundColor(getResources().getColor(
                criteria.equals("name") ? R.color.lightGray : R.color.delft_blue));
        btnSearchByProductId.setBackgroundColor(getResources().getColor(
                criteria.equals("productId") ? R.color.lightGray : R.color.delft_blue));
        btnSearchByCategory.setBackgroundColor(getResources().getColor(
                criteria.equals("category") ? R.color.lightGray : R.color.delft_blue));
    }

    private void performSearch() {
        String input = editTextProductName.getText().toString().trim();

        if (input.isEmpty()) {
            Toast.makeText(this, "Введіть текст для пошуку", Toast.LENGTH_SHORT).show();
            return;
        }

        InventoryApi api = ApiClient.getClient().create(InventoryApi.class);
        Call<List<InventorySessionProduct>> call = api.searchInventorySessionProducts(sessionId, input, searchCriteria, sortBy);

        call.enqueue(new Callback<List<InventorySessionProduct>>() {
            @Override
            public void onResponse(Call<List<InventorySessionProduct>> call, Response<List<InventorySessionProduct>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inventorySessionProductList.clear();
                    inventorySessionProductList.addAll(response.body());
                    productAdapter.setProducts(inventorySessionProductList);
                } else {
                    Toast.makeText(ManualProductSearchActivity.this, "Товари не знайдено", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<InventorySessionProduct>> call, Throwable t) {
                Toast.makeText(ManualProductSearchActivity.this, "Помилка з'єднання: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}



//
//public class ManualProductSearchActivity extends AppCompatActivity {
//
//    private EditText editTextProductName;
//    private Button btnSearchByName, btnSearchByProductId, btnSearchByCategory;
//    private Button btnSortByCategory, btnSortByStorage, btnSearch;
//    private RecyclerView recyclerViewResults;
//    private InventorySessionProductAdapter productAdapter;
//    private List<InventorySessionProduct> inventorySessionProductList = new ArrayList<>();
//
//    private Long sessionId; // ID сесії інвентаризації
//    private String searchCriteria = "name"; // За замовчуванням, пошук по назві
//    private String sortBy = "category"; // За замовчуванням, сортування по категорії
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manual_product_search);
//
//        // Ініціалізація елементів UI
//        editTextProductName = findViewById(R.id.editTextProductName);
//        btnSearchByName = findViewById(R.id.btnSearchByName);
//        btnSearchByProductId = findViewById(R.id.btnSearchByProductId);
//        btnSearchByCategory = findViewById(R.id.btnSearchByCategory);
//        btnSortByCategory = findViewById(R.id.btnSortByCategory);
//        btnSortByStorage = findViewById(R.id.btnSortByStorage);
//        btnSearch = findViewById(R.id.btnSearch);
//        recyclerViewResults = findViewById(R.id.recyclerViewResults);
//
//        // Отримання ID сесії з Intent
//        sessionId = getIntent().getLongExtra("SESSION_ID", -1);
//        if (sessionId == -1) {
//            Toast.makeText(this, "Невірна сесія інвентаризації", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // Ініціалізація RecyclerView
//        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
//        productAdapter = new InventorySessionProductAdapter(inventorySessionProductList);
//        recyclerViewResults.setAdapter(productAdapter);
//
//        // Обробники кнопок для вибору критеріїв пошуку
//        btnSearchByName.setOnClickListener(v -> searchCriteria = "name");
//        btnSearchByProductId.setOnClickListener(v -> searchCriteria = "productId");
//        btnSearchByCategory.setOnClickListener(v -> searchCriteria = "category");
//
//        // Обробники кнопок для вибору критерію сортування
//        btnSortByCategory.setOnClickListener(v -> sortBy = "category");
//        btnSortByStorage.setOnClickListener(v -> sortBy = "storage");
//
//        // Обробник натискання кнопки пошуку
//        btnSearch.setOnClickListener(v -> performSearch());
//    }
//
//    private void performSearch() {
//        String productName = editTextProductName.getText().toString();
//
//        // Створення запиту до API для пошуку інвентаризаційних товарів
//        InventoryApi api = ApiClient.getClient().create(InventoryApi.class);
//
//        // Запит до API для пошуку товарів в сесії інвентаризації з відповідними критеріями
//        Call<List<InventorySessionProduct>> call = api.searchInventorySessionProducts(sessionId, productName, searchCriteria, sortBy);
//        call.enqueue(new Callback<List<InventorySessionProduct>>() {
//            @Override
//            public void onResponse(Call<List<InventorySessionProduct>> call, Response<List<InventorySessionProduct>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    inventorySessionProductList.clear();
//                    inventorySessionProductList.addAll(response.body());
//                    productAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(ManualProductSearchActivity.this, "Не вдалося знайти товари", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<InventorySessionProduct>> call, Throwable t) {
//                Toast.makeText(ManualProductSearchActivity.this, "Помилка з'єднання: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
