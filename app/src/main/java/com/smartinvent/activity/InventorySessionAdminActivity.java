package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartinvent.R;
import com.smartinvent.adapter.InventorySessionAdapter;
import com.smartinvent.model.InventorySession;
import com.smartinvent.network.ApiClient;

import java.util.List;

import com.smartinvent.service.InventoryApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventorySessionAdminActivity extends AppCompatActivity implements InventorySessionAdapter.OnSessionClickListener {

    private RecyclerView recyclerView;
    private InventorySessionAdapter adapter;
    private Button btnCreateNew, btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_session_admin);

        recyclerView = findViewById(R.id.recyclerViewSessions);
        btnCreateNew = findViewById(R.id.btn_add_session);
        btnRefresh = findViewById(R.id.btn_refresh_sessions);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new InventorySessionAdapter(List.of(), this, this);
        recyclerView.setAdapter(adapter);

        loadSessions();

        btnCreateNew.setOnClickListener(v -> {
            Intent intent = new Intent(InventorySessionAdminActivity.this, CreateInventorySessionActivity.class);
            startActivity(intent);
        });

        btnRefresh.setOnClickListener(v -> loadSessions());
    }

    private void loadSessions() {
        InventoryApi apiService = ApiClient.getClient().create(InventoryApi.class);
        Call<List<InventorySession>> call = apiService.getAllSessions();

        call.enqueue(new Callback<List<InventorySession>>() {
            @Override
            public void onResponse(Call<List<InventorySession>> call, Response<List<InventorySession>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateList(response.body());
                } else {
                    Toast.makeText(InventorySessionAdminActivity.this, "Не вдалося завантажити сесії", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<InventorySession>> call, Throwable t) {
                Toast.makeText(InventorySessionAdminActivity.this, "Помилка при з'єднанні з сервером", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSessionClick(InventorySession session) {
        Intent intent = new Intent(this, InventorySessionDetailsActivity.class);
        intent.putExtra("session_id", session.getInventorySessionId());
        startActivity(intent);
    }
}
