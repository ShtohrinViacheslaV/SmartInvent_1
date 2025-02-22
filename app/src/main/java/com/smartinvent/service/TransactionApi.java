package com.smartinvent.service;

import com.smartinvent.model.Transaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TransactionApi {
    @POST("transactions")
    Call<Void> createTransaction(@Body Transaction transaction);
}
