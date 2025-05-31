package com.smartinvent.service;

import com.smartinvent.model.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;
public interface EmployeeApi {

    @GET("api/employees/all")
    Call<List<Employee>> getAllEmployees(@Query("companyId") Long companyId);

    @GET("api/employees/{employeeId}")
    Call<Employee> getEmployeeById(@Path("employeeId") Long employeeId);

    @POST("api/employees/create")
    Call<Employee> createEmployee(@Body Employee employee);

    @PUT("api/employees/update/{employeeId}")
    Call<Employee> updateEmployee(@Path("employeeId") Long employeeId, @Body Employee employee);

    @DELETE("api/employees/delete/{employeeId}")
    Call<Void> deleteEmployee(@Path("employeeId") Long employeeId);

    @GET("api/employees/byWorkId/{employeeWorkId}")
    Call<Employee> getEmployeeByEmployeeWorkId(@Path("employeeWorkId") String employeeWorkId);

    @GET("api/employees/search/{lastName}")
    Call<List<Employee>> searchEmployeesByLastName(@Path("lastName") String lastName);

    @GET("api/employees/search")
    Call<List<Employee>> searchEmployees(@Query("query") String query);



    @GET("/api/employees/check-unique")
    Call<Boolean> isEmployeeUnique(
            @Query("companyId") Long companyId,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("employeeWorkId") String employeeWorkId
    );

    @GET("/api/employees/check-unique-exclude")
    Call<Boolean> isEmployeeUniqueExclude(
            @Query("companyId") Long companyId,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("employeeWorkId") String employeeWorkId,
            @Query("employeeIdToExclude") Long employeeIdToExclude
    );

    @PUT("api/employees/update-password/{employeeId}")
    Call<String> updatePassword(@Path("employeeId") Long employeeId, @Body UpdatePasswordRequest request);

    @PUT("api/employees/update-contact/{employeeId}")
    Call<String> updateContactInfo(@Path("employeeId") Long employeeId, @Body UpdateContactRequest request);

}
