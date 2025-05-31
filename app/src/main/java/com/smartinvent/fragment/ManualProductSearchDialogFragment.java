package com.smartinvent.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.smartinvent.R;
import com.smartinvent.adapter.InventorySessionProductAdapter;
import com.smartinvent.model.InventoryProductResultDto;
import com.smartinvent.service.InventoryService;

import java.util.ArrayList;
import java.util.List;

public class ManualProductSearchDialogFragment extends DialogFragment {

    private EditText editTextSearch;
    private RecyclerView recyclerViewResults;
    private MaterialButton btnSearch;
    private InventorySessionProductAdapter adapter;
    private List<InventoryProductResultDto> searchResults = new ArrayList<>();

    private InventoryService inventoryService;
    private Long inventorySessionId;

    // Інтерфейс для callback з вибраним продуктом
    public interface OnProductSelectedListener {
        void onProductSelected(InventoryProductResultDto product);
    }

    private OnProductSelectedListener listener;

    public void setOnProductSelectedListener(OnProductSelectedListener listener) {
        this.listener = listener;
    }

    public static ManualProductSearchDialogFragment newInstance(Long sessionId) {
        ManualProductSearchDialogFragment fragment = new ManualProductSearchDialogFragment();
        Bundle args = new Bundle();
        args.putLong("inventorySessionId", sessionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_manual_product_search, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        recyclerViewResults = view.findViewById(R.id.recyclerViewResults);
        btnSearch = view.findViewById(R.id.btnSearch);

        inventorySessionId = getArguments() != null ? getArguments().getLong("inventorySessionId") : -1;

        inventoryService = new InventoryService();

        adapter = new InventorySessionProductAdapter(searchResults, getContext());
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewResults.setAdapter(adapter);

        adapter.setOnItemClickListener(product -> {
            if (product.getProductId() == null || inventorySessionId == -1) {
                Toast.makeText(getContext(), "Некоректні параметри", Toast.LENGTH_SHORT).show();
                return;
            }

            if (listener != null) {
                listener.onProductSelected(product);
                dismiss();
            }
        });


        btnSearch.setOnClickListener(v -> performSearch());

        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        return view;
    }

    private void performSearch() {
        String query = editTextSearch.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(getContext(), "Введіть пошуковий запит", Toast.LENGTH_SHORT).show();
            return;
        }

        inventoryService.getInventoryResultsBySession(inventorySessionId, new InventoryService.InventoryProductResultCallback() {
            @Override
            public void onSuccess(List<InventoryProductResultDto> products) {
                searchResults.clear();

                // Пошук по частковому входженню в productWorkId (ігноруємо регістр)
                String lowerQuery = query.toLowerCase();
                for (InventoryProductResultDto p : products) {
                    if (p.getProductWorkId() != null && p.getProductWorkId().toLowerCase().contains(lowerQuery)) {
                        searchResults.add(p);
                    }
                }

                if (searchResults.isEmpty()) {
                    Toast.makeText(getContext(), "Продукти не знайдені", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Помилка пошуку: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
