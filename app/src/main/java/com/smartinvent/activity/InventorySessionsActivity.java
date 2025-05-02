//package com.smartinvent.activity;
//
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//import com.smartinvent.R;
//import com.smartinvent.adapter.InventorySessionAdapter;
//import com.smartinvent.model.InventorySession;
//import com.smartinvent.network.ApiClient;
//import com.smartinvent.network.ApiService;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class InventorySessionsActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private InventorySessionAdapter adapter;
//    private List<InventorySession> inventorySessionList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_inventory_sessions);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        loadInventorySessions();
//    }
//
//    private void loadInventorySessions() {
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//        Call<List<InventorySession>> call = apiService.getActiveInventorySessions();
//
//        call.enqueue(new Callback<List<InventorySession>>() {
//            @Override
//            public void onResponse(Call<List<InventorySession>> call, Response<List<InventorySession>> response) {
//                if (response.isSuccessful()) {
//                    inventorySessionList = response.body();
//                    adapter = new InventorySessionAdapter(InventorySessionsActivity.this, inventorySessionList);
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<InventorySession>> call, Throwable t) {
//                // Handle error
//            }
//        });
//    }
//}
