package com.smartinvent.service;

import com.android.volley.Request;
import com.smartinvent.model.*;
import com.smartinvent.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Map;


public class InventoryService {

    private final InventoryApi inventoryApi;

    public InventoryService() {
        inventoryApi = ApiClient.getClient().create(InventoryApi.class);
    }

    // === SESSION METHODS ===

    public void getAllSessions(InventorySessionListCallback callback) {
        inventoryApi.getAllSessions().enqueue(new Callback<List<InventorySession>>() {
            @Override
            public void onResponse(Call<List<InventorySession>> call, Response<List<InventorySession>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error loading sessions");
                }
            }

            @Override
            public void onFailure(Call<List<InventorySession>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void getActiveSessions(InventorySessionListCallback callback) {
        inventoryApi.getActiveSessions().enqueue(new Callback<List<InventorySession>>() {
            @Override
            public void onResponse(Call<List<InventorySession>> call, Response<List<InventorySession>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error loading active sessions");
                }
            }

            @Override
            public void onFailure(Call<List<InventorySession>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void createSession(InventorySession session, InventorySessionCallback callback) {
        inventoryApi.createSession(session).enqueue(new Callback<InventorySession>() {
            @Override
            public void onResponse(Call<InventorySession> call, Response<InventorySession> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error creating session");
                }
            }

            @Override
            public void onFailure(Call<InventorySession> call, Throwable t) {
                callback.onFailure("Network error");
            }
        });
    }

    public void completeSession(Long sessionId, InventorySessionCallback callback) {
        inventoryApi.completeSession(sessionId).enqueue(new Callback<InventorySession>() {
            @Override
            public void onResponse(Call<InventorySession> call, Response<InventorySession> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error completing session");
                }
            }

            @Override
            public void onFailure(Call<InventorySession> call, Throwable t) {
                callback.onFailure("Network error");
            }
        });
    }

    public void cancelSession(Long sessionId, InventorySessionCallback callback) {
        inventoryApi.cancelSession(sessionId).enqueue(new Callback<InventorySession>() {
            @Override
            public void onResponse(Call<InventorySession> call, Response<InventorySession> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error cancelling session");
                }
            }

            @Override
            public void onFailure(Call<InventorySession> call, Throwable t) {
                callback.onFailure("Network error");
            }
        });
    }


    public void getSessionDetails(Long sessionId, InventorySessionCallback callback) {
        inventoryApi.getSessionDetails(sessionId).enqueue(new Callback<InventorySession>() {
            @Override
            public void onResponse(Call<InventorySession> call, Response<InventorySession> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error loading session details");
                }
            }

            @Override
            public void onFailure(Call<InventorySession> call, Throwable t) {
                callback.onFailure("Network error");
            }
        });
    }


// === RESULT METHODS ===


    public void getStatusCounts(Long sessionId, StatusCountCallback callback) {
        inventoryApi.getStatusCounts(sessionId).enqueue(new Callback<Map<String, Long>>() {
            @Override
            public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error loading status counts");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {
                callback.onFailure("Network error");
            }
        });
    }



    public void getResultsForSession(Long sessionId, InventoryResultListCallback callback) {
        inventoryApi.getResultsForSession(sessionId).enqueue(new Callback<List<InventorySessionProduct>>() {
            @Override
            public void onResponse(Call<List<InventorySessionProduct>> call, Response<List<InventorySessionProduct>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error loading results");
                }
            }

            @Override
            public void onFailure(Call<List<InventorySessionProduct>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void getProductsForSession(Long sessionId, InventoryResultListCallback callback) {
        inventoryApi.getProductsForSession(sessionId).enqueue(new Callback<List<InventorySessionProduct>>() {
            @Override
            public void onResponse(Call<List<InventorySessionProduct>> call, Response<List<InventorySessionProduct>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error loading products");
                }
            }

            @Override
            public void onFailure(Call<List<InventorySessionProduct>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void addResult(InventoryResult result, InventoryActionCallback callback) {
        inventoryApi.addResult(result).enqueue(new Callback<InventoryResult>() {
            @Override
            public void onResponse(Call<InventoryResult> call, Response<InventoryResult> response) {
                callback.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<InventoryResult> call, Throwable t) {
                callback.onFailure("Network error");
            }
        });
    }

    public void saveOrUpdateInventoryProductResultDto(Long sessionId, InventoryProductResultDto dto, InventoryActionCallback callback) {
        inventoryApi.saveOrUpdateInventoryProductResultDto(sessionId, dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }





    public void updateResult(Long resultId, InventoryResult result, InventoryActionCallback callback) {
        inventoryApi.updateResult(resultId, result).enqueue(new Callback<InventoryResult>() {
            @Override
            public void onResponse(Call<InventoryResult> call, Response<InventoryResult> response) {
                callback.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<InventoryResult> call, Throwable t) {
                callback.onFailure("Network error");
            }
        });
    }

    public void markNotFound(Long sessionId, InventoryActionCallback callback) {
        inventoryApi.markNotFound(sessionId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Network error");
            }
        });
    }

    public void addProductDuringInventory(CreateProductDuringInventoryRequest request, InventoryProductCallback callback) {
        inventoryApi.addProductDuringInventory(request).enqueue(new Callback<InventorySessionProduct>() {
            @Override
            public void onResponse(Call<InventorySessionProduct> call, Response<InventorySessionProduct> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to add product");
                }
            }

            @Override
            public void onFailure(Call<InventorySessionProduct> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void getInventoryResultsBySession(Long sessionId, InventoryProductResultCallback callback) {
        inventoryApi.getResultsBySession(sessionId).enqueue(new Callback<List<InventoryProductResultDto>>() {
            @Override
            public void onResponse(Call<List<InventoryProductResultDto>> call, Response<List<InventoryProductResultDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Error loading inventory results");
                }
            }

            @Override
            public void onFailure(Call<List<InventoryProductResultDto>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void getProductByWorkId(Long sessionId, String productWorkId, InventoryProductResultSingleCallback callback) {
        inventoryApi.getProductByWorkId(sessionId, productWorkId)
                .enqueue(new Callback<InventoryProductResultDto>() {
                    @Override
                    public void onResponse(Call<InventoryProductResultDto> call, Response<InventoryProductResultDto> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onFailure("Product not found");
                        }
                    }

                    @Override
                    public void onFailure(Call<InventoryProductResultDto> call, Throwable t) {
                        callback.onFailure("Network error: " + t.getMessage());
                    }
                });
    }

    public void getProductById(Long sessionId, Long productId, InventoryProductResultSingleCallback callback) {
        inventoryApi.getProductById(sessionId, productId).enqueue(new Callback<InventoryProductResultDto>() {
            @Override
            public void onResponse(Call<InventoryProductResultDto> call, Response<InventoryProductResultDto> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Product not found");
                }
            }

            @Override
            public void onFailure(Call<InventoryProductResultDto> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }


    public void getProductsByStatus(long sessionId, String status, ProductsCallback callback) {
        InventoryApi api = ApiClient.getClient().create(InventoryApi.class);

        Call<List<InventoryProductResultDto>> call = api.getInventoryProductsByStatus(sessionId, status);
        call.enqueue(new Callback<List<InventoryProductResultDto>>() {
            @Override
            public void onResponse(Call<List<InventoryProductResultDto>> call, Response<List<InventoryProductResultDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Не вдалося отримати товари");
                }
            }

            @Override
            public void onFailure(Call<List<InventoryProductResultDto>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }


    // === BACKUP ===

    public void completeAndBackupSession(Long sessionId, StringCallback callback) {
        inventoryApi.completeAndBackupSession(sessionId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Помилка при завершенні сесії з бекапом");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure("Помилка мережі: " + t.getMessage());
            }
        });
    }













    // === STATUS CHECKING UTILS ===

    public boolean isLockedByAnotherUser(InventorySessionProduct product) {
        return product.isLocked();
    }

    public boolean isAdded(InventorySessionProduct product) {
        return "ADDED".equalsIgnoreCase(product.getStatus());
    }

    public boolean isChanged(InventorySessionProduct product) {
        return "MODIFIED".equalsIgnoreCase(product.getStatus());
    }

    public boolean isConfirmed(InventorySessionProduct product) {
        return "CONFIRMED".equalsIgnoreCase(product.getStatus());
    }

    // === CALLBACK INTERFACES ===


    public interface StringCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }


    public interface ProductsCallback {
        void onSuccess(List<InventoryProductResultDto> products);
        void onFailure(String errorMessage);
    }


    public interface InventorySessionCallback {
        void onSuccess(InventorySession session);
        void onFailure(String errorMessage);
    }

    public interface InventorySessionListCallback {
        void onSuccess(List<InventorySession> sessions);
        void onFailure(String errorMessage);
    }

    public interface InventoryResultListCallback {
        void onSuccess(List<InventorySessionProduct> results);
        void onFailure(String errorMessage);
    }

    public interface InventoryProductCallback {
        void onSuccess(InventorySessionProduct product);
        void onFailure(String errorMessage);
    }

    public interface InventoryActionCallback {
        void onSuccess(boolean success);
        void onFailure(String errorMessage);
    }

    public interface InventoryProductResultCallback {
        void onSuccess(List<InventoryProductResultDto> results);
        void onFailure(String errorMessage);
    }

    public interface InventoryProductResultSingleCallback {
        void onSuccess(InventoryProductResultDto productResultDto);
        void onFailure(String errorMessage);
    }

    public interface StatusCountCallback {
        void onSuccess(Map<String, Long> statusCounts);
        void onFailure(String errorMessage);
    }



}
