package com.smartinvent.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.smartinvent.network.DatabaseConfigRequest;

import java.util.HashMap;
import java.util.Map;

public class DatabaseConfigViewModel extends AndroidViewModel {
    private final DatabaseConfigRequest configRequest;



    public DatabaseConfigViewModel(Application application) {
        super(application);
        configRequest = new DatabaseConfigRequest(application);
    }

    public void saveDatabaseConfig(String url, String username, String password, OnSaveCallback callback) {
        configRequest.sendConfigToServer(url, username, password, callback::onSave);
    }

//    public void saveDatabaseConfig(String dbType, String host, String port, String database, String user, String password, OnSaveCallback callback) {
//        configRequest.sendConfigToServer(dbType, host, port, database, user, password, callback::onSave);
//    }

    public void saveDatabaseUrl(String url, OnSaveCallback callback) {
        configRequest.sendUrlToServer(url, callback::onSave);
    }

    public interface OnSaveCallback {
        void onSave(boolean success);
    }
}


//package com.smartinvent.viewmodel;
//
//import android.app.Application;
//import androidx.lifecycle.AndroidViewModel;
//import com.smartinvent.network.DatabaseConfigRequest;
//
//public class DatabaseConfigViewModel extends AndroidViewModel {
//    private final DatabaseConfigRequest configRequest;
//
//    public DatabaseConfigViewModel(Application application) {
//        super(application);
//        configRequest = new DatabaseConfigRequest(application);
//    }
//
//    public void saveDatabaseConfig(String dbType, String host, String port, String database, String user, String password, OnSaveCallback callback) {
//        boolean success = configRequest.saveConfigToFile(dbType, host, port, database, user, password);
//        callback.onSave(success);
//    }
//
//    public void saveDatabaseUrl(String url, OnSaveCallback callback) {
//        boolean success = configRequest.saveUrlToFile(url);
//        callback.onSave(success);
//    }
//
//    public interface OnSaveCallback {
//        void onSave(boolean success);
//    }
//}



//package com.smartinvent.viewmodel;
//
//import androidx.lifecycle.ViewModel;
//
//import com.smartinvent.network.ApiClient;
//import com.smartinvent.network.ApiService;
//import com.smartinvent.network.DatabaseConfigRequest;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class DatabaseConfigViewModel extends ViewModel {
//
//    private final ApiService apiService;
//
//    public DatabaseConfigViewModel() {
//        apiService = ApiClient.getClient().create(ApiService.class);
//    }
//
//    public void saveDatabaseConfig(DatabaseConfigRequest request, SaveCallback callback) {
//        apiService.saveDatabaseConfig(request).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                callback.onResult(response.isSuccessful());
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                callback.onResult(false);
//            }
//        });
//    }
//
//    public interface SaveCallback {
//        void onResult(boolean success);
//    }
//}
