package com.smartinvent.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.smartinvent.localdb.InventoryScanDao;
import com.smartinvent.localdb.InventoryScanDatabase;
import com.smartinvent.localdb.InventoryScanEntity;
import com.smartinvent.model.InventoryScan;
import com.smartinvent.network.InventoryApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InventoryScanViewModel extends AndroidViewModel {

    private final InventoryScanDao scanDao;
    private final InventoryApiService apiService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<InventoryScan>> scanLiveData = new MutableLiveData<>();

    public InventoryScanViewModel(Application application, InventoryApiService apiService) {
        super(application);
        this.scanDao = InventoryScanDatabase.getDatabase(application).inventoryScanDao();
        this.apiService = apiService;
    }

    // Додаємо товар локально
    public void insertScanLocally(InventoryScanEntity scan) {
        executorService.execute(() -> scanDao.insertScan(scan));
    }

    // Отримуємо список сканувань локально
    public LiveData<List<InventoryScanEntity>> getLocalScansByProduct(long productId) {
        return scanDao.getScansByProduct(productId);
    }

    // Отримуємо дані з сервера
    public LiveData<List<InventoryScan>> getScansByProduct(long productId) {
        apiService.getScansByProduct(productId).enqueue(new Callback<List<InventoryScan>>() {
            @Override
            public void onResponse(Call<List<InventoryScan>> call, Response<List<InventoryScan>> response) {
                if (response.isSuccessful()) {
                    scanLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<InventoryScan>> call, Throwable t) {
                scanLiveData.postValue(null);
            }
        });
        return scanLiveData;
    }

    // Відправляємо дані на сервер
    public void sendScanToServer(InventoryScan scan) {
        apiService.sendScan(scan).enqueue(new Callback<InventoryScan>() {
            @Override
            public void onResponse(Call<InventoryScan> call, Response<InventoryScan> response) {
                // Дані успішно відправлені
            }

            @Override
            public void onFailure(Call<InventoryScan> call, Throwable t) {
                // Помилка відправки
            }
        });
    }
}
