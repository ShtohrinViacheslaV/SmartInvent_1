package com.smartinvent.service;


import com.smartinvent.model.Storage;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface StorageApi {
    @GET("storages")
    Call<List<Storage>> getStorages();

}
