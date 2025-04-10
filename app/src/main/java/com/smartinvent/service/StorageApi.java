package com.smartinvent.service;

import com.smartinvent.model.Storage;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Body;


public interface StorageApi {
    @GET("api/storages/all")
    Call<List<Storage>> getAllStorages();

    @POST("api/storages/create")
    Call<Storage> createStorage(@Body Storage storage);

    @PUT("api/storages/update/{id}")
    Call<Void> updateStorage(@Body Storage storage);

    @DELETE("api/storages/delete/{id}")
    Call<Void> deleteStorage(@Path("id") Long id);

    @GET("api/storages/{id}")
    Call<Storage> getStorageById(@Path("id") Long storageId);
}
