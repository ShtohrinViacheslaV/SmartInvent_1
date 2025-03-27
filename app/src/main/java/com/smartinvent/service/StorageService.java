package com.smartinvent.service;

import com.smartinvent.model.Storage;
import com.smartinvent.network.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StorageService {

    private final StorageApi storageApi;

    public StorageService() {
        storageApi = ApiClient.getClient().create(StorageApi.class);
    }

    public void getAllStorages(StorageCallback callback) {
        storageApi.getAllStorages().enqueue(new Callback<List<Storage>>() {
            @Override
            public void onResponse(Call<List<Storage>> call, Response<List<Storage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Не вдалося завантажити склади");
                }
            }

            @Override
            public void onFailure(Call<List<Storage>> call, Throwable t) {
                callback.onFailure("Помилка: " + t.getMessage());
            }
        });
    }

    public interface StorageCallback {
        void onSuccess(List<Storage> storages);
        void onFailure(String errorMessage);
    }
}
