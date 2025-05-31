package com.smartinvent.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smartinvent.R;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.smartinvent.adapter.EmployeeAdapter;
import com.smartinvent.model.Employee;
import com.smartinvent.model.InventorySession;
import com.smartinvent.model.InventorySessionStatusEnum;
import com.smartinvent.network.ApiClient;
import com.smartinvent.service.EmployeeApi;
import com.smartinvent.service.InventoryApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateInventorySessionActivity extends AppCompatActivity {

    private EditText editTextSessionName, editTextSessionDescription, editTextEmployeeWorkId;
    private TextView textViewStartTime, textViewEndTime, textViewEmployeeFullName;
    private Button buttonSetCurrentTime, buttonSetFutureTime, buttonSetEndTimePlanned, buttonClearEndTime, buttonSelectEmployee, buttonSave;

    private LocalDateTime selectedStartTime = null;
    private LocalDateTime selectedEndTime = null;
    private InventorySessionStatusEnum inventorySessionStatus;
    private Long selectedEmployeeId = null;
    private final DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private InventoryApi inventoryApi;
    private EmployeeApi employeeApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_inventory_session);

        inventoryApi = ApiClient.getClient().create(InventoryApi.class);
        employeeApi = ApiClient.getClient().create(EmployeeApi.class);

        editTextSessionName = findViewById(R.id.editTextSessionName);
        editTextSessionDescription = findViewById(R.id.editTextDescription);
        editTextEmployeeWorkId = findViewById(R.id.editTextEmployeeWorkId);
        textViewEmployeeFullName = findViewById(R.id.textViewEmployeeFullName);
        editTextEmployeeWorkId.setEnabled(false);

        textViewStartTime = findViewById(R.id.textViewStartTime);
        textViewEndTime = findViewById(R.id.textViewEndTime);
        buttonSetCurrentTime = findViewById(R.id.buttonSetCurrentTime);
        buttonSetFutureTime = findViewById(R.id.buttonSetFutureTime);
        buttonSetEndTimePlanned = findViewById(R.id.buttonSetEndTime);
        buttonClearEndTime = findViewById(R.id.buttonClearEndTime);
        buttonSelectEmployee = findViewById(R.id.buttonSelectEmployee);
        buttonSave = findViewById(R.id.buttonCreateSession);

        buttonSetCurrentTime.setOnClickListener(v -> {
            selectedStartTime = LocalDateTime.now(ZoneId.systemDefault());
            textViewStartTime.setText(selectedStartTime.format(displayFormatter));
        });

        buttonSetFutureTime.setOnClickListener(v -> showDateTimePicker(true));

        buttonSetEndTimePlanned.setOnClickListener(v -> showDateTimePicker(false));


        buttonClearEndTime.setOnClickListener(v -> {
            selectedEndTime = null;
            textViewEndTime.setText("");
        });


        buttonSelectEmployee.setOnClickListener(v -> showEmployeeSearchDialog());

        buttonSave.setOnClickListener(v -> saveSession());
    }

    private void showDateTimePicker(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (timeView, hour, minute) -> {
                                LocalDateTime dateTime = LocalDateTime.of(year, month + 1, day, hour, minute);
                                if (!isStartTime && selectedStartTime != null && dateTime.isBefore(selectedStartTime)) {
                                    Toast.makeText(this, "Час завершення не може бути раніше початку", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (!isStartTime && selectedStartTime == null) {
                                    Toast.makeText(this, "Спочатку задайте час початку", Toast.LENGTH_SHORT).show();
                                    return;
                                }


                                if (isStartTime) {
                                    selectedStartTime = dateTime;
                                    textViewStartTime.setText(selectedStartTime.format(displayFormatter));
                                } else {
                                    selectedEndTime = dateTime;
                                    textViewEndTime.setText(selectedEndTime.format(displayFormatter));
                                }


                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    );
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveSession() {
        String sessionName = editTextSessionName.getText().toString().trim();
        String description = editTextSessionDescription.getText().toString().trim();

        if (sessionName.isEmpty() || selectedStartTime == null || selectedEmployeeId == null) {
            Toast.makeText(this, "Будь ласка, заповніть всі обов'язкові поля і виберіть співробітника", Toast.LENGTH_SHORT).show();
            return;
        }

        InventorySession session = new InventorySession();
        session.setName(sessionName);
        session.setDescription(description);


        Employee employee = new Employee();
        employee.setEmployeeId(selectedEmployeeId);

        session.setEmployee(employee);

        session.setStartTime(selectedStartTime);
        session.setEndTime(selectedEndTime);


        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime startUtc = selectedStartTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);

        InventorySessionStatusEnum statusEnum;
        if (startUtc.isAfter(nowUtc)) {
            statusEnum = InventorySessionStatusEnum.PLANNED;
        } else {
            statusEnum = InventorySessionStatusEnum.ACTIVE;
        }

        session.setStatus(statusEnum);

        inventoryApi.createSession(session).enqueue(new Callback<InventorySession>() {
            @Override
            public void onResponse(Call<InventorySession> call, Response<InventorySession> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateInventorySessionActivity.this, "Сесію створено!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateInventorySessionActivity.this, "Помилка при створенні", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InventorySession> call, Throwable t) {
                Toast.makeText(CreateInventorySessionActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showEmployeeSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_employee_search, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText editTextSearchQuery = dialogView.findViewById(R.id.editTextSearchQuery);
        Button buttonSearch = dialogView.findViewById(R.id.buttonSearch);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerViewEmployees);

        List<Employee> employees = new ArrayList<>();
        EmployeeAdapter adapter = new EmployeeAdapter(employees, new EmployeeAdapter.OnEmployeeClickListener() {
            @Override
            public void onEmployeeClick(Employee employee) {
                editTextEmployeeWorkId.setText(employee.getEmployeeWorkId());
                textViewEmployeeFullName.setText(employee.getLastName() + " " + employee.getFirstName());
                selectedEmployeeId = employee.getEmployeeId();
                dialog.dismiss();
            }

            @Override
            public void onEmployeeDeselected() {
                // При потребі додайте логіку
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonSearch.setOnClickListener(v -> {
            String query = editTextSearchQuery.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(this, "Введіть пошуковий запит", Toast.LENGTH_SHORT).show();
                return;
            }
            performEmployeeSearch(query, employees, adapter);
        });

        dialog.show();
    }

    private void performEmployeeSearch(String query, List<Employee> employees, EmployeeAdapter adapter) {
        employeeApi.searchEmployees(query).enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                employees.clear();
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    employees.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CreateInventorySessionActivity.this, "Співробітників не знайдено", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Toast.makeText(CreateInventorySessionActivity.this, "Помилка пошуку: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
