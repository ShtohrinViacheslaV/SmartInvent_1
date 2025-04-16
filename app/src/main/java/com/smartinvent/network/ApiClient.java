package com.smartinvent.network;

import com.smartinvent.config.ApiConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Клас ApiClient відповідає за ініціалізацію і налаштування Retrofit клієнта для комунікації з сервером.
 * Має методи для отримання клієнта та сервісу для здійснення HTTP-запитів.
 */
public class ApiClient {

    /**
     * retrofit - об'єкт Retrofit, який використовується для виконання HTTP-запитів.
     */
    private static Retrofit retrofit = null;


    /**
     * Метод updateClient оновлює базову URL-адресу для Retrofit клієнта.
     *
     * @param baseUrl нова базова URL-адреса
     */
    public static void updateClient(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Метод getClient повертає об'єкт Retrofit клієнта.
     * Якщо клієнт ще не ініціалізований, він буде створений з базовою URL-адресою.
     *
     * @return об'єкт Retrofit
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            updateClient(ApiConfig.getBaseUrl());
        }
        return retrofit;
    }

    public static ApiService getService() {
        return getClient().create(ApiService.class);
    }
}

