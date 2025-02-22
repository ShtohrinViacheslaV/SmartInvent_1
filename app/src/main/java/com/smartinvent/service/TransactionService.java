package com.smartinvent.service;

import com.smartinvent.model.Transaction;
import com.smartinvent.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionService {

    private final TransactionApi transactionApi;

    public TransactionService() {
        transactionApi = ApiClient.getClient().create(TransactionApi.class);
    }

    public void createTransaction(Transaction transaction, TransactionCallback callback) {
        transactionApi.createTransaction(transaction).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(true);
                } else {
                    callback.onSuccess(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onSuccess(false);
            }
        });
    }

    public interface TransactionCallback {
        void onSuccess(boolean success);
    }
}
