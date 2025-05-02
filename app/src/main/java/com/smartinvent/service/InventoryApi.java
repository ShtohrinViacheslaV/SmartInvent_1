package com.smartinvent.service;

import com.smartinvent.model.CreateProductDuringInventoryRequest;
import com.smartinvent.model.InventoryResult;
import com.smartinvent.model.InventorySession;
import com.smartinvent.model.InventorySessionProduct;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
public interface InventoryApi {

    // --- SESSION ENDPOINTS ---

    @POST("api/inventory/session/create")
    Call<InventorySession> createSession(@Body InventorySession inventorySession);

    @GET("api/inventory/session/all")
    Call<List<InventorySession>> getAllSessions();

    @GET("api/inventory/session/{sessionId}")
    Call<InventorySession> getSessionDetails(@Path("sessionId") Long sessionId);

    @GET("api/inventory/session/active")
    Call<List<InventorySession>> getActiveSessions();

    @PUT("api/inventory/session/complete/{sessionId}")
    Call<InventorySession> completeSession(@Path("sessionId") Long sessionId);


    // --- RESULT ENDPOINTS ---

    @GET("api/inventory/result/session/{sessionId}")
    Call<List<InventorySessionProduct>> getResultsForSession(@Path("sessionId") Long sessionId);

    @POST("api/inventory/result/add")
    Call<InventoryResult> addResult(@Body InventoryResult inventoryResult);

    @PUT("api/inventory/result/update/{resultId}")
    Call<InventoryResult> updateResult(@Path("resultId") Long resultId, @Body InventoryResult inventoryResult);

    @GET("api/inventory/result/products/{sessionId}")
    Call<List<InventorySessionProduct>> getProductsForSession(@Path("sessionId") Long sessionId);

    @POST("api/inventory/result/mark-not-found/{sessionId}")
    Call<Void> markNotFound(@Path("sessionId") Long sessionId);

    @POST("api/inventory/result/add-product")
    Call<InventorySessionProduct> addProductDuringInventory(@Body CreateProductDuringInventoryRequest request);

    @GET("api/inventory/result/search")
    Call<List<InventorySessionProduct>> searchInventorySessionProducts(
            @Query("sessionId") Long sessionId,
            @Query("query") String query,
            @Query("criteria") String criteria,
            @Query("sortBy") String sortBy
    );


}
