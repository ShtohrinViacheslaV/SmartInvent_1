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

    public void createStorage(Storage storage, SingleStorageCallback callback) {
        storageApi.createStorage(storage).enqueue(new Callback<Storage>() {
            @Override
            public void onResponse(Call<Storage> call, Response<Storage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Не вдалося створити склад");
                }
            }

            @Override
            public void onFailure(Call<Storage> call, Throwable t) {
                callback.onFailure("Помилка: " + t.getMessage());
            }
        });
    }

    public void updateStorage(Storage storage, SingleStorageCallback callback) {
        storageApi.updateStorage(storage.getStorageId(), storage).enqueue(new Callback<Storage>() {
            @Override
            public void onResponse(Call<Storage> call, Response<Storage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(null);
                } else {
                    callback.onFailure("Не вдалося оновити склад");
                }
            }

            @Override
            public void onFailure(Call<Storage> call, Throwable t) {
                callback.onFailure("Помилка: " + t.getMessage());
            }
        });
    }

    public void deleteStorage(Long id, SimpleCallback callback) {
        storageApi.deleteStorage(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Не вдалося видалити склад");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Помилка: " + t.getMessage());
            }
        });
    }

    public interface StorageCallback {
        void onSuccess(List<Storage> storages);
        void onFailure(String errorMessage);
    }

    public interface SingleStorageCallback {
        void onSuccess(Storage storage);  // Повертає Storage при create або null при update
        void onFailure(String errorMessage);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
