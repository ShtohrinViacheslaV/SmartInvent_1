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

/**
 * Інтерфейс для взаємодії з REST API за допомогою Retrofit.
 * Містить методи для надсилання запитів до сервера.
 */
public interface ApiService {

    /**
     * Зберігає конфігурацію бази даних на сервері.
     */
    @POST("api/config/save")
    Call<Void> saveDatabaseConfig(@Body Map<String, String> request);

    /**
     * Створює нову компанію.
     */
    @POST("api/companies")
    Call<Company> createCompany(@Body Company company);

    /**
     * Створює нового працівника.
     */
    @POST("api/employees")
    Call<Employee> createEmployee(@Body Employee employee);

    /**
     * Перевіряє з'єднання з базою даних.
     */
    @POST("api/testConnection")
    Call<Void> testDbConnection(@Body DatabaseConfig config);

    /**
     * Перевіряє, чи створені всі необхідні таблиці.
     */
    @POST("api/checkTables")
    Call<Boolean> checkDatabaseTables(@Body DatabaseConfig config);

    /**
     * Ініціалізує базу даних: створює таблиці та виконує початкову конфігурацію.
     */
    @POST("api/setupDatabase")
    Call<Void> initializeDatabase(@Body DatabaseConfig config);

    /**
     * Очищає базу даних (видаляє всі записи).
     */
    @POST("/api/clearDatabase")
    Call<Void> clearDatabase(@Body DatabaseConfig config);

    /**
     * Авторизує користувача за email і паролем.
     */
    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);
}

//    @POST("api/setupDatabase")
//    Call<String> initializeDatabase();

//    @POST("api/initializeDatabase")
//    Call<Void> initializeDatabase(@Body DatabaseConfig config);

//    @POST("config/save")
//    Call<Void> saveDatabaseConfig(@Body DatabaseConfigRequest request);

//    @POST("api/config/tst-connection")
//    Call<Void> testDbConnection(@Body DatabaseConfig config);
