package com.smartinvent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProductApi productApi;
    private EditText searchInput;
    private Button btnScanQr;

    public ProductFragment() {
        // Конструктор без параметрів
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchInput = view.findViewById(R.id.search_input);
        btnScanQr = view.findViewById(R.id.btn_scan_qr);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new ProductAdapter(null, product ->
                Toast.makeText(getContext(), "Вибрано: " + product.getName(), Toast.LENGTH_SHORT).show()
        );
        recyclerView.setAdapter(productAdapter);

        productApi = ApiClient.getClient().create(ProductApi.class);
        loadProducts();

        btnScanQr.setOnClickListener(v -> startQrScanner());

        return view;
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
                Toast.makeText(getContext(), "Помилка завантаження", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startQrScanner() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Наведіть камеру на QR-код товару");
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedCode = result.getContents();
                searchInput.setText(scannedCode);
                searchProduct(scannedCode);
            } else {
                Toast.makeText(getContext(), "Сканування скасовано", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Товар не знайдено", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Помилка пошуку товару", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
