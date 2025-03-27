package com.smartinvent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.smartinvent.activity.*;
import com.smartinvent.adapter.ProductAdapter;
import com.smartinvent.model.Product;
import com.smartinvent.network.ApiClient;
import com.smartinvent.service.ProductApi;

import java.util.ArrayList;
import java.util.List;

import com.smartinvent.service.ProductService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProductService productService;
    private EditText searchInput;
    private Button btnScanQr, btnSearchProduct, btnAddProduct, btnEditProduct, btnRefresh, btnDetailsProduct;
    private List<Product> productList = new ArrayList<>();
    private static final int REQUEST_ADD_PRODUCT = 1;
    private static final int REQUEST_EDIT_PRODUCT = 2;

    public ProductFragment() {
        // Порожній конструктор
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
        btnSearchProduct = view.findViewById(R.id.btn_search_product);

        btnAddProduct = view.findViewById(R.id.btn_add_product);
        btnEditProduct = view.findViewById(R.id.btn_edit_product);
        btnRefresh = view.findViewById(R.id.btn_refresh);
        btnDetailsProduct = view.findViewById(R.id.btn_details_product);

        btnEditProduct.setEnabled(false);
        btnDetailsProduct.setEnabled(false);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productService = new ProductService();

        productAdapter = new ProductAdapter(productList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                btnEditProduct.setEnabled(true);
                btnDetailsProduct.setEnabled(true);
            }

            @Override
            public void onProductDeselected() {
                btnEditProduct.setEnabled(false);
                btnDetailsProduct.setEnabled(false);

            }
        });
        recyclerView.setAdapter(productAdapter);


        loadProducts();

        btnScanQr.setOnClickListener(v -> scanQrCode());
        btnSearchProduct.setOnClickListener(v -> performSearch());


        btnAddProduct.setOnClickListener(v -> openAddProductActivity());
        btnEditProduct.setOnClickListener(v -> openEditProductActivity());
        btnRefresh.setOnClickListener(v -> loadProducts()); // Додаємо оновлення списку
        btnDetailsProduct.setOnClickListener(v -> openProductDetails());

        return view;
    }

    private void performSearch() {
        String query = searchInput.getText().toString().trim();
        if (!query.isEmpty()) {
            searchProducts(query);
        } else {
            Toast.makeText(getContext(), "Введіть запит для пошуку", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProducts() {
        productService.getAllProducts(new ProductService.ProductListCallback() {
            @Override
            public void onSuccess(boolean success, List<Product> products) {
                productList.clear();
                if (success && products != null) {
                    productList.addAll(products);
                } else {
                    Log.w("ProductFragment", "⚠️ Товари не знайдено або сталася помилка");
                }
                productAdapter.setProducts(productList);
            }
        });
    }

    private void scanQrCode() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Скануйте QR-код товару");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_ADD_PRODUCT || requestCode == REQUEST_EDIT_PRODUCT) {
                loadProducts();
            } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
                IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
                if (result != null && result.getContents() != null) {
                    searchInput.setText(result.getContents());
                    searchProducts(result.getContents());
                }
            }
        }
    }

    private void searchProducts(String query) {
        productService.searchProducts(query, new ProductService.ProductListCallback() {
            @Override
            public void onSuccess(boolean success, List<Product> products) {
                if (success && products != null) {
                    productList.clear();
                    productList.addAll(products);
                    productAdapter.setProducts(productList);
                } else {
                    Toast.makeText(getContext(), "Товари не знайдено", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openAddProductActivity() {
        Intent intent = new Intent(getActivity(), AddProductActivity.class);
        startActivityForResult(intent, REQUEST_ADD_PRODUCT);
    }

    private void openEditProductActivity() {
        Product selectedProduct = productAdapter.getSelectedProduct();
        if (selectedProduct == null) {
            Toast.makeText(getContext(), "Оберіть товар для редагування", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity(), EditProductActivity.class);
        intent.putExtra("product", selectedProduct);
        startActivityForResult(intent, REQUEST_EDIT_PRODUCT);
    }

    private void openProductDetails() {
        Product selectedProduct = productAdapter.getSelectedProduct();
        if (selectedProduct == null) {
            Toast.makeText(getContext(), "Оберіть товар для перегляду", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        intent.putExtra("product", selectedProduct);
        startActivity(intent);
    }

}

//public class ProductFragment extends Fragment {
//
//    private RecyclerView recyclerView;
//    private ProductAdapter productAdapter;
//    private ProductApi productApi;
//    private EditText searchInput;
//    private Button btnScanQr, btnAddProduct, btnEditProduct;
//
//    public ProductFragment() {
//        // Конструктор без параметрів
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_product, container, false);
//
//        recyclerView = view.findViewById(R.id.recyclerView);
//        searchInput = view.findViewById(R.id.search_input);
//        btnScanQr = view.findViewById(R.id.btn_scan_qr);
//        btnAddProduct = view.findViewById(R.id.btn_add_product);
//        btnEditProduct = view.findViewById(R.id.btn_edit_product);
//
//
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        productAdapter = new ProductAdapter(null, product ->
//                Toast.makeText(getContext(), "Вибрано: " + product.getName(), Toast.LENGTH_SHORT).show()
//        );
//        recyclerView.setAdapter(productAdapter);
//
//        productApi = ApiClient.getClient().create(ProductApi.class);
//        loadProducts();
//
//        btnScanQr.setOnClickListener(v -> startQrScanner());
//
//
//        return view;
//    }
//
//    private void loadProducts() {
//        productApi.getAllProducts().enqueue(new Callback<List<Product>>() {
//            @Override
//            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    productAdapter.setProducts(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Product>> call, Throwable t) {
//                Toast.makeText(getContext(), "Помилка завантаження", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void startQrScanner() {
//        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
//        integrator.setOrientationLocked(false);
//        integrator.setPrompt("Наведіть камеру на QR-код товару");
//        integrator.setBeepEnabled(true);
//        integrator.initiateScan();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            if (result.getContents() != null) {
//                String scannedCode = result.getContents();
//                searchInput.setText(scannedCode);
//                searchProduct(scannedCode);
//            } else {
//                Toast.makeText(getContext(), "Сканування скасовано", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//    private void searchProduct(String productId) {
//        productApi.searchProducts(productId).enqueue(new Callback<List<Product>>() {
//            @Override
//            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
//                    productAdapter.setProducts(response.body());
//                } else {
//                    Toast.makeText(getContext(), "Товар не знайдено", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Product>> call, Throwable t) {
//                Toast.makeText(getContext(), "Помилка пошуку товару", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//}
