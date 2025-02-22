package com.smartinvent.network;

import com.smartinvent.model.InventoryScan;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import java.util.List;

public interface InventoryApiService {

    @POST("/api/inventory/scan")
    Call<InventoryScan> sendScan(@Body InventoryScan scan);

    @GET("/api/inventory/scan/{productId}")
    Call<List<InventoryScan>> getScansByProduct(@Path("productId") long productId);
}
