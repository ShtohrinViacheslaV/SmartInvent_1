package com.smartinvent.service;

import com.smartinvent.model.CreateProductDuringInventoryRequest;
import com.smartinvent.model.InventorySessionProduct;
import com.smartinvent.network.ApiClient;
import com.smartinvent.model.InventorySession;
import com.smartinvent.model.InventoryResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;


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
                callback.onFailure("Network error");
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
                callback.onFailure("Network error");
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
                callback.onFailure("Network error");
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
                callback.onFailure("Network error");
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
                callback.onFailure("Network error");
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
}
