package com.smartinvent.service;

import com.smartinvent.model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {
    @GET("api/products/all")
    Call<List<Product>> getAllProducts();

    @GET("api/products/search")
    Call<List<Product>> searchProducts(@Query("query") String query);

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") Long productId);

    @POST("products")
    Call<Void> createProduct(@Body Product product);

    @POST("products/update/{id}")
    Call<Void> updateProduct(@Body Product product);

    @GET("products/checkQrCode")
    Call<Boolean> isQrCodeUnique(@Query("qrCode") String qrCode);

    @GET("/api/categories")
    Call<List<String>> getCategories();

}
