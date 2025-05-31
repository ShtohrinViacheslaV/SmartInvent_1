package com.smartinvent.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartinvent.R;
import com.smartinvent.adapter.StorageAdapter;
import com.smartinvent.model.Company;
import com.smartinvent.model.Storage;
import com.smartinvent.service.StorageService;

import java.util.ArrayList;
import java.util.List;

public class ManageStoragesActivity extends AppCompatActivity implements StorageAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private StorageAdapter adapter;
    private List<Storage> storageList = new ArrayList<>();
    private StorageService storageService;

    private Button btnAdd, btnEdit, btnDelete, btnRefresh, btnSave, btnCancel;
    private EditText editName, editLocation, editDetails;
    private View formLayout;
    private TextView formTitle;

    private Storage selectedStorage = null;
    private boolean isEditMode = false;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private long companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_storages);

        storageService = new StorageService();

        initViews();

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        companyId = sharedPreferences.getLong("companyId", -1);
        Log.d("ManageStoragesActivity", "companyId: " + companyId);

        if (companyId == -1) {
            showToast("Не вдалося отримати компанію користувача");
            finish();
            return;
        }

        updateActionButtonsState();
        loadStorages();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewStorages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StorageAdapter(storageList, this);
        recyclerView.setAdapter(adapter);

        btnAdd = findViewById(R.id.buttonAddStorage);
        btnEdit = findViewById(R.id.buttonEditStorage);
        btnDelete = findViewById(R.id.buttonDeleteStorage);
        btnRefresh = findViewById(R.id.buttonRefreshStorages);
        btnSave = findViewById(R.id.buttonSaveStorage);
        btnCancel = findViewById(R.id.buttonCancel);

        editName = findViewById(R.id.editTextStorageName);
        editLocation = findViewById(R.id.editTextStorageLocation);
        editDetails = findViewById(R.id.editTextStorageDetails);
        formLayout = findViewById(R.id.formLayout);
        formTitle = findViewById(R.id.formTitle);

        setupListeners();


    }

    private void setupListeners() {
        btnAdd.setOnClickListener(v -> showFormForCreate());
        btnEdit.setOnClickListener(v -> {
            if (selectedStorage != null) {
                showFormForEdit();
            } else {
                showToast("Оберіть склад для редагування");
            }
        });
        btnDelete.setOnClickListener(v -> {
            if (selectedStorage != null) {
                deleteSelectedStorage();
            } else {
                showToast("Оберіть склад для видалення");
            }
        });
        btnRefresh.setOnClickListener(v -> loadStorages());
        btnSave.setOnClickListener(v -> saveStorage());
        btnCancel.setOnClickListener(v -> hideForm());
    }


    private void loadStorages() {
        storageService.getAllStorages(new StorageService.StorageCallback() {
            @Override
            public void onSuccess(List<Storage> storages) {
                storageList.clear();
                storageList.addAll(storages);
                adapter.notifyDataSetChanged();

                clearSelection();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ManageStoragesActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFormForCreate() {
        isEditMode = false;
        clearSelection();

        formTitle.setText("Створити склад");
        clearFormFields();
        formLayout.setVisibility(View.VISIBLE);
        setUiEnabled(false);
    }

    private void showFormForEdit() {
        isEditMode = true;
        formTitle.setText("Редагувати склад");
        editName.setText(selectedStorage.getName());
        editLocation.setText(selectedStorage.getLocation());
        editDetails.setText(selectedStorage.getDetails());
        formLayout.setVisibility(View.VISIBLE);
        setUiEnabled(false);
    }

    private void hideForm() {
        formLayout.setVisibility(View.GONE);
        clearFormFields();
        clearSelection();
        setUiEnabled(true);
    }

    private void clearFormFields() {
        editName.setText("");
        editLocation.setText("");
        editDetails.setText("");
    }

    private void clearSelection() {
        selectedStorage = null;
        selectedPosition = RecyclerView.NO_POSITION;
        adapter.setSelectedPosition(selectedPosition);
        updateActionButtonsState();
    }

    private void setUiEnabled(boolean enabled) {
        recyclerView.setEnabled(enabled);
        btnAdd.setEnabled(enabled);
        btnEdit.setEnabled(enabled && selectedStorage != null);
        btnDelete.setEnabled(enabled && selectedStorage != null);
        btnRefresh.setEnabled(enabled);
    }

    private void saveStorage() {
        String name = editName.getText().toString().trim();
        String location = editLocation.getText().toString().trim();
        String details = editDetails.getText().toString().trim();


        if (name.isEmpty()) {
            Toast.makeText(this, "Назва обов'язкова", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode && selectedStorage != null) {
            updateStorage(name, location, details);
        } else {
            createNewStorage(name, location, details);
        }
    }

    private void updateStorage(String name, String location, String details) {
        selectedStorage.setName(name);
        selectedStorage.setLocation(location);
        selectedStorage.setDetails(details);
        storageService.updateStorage(selectedStorage, new StorageService.SingleStorageCallback() {
            @Override
            public void onSuccess(Storage storage) {
                showToast("Склад оновлено");
                hideForm();
                loadStorages();
            }

            @Override
            public void onFailure(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    private void createNewStorage(String name, String location, String details) {
        Company company = new Company();
        company.setCompanyId(companyId);

        Storage newStorage = new Storage(company, name, location, details);
        storageService.createStorage(newStorage, new StorageService.SingleStorageCallback() {
            @Override
            public void onSuccess(Storage storage) {
                showToast("Склад створено");
                hideForm();
                loadStorages();
            }

            @Override
            public void onFailure(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    private void deleteSelectedStorage() {
        storageService.deleteStorage(selectedStorage.getStorageId(), new StorageService.SimpleCallback() {
            @Override
            public void onSuccess() {
                showToast("Склад видалено");
                clearSelection();
                loadStorages();
            }

            @Override
            public void onFailure(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    @Override
    public void onItemClick(Storage storage) {
        selectedStorage = storage;
        selectedPosition = storageList.indexOf(storage);
        adapter.setSelectedPosition(selectedPosition);
        showToast("Обрано: " + storage.getName());
        updateActionButtonsState();
    }

    private void updateActionButtonsState() {
        boolean hasSelection = selectedStorage != null;
        btnEdit.setEnabled(hasSelection);
        btnDelete.setEnabled(hasSelection);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
