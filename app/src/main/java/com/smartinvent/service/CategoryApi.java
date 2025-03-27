package com.smartinvent.service;

import com.smartinvent.model.Category;
import java.util.List;

import com.smartinvent.model.Product;
import retrofit2.Call;
import retrofit2.http.*;

public interface CategoryApi {
    @GET("api/categories/all")
    Call<List<Category>> getAllCategories();

    @POST("api/categories/create")
    Call<Category> createCategory(@Body Category category);

    @PUT("api/categories/update/{id}")
    Call<Void> updateCategory(@Body Category category);

    @DELETE("api/categories/delete/{id}")
    Call<Void> deleteCategory(@Path("id") Long id);
}
