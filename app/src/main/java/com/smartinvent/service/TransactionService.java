package com.smartinvent.service;

import com.smartinvent.model.Transaction;
import com.smartinvent.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class TransactionService {

    private final TransactionApi transactionApi;

    public TransactionService() {
        transactionApi = ApiClient.getClient().create(TransactionApi.class);
    }

    // Створення транзакції - залишаємо як є
    public void createTransaction(Transaction transaction, TransactionCallback callback) {
        transactionApi.createTransaction(transaction).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onSuccess(false);
            }
        });
    }

    // Новий метод для отримання всіх транзакцій
    public void getAllTransactions(TransactionsCallback callback) {
        transactionApi.getAllTransactions().enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Помилка завантаження транзакцій");
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getTransactionsByEmployeeId(Long employeeId, TransactionsCallback callback) {
        transactionApi.getTransactionsByEmployeeId(employeeId).enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Помилка завантаження транзакцій");
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface TransactionCallback {
        void onSuccess(boolean success);
    }

    public interface TransactionsCallback {
        void onSuccess(List<Transaction> transactions);
        void onError(String error);
    }
}

