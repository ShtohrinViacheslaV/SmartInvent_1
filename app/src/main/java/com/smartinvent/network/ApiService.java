package com.smartinvent.network;

import com.smartinvent.config.DatabaseConfig;
import com.smartinvent.model.AuthRequest;
import com.smartinvent.model.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.Map;

public interface ApiService {

    @POST("api/config/save")
    Call<Void> saveDatabaseConfig(@Body Map<String, String> request);

//    @POST("config/save")
//    Call<Void> saveDatabaseConfig(@Body DatabaseConfigRequest request);

    @POST("test-db-connection")
    Call<Void> testDbConnection(@Body DatabaseConfig config);


    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);
}
