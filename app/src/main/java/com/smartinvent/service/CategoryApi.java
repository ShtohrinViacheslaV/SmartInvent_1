package com.smartinvent.service;

import com.smartinvent.model.Category;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApi {
    @GET("/categories")
    Call<List<Category>> getCategories();
}
