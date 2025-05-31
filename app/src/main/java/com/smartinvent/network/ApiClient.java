package com.smartinvent.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartinvent.config.ApiConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.LocalDateTime;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static void updateClient(String baseUrl) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeType())
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

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