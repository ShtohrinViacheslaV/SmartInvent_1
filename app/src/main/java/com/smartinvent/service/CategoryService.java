package com.smartinvent.service;

import com.smartinvent.model.Category;
import com.smartinvent.network.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryService {

    private final CategoryApi categoryApi;

    public CategoryService() {
        categoryApi = ApiClient.getClient().create(CategoryApi.class);
    }

    public void getAllCategories(CategoryCallback callback) {
        categoryApi.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Не вдалося завантажити категорії");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                callback.onFailure("Помилка: " + t.getMessage());
            }
        });
    }

    public interface CategoryCallback {
        void onSuccess(List<Category> categories);
        void onFailure(String errorMessage);
    }
}
