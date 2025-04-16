package com.smartinvent.service;

import com.smartinvent.model.Product;
import com.smartinvent.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.function.Consumer;

/**
 * Сервіс для взаємодії з API продуктів.
 * Використовується для створення, оновлення та перевірки унікальності QR-кодів товарів.
 */
public class ProductService {

    private final ProductApi productApi;

    /**
     * Ініціалізує клієнт для взаємодії з ProductApi.
     */
    public ProductService() {
        productApi = ApiClient.getClient().create(ProductApi.class);
    }

    /**
     * Створює новий товар.
     *
     * @param product товар для створення
     * @param callback колбек для обробки результату
     */
    public void createProduct(Product product, ProductCallback callback) {
        productApi.createProduct(product).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onSuccess(false);
            }
        });
    }

    /**
     * Перевіряє, чи є QR-код унікальним.
     *
     * @param qrCode QR-код для перевірки
     * @param callback колбек для обробки результату
     */
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

    /**
     * Оновлює інформацію про товар.
     *
     * @param product товар для оновлення
     * @param callback функція для обробки результату
     */
    public void updateProduct(Product product, Consumer<Boolean> callback) {
        productApi.updateProduct(product).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.accept(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.accept(false);
            }
        });
    }

    /**
     * Колбек для створення товару.
     */
    public interface ProductCallback {
        void onSuccess(boolean success);
    }

    /**
     * Колбек для перевірки унікальності QR-коду.
     */
    public interface QrCodeCallback {
        void onResult(boolean isUnique);
    }
}
