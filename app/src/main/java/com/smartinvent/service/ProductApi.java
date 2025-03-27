package com.smartinvent.service;

import com.smartinvent.model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ProductApi {
    @GET("api/products/all")
    Call<List<Product>> getAllProducts();

    @POST("api/products/create")
    Call<Product> createProduct(@Body Product product);

    @PUT("api/products/update/{id}")
    Call<Product> updateProduct(@Body Product product);

    @DELETE("api/products/delete/{id}")
    Call<Void> deleteProduct(@Path("id") Long id);

    @GET("api/products/search")
    Call<List<Product>> searchProducts(@Query("query") String query);

    @GET("api/products/{id}")
    Call<Product> getProductById(@Path("id") String productWorkId);

    @GET("products/checkQrCode")
    Call<Boolean> isQrCodeUnique(@Query("qrCode") String qrCode);

}

