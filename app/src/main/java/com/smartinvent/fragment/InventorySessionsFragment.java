package com.smartinvent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartinvent.R;
import com.smartinvent.activity.InventoryProductListActivity;
import com.smartinvent.adapter.InventorySessionAdapter;
import com.smartinvent.model.Constants;
import com.smartinvent.model.InventorySession;
import com.smartinvent.model.InventorySessionStatusEnum;
import com.smartinvent.network.ApiClient;
import com.smartinvent.service.InventoryApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventorySessionsFragment extends Fragment implements InventorySessionAdapter.OnSessionClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private InventorySessionAdapter adapter;
    private List<InventorySession> allSessions = new ArrayList<>();
    private Button btnAll, btnActive, btnCompleted, btnSortDate;
    private boolean isAscending = false;

    public InventorySessionsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_sessions, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSessions);
        progressBar = view.findViewById(R.id.progressBarSessions);
//        btnAll = view.findViewById(R.id.btn_all);
//        btnActive = view.findViewById(R.id.btn_active);
//        btnCompleted = view.findViewById(R.id.btn_completed);
//        btnSortDate = view.findViewById(R.id.btn_sort_by_date);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchSessions();

//        btnAll.setOnClickListener(v -> filterSessions(null));
//        btnActive.setOnClickListener(v -> filterSessions(InventorySessionStatusEnum.ACTIVE));
//        btnCompleted.setOnClickListener(v -> filterSessions(InventorySessionStatusEnum.COMPLETED));
//        btnSortDate.setOnClickListener(v -> toggleSortByDate());

        return view;
    }


    private void fetchSessions() {
        progressBar.setVisibility(View.VISIBLE);

        InventoryApi apiService = ApiClient.getClient().create(InventoryApi.class);
        Call<List<InventorySession>> call = apiService.getAllSessions();

        call.enqueue(new Callback<List<InventorySession>>() {
            @Override
            public void onResponse(Call<List<InventorySession>> call, Response<List<InventorySession>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    // Фільтрація лише активних сесій
                    allSessions = new ArrayList<>();
                    for (InventorySession session : response.body()) {
                        if (session.getStatus() == InventorySessionStatusEnum.ACTIVE) {
                            allSessions.add(session);
                        }
                    }
                    adapter = new InventorySessionAdapter(allSessions, requireContext(), InventorySessionsFragment.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Помилка завантаження сесій", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<InventorySession>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Помилка підключення", Toast.LENGTH_SHORT).show();
            }
        });
    }
//
//    private void fetchSessions() {
//        progressBar.setVisibility(View.VISIBLE);
//
//        InventoryApi apiService = ApiClient.getClient().create(InventoryApi.class);
//        Call<List<InventorySession>> call = apiService.getAllSessions();
//
//        call.enqueue(new Callback<List<InventorySession>>() {
//            @Override
//            public void onResponse(Call<List<InventorySession>> call, Response<List<InventorySession>> response) {
//                progressBar.setVisibility(View.GONE);
//                if (response.isSuccessful() && response.body() != null) {
//                    allSessions = response.body();
//                    adapter = new InventorySessionAdapter(allSessions, requireContext(), InventorySessionsFragment.this);
//                    recyclerView.setAdapter(adapter);
//                } else {
//                    Toast.makeText(getContext(), "Помилка завантаження сесій", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<InventorySession>> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getContext(), "Помилка підключення", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void filterSessions(@Nullable InventorySessionStatusEnum status) {
        List<InventorySession> filteredList = new ArrayList<>();

        for (InventorySession session : allSessions) {
            if (status == null || session.getStatus() == status) {
                filteredList.add(session);
            }
        }

        adapter.updateList(filteredList);
    }

    private void toggleSortByDate() {
        isAscending = !isAscending;

        Collections.sort(allSessions, (o1, o2) -> {
            if (o1.getStartTime() == null || o2.getStartTime() == null) return 0;
            return isAscending ? o1.getStartTime().compareTo(o2.getStartTime())
                    : o2.getStartTime().compareTo(o1.getStartTime());
        });

        adapter.updateList(new ArrayList<>(allSessions));
    }

    @Override
    public void onSessionClick(InventorySession session) {
        Intent intent = new Intent(getContext(), InventoryProductListActivity.class);
        intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, session.getInventorySessionId());
        Log.d("SessionID InventorySessionsFragment", session.getInventorySessionId() + "");
        startActivity(intent);
    }
}