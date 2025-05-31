package com.smartinvent.network;

import com.smartinvent.config.DatabaseConfig;
import com.smartinvent.model.*;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;

public interface ApiService {

    @POST("api/config/save")
    Call<Void> saveDatabaseConfig(@Body Map<String, String> request);

    @POST("api/companies")
    Call<Company> createCompany(@Body Company company);

    @POST("api/employees")
    Call<Employee> createEmployee(@Body Employee employee);

    @POST("api/testConnection")
    Call<Void> testDbConnection(@Body DatabaseConfig config);


    @POST("api/checkTables")
    Call<Boolean> checkDatabaseTables(@Body DatabaseConfig config);


    @POST("api/setupDatabase")
    Call<Void> initializeDatabase(@Body DatabaseConfig config);


    @POST("api/clearDatabase")
    Call<Void> clearDatabase(@Body DatabaseConfig config);


    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("api/auth/forgot-password")
    Call<ResponseBody> forgotPassword(@Body ForgotPasswordRequest request);

}
