//package com.smartinvent.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.Fragment;
//import com.smartinvent.activity.AddProductActivity;
//import com.smartinvent.activity.ProductDetailsActivity;
//import com.smartinvent.activity.ScannerActivity;
//import com.smartinvent.databinding.FragmentMainScannerBinding;
//import com.smartinvent.model.Product;
//import com.smartinvent.network.ApiClient;
//import com.smartinvent.service.ProductApi;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class MainScannerFragment extends Fragment {
//
//    private FragmentMainScannerBinding binding;
//    private ProductApi productApi;
//
//    private final ActivityResultLauncher<Intent> qrScannerLauncher =
//            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//                if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
//                    String scannedCode = result.getData().getStringExtra("scannedCode");
//                    checkProductOnServer(Long.parseLong(scannedCode));
//                } else {
//                    Toast.makeText(requireContext(), "Сканування скасовано", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentMainScannerBinding.inflate(inflater, container, false);
//        productApi = ApiClient.getClient().create(ProductApi.class);
//
//        binding.btnScan.setOnClickListener(v -> startQrScanner());
//
//        return binding.getRoot();
//    }
//
//    private void startQrScanner() {
//        Intent intent = new Intent(requireContext(), ScannerActivity.class);
//        qrScannerLauncher.launch(intent);
//    }
//
//    private void checkProductOnServer(Long productId) {
//        productApi.getProductById(productId).enqueue(new Callback<Product>() {
//            @Override
//            public void onResponse(Call<Product> call, Response<Product> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    openProductDetails(response.body());
//                } else {
//                    showAddProductDialog(productId);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Product> call, Throwable t) {
//                Toast.makeText(requireContext(), "Помилка підключення до сервера", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void openProductDetails(Product product) {
//        Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
//        intent.putExtra("product", product);
//        startActivity(intent);
//    }
//
//    private void showAddProductDialog(Long productId) {
//        new AlertDialog.Builder(requireContext())
//                .setTitle("Товар не знайдено")
//                .setMessage("Додати новий товар?")
//                .setPositiveButton("Так", (dialog, which) -> {
//                    Intent intent = new Intent(requireContext(), AddProductActivity.class);
//                    intent.putExtra("product_id", productId.toString());
//                    startActivity(intent);
//                })
//                .setNegativeButton("Ні", null)
//                .show();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//}



package com.smartinvent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.smartinvent.R;
import com.smartinvent.activity.AddProductActivity;
import com.smartinvent.activity.ProductDetailsActivity;
import com.smartinvent.activity.ScannerActivity;
import com.smartinvent.model.Product;
import com.smartinvent.network.ApiClient;
import com.smartinvent.service.ProductApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScannerFragment extends Fragment {

    private ProductApi productApi;
    private final ActivityResultLauncher<Intent> qrScannerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    final String scannedCode = result.getData().getStringExtra("scannedCode");
                    checkProductOnServer(scannedCode);
                } else {
                    Toast.makeText(getContext(), "Сканування скасовано", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_scanner, container, false);
        productApi = ApiClient.getClient().create(ProductApi.class);
        startQrScanner();
        return view;
    }

    private void startQrScanner() {
        final Intent intent = new Intent(getActivity(), ScannerActivity.class);
        qrScannerLauncher.launch(intent);
    }

    private void checkProductOnServer(String productWorkId) {
        try {
            productApi.getProductById(productWorkId).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        openProductDetails(response.body());
                    } else {
                        showAddProductDialog(String.valueOf(productWorkId));
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(getContext(), "Помилка підключення до сервера", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Помилка: невірний формат ID товару", Toast.LENGTH_SHORT).show();
        }
    }

    private void openProductDetails(Product product) {
        final Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    private void showAddProductDialog(String productWorkId) {
        new AlertDialog.Builder(getContext())
                .setTitle("Товар не знайдено")
                .setMessage("Додати новий товар?")
                .setPositiveButton("Так", (dialog, which) -> {
                    final Intent intent = new Intent(getActivity(), AddProductActivity.class);
                    intent.putExtra("productWorkId", productWorkId);
                    startActivity(intent);
                })
                .setNegativeButton("Ні", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
