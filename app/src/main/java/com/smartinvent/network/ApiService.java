package com.smartinvent.network;

import com.smartinvent.config.DatabaseConfig;
import com.smartinvent.model.AuthRequest;
import com.smartinvent.model.AuthResponse;
import com.smartinvent.model.Company;
import com.smartinvent.model.Employee;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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

//    @POST("api/setupDatabase")
//    Call<String> initializeDatabase();

//    @POST("api/initializeDatabase")
//    Call<Void> initializeDatabase(@Body DatabaseConfig config);

    @POST("clear-database")
    Call<Void> clearDatabase(@Body DatabaseConfig config);

//    @POST("config/save")
//    Call<Void> saveDatabaseConfig(@Body DatabaseConfigRequest request);

//    @POST("api/config/tst-connection")
//    Call<Void> testDbConnection(@Body DatabaseConfig config);


    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);
}
