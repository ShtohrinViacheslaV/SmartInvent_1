package com.smartinvent.service;

import com.smartinvent.model.Transaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface TransactionApi {
    @GET("api/transactions/all")
    Call<List<Transaction>> getAllTransactions();

    @POST("api/transactions/create")
    Call<Void> createTransaction(@Body Transaction transaction);

    @GET("api/transactions/{employeeId}")
    Call<List<Transaction>> getTransactionsByEmployeeId(@Path("employeeId") Long employeeId);
}
