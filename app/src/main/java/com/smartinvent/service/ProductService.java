package com.smartinvent.service;

import com.smartinvent.model.Product;
import com.smartinvent.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Collections;
import java.util.List;


public class ProductService {

    private final ProductApi productApi;

    public ProductService() {
        productApi = ApiClient.getClient().create(ProductApi.class);
    }

    // Метод для отримання всіх продуктів
    public void getAllProducts(ProductListCallback callback) {
        productApi.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(true, response.body());
                } else {
                    callback.onSuccess(false, null);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                callback.onSuccess(false, null);
            }
        });
    }

    public void searchProducts(String query, ProductListCallback callback) {
        productApi.searchProducts(query).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(true, response.body());
                } else {
                    callback.onSuccess(true, Collections.emptyList()); // Повертаємо пустий список, але не чіпаємо старий
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                callback.onSuccess(false, null); // Не очищаємо список, якщо запит не вдався
            }
        });
    }


    public void createProduct(Product product, ProductCallback callback) {
        productApi.createProduct(product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(true, response.body());
                } else {
                    callback.onSuccess(false, null);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                callback.onSuccess(false, null);
            }
        });
    }



    public void isQrCodeUnique(String qrCode, QrCodeCallback callback) {
        productApi.isQrCodeUnique(qrCode).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                callback.onResult(response.isSuccessful() && Boolean.TRUE.equals(response.body()));
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onResult(false);
            }
        });
    }

    public void updateProduct(Product product, ProductCallback callback) {
        productApi.updateProduct(product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(true, response.body());
                } else {
                    callback.onSuccess(false, null);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                callback.onSuccess(false, null);
            }
        });
    }


    public interface ProductListCallback {
        void onSuccess(boolean success, List<Product> products);
    }

    public interface ProductCallback {
        void onSuccess(boolean success, Product product);
    }

    public interface QrCodeCallback {
        void onResult(boolean isUnique);
    }
}

//public class ProductService {
//
//    private final ProductApi productApi;
//
//    public ProductService() {
//        productApi = ApiClient.getClient().create(ProductApi.class);
//    }
//
//    public void createProduct(Product product, ProductCallback callback) {
//        productApi.createProduct(product).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                callback.onSuccess(response.isSuccessful());
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                callback.onSuccess(false);
//            }
//        });
//    }
//
//    public void isQrCodeUnique(String qrCode, QrCodeCallback callback) {
//        productApi.isQrCodeUnique(qrCode).enqueue(new Callback<Boolean>() {
//            @Override
//            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                callback.onResult(response.isSuccessful() && Boolean.TRUE.equals(response.body()));
//            }
//
//            @Override
//            public void onFailure(Call<Boolean> call, Throwable t) {
//                callback.onResult(false);
//            }
//        });
//    }
//
//    public void updateProduct(Product product, Consumer<Boolean> callback) {
//        productApi.updateProduct(product).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                callback.accept(response.isSuccessful());
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                callback.accept(false);
//            }
//        });
//    }
//
//    public interface ProductCallback {
//        void onSuccess(boolean success);
//    }
//
//    public interface QrCodeCallback {
//        void onResult(boolean isUnique);
//    }
//}
