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

    // Отримання всіх категорій
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

    // Створення категорії
    public void createCategory(Category category, SingleCategoryCallback callback) {
        categoryApi.createCategory(category).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Не вдалося створити категорію");
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                callback.onFailure("Помилка: " + t.getMessage());
            }
        });
    }

    // Оновлення категорії
    public void updateCategory(Category category, SingleCategoryCallback callback) {
        categoryApi.updateCategory(category.getCategoryId(), category).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onFailure("Не вдалося оновити категорію");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Помилка: " + t.getMessage());
            }
        });
    }

    // Видалення категорії
    public void deleteCategory(Long id, SimpleCallback callback) {
        categoryApi.deleteCategory(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailure("Не вдалося видалити категорію");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Помилка: " + t.getMessage());
            }
        });
    }

    // Callback-и
    public interface CategoryCallback {
        void onSuccess(List<Category> categories);
        void onFailure(String errorMessage);
    }

    public interface SingleCategoryCallback {
        void onSuccess(Category category);  // Повертає Category при create або null при update
        void onFailure(String errorMessage);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}