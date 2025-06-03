package com.smartinvent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smartinvent.R;
import com.smartinvent.activity.AddEmployeeActivity;
import com.smartinvent.activity.EditEmployeeActivity;
import com.smartinvent.adapter.EmployeeAdapter;
import com.smartinvent.model.Constants;
import com.smartinvent.model.Employee;
import com.smartinvent.service.EmployeeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
public class EmployeeFragment extends Fragment {

    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;
    private List<Employee> employeeList = new ArrayList<>();
    private EmployeeService employeeService;
    private ImageButton btnAddEmployee, btnEditEmployee, btnDeleteEmployee, btnRefreshEmployees;
    private static final int REQUEST_ADD_EMPLOYEE = 1;
    private static final int REQUEST_EDIT_EMPLOYEE = 2;
    private Long companyId;

    public EmployeeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewEmployees);
        btnAddEmployee = view.findViewById(R.id.btn_add_employee);
        btnEditEmployee = view.findViewById(R.id.btn_edit_employee);
        btnDeleteEmployee = view.findViewById(R.id.btn_delete_employee);
        btnRefreshEmployees = view.findViewById(R.id.btn_refresh_employees);

        btnEditEmployee.setEnabled(false);
        btnDeleteEmployee.setEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        employeeService = new EmployeeService();

        employeeAdapter = new EmployeeAdapter(employeeList, new EmployeeAdapter.OnEmployeeClickListener() {
            @Override
            public void onEmployeeClick(Employee employee) {
                btnEditEmployee.setEnabled(true);
                btnDeleteEmployee.setEnabled(true);
            }

            @Override
            public void onEmployeeDeselected() {
                btnEditEmployee.setEnabled(false);
                btnDeleteEmployee.setEnabled(false);
            }
        });
        recyclerView.setAdapter(employeeAdapter);

        // Витягуємо companyId із SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        companyId = sharedPreferences.getLong(Constants.KEY_COMPANY_ID, -1L);
        if (companyId == -1L) {
            Toast.makeText(getContext(), "Не вдалось отримати ID компанії", Toast.LENGTH_SHORT).show();
        }

        loadEmployees();

        btnAddEmployee.setOnClickListener(v -> openAddEmployeeActivity());
        btnEditEmployee.setOnClickListener(v -> openEditEmployeeActivity());
        btnDeleteEmployee.setOnClickListener(v -> deleteSelectedEmployee());
        btnRefreshEmployees.setOnClickListener(v -> loadEmployees());

        return view;
    }

    private void loadEmployees() {
        if (companyId == null || companyId == -1L) {
            Toast.makeText(getContext(), "Невідомий ID компанії", Toast.LENGTH_SHORT).show();
            return;
        }


        employeeService.getAllEmployees(companyId, new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                employeeList.clear();
                if (response.isSuccessful() && response.body() != null) {
                    employeeList.addAll(response.body());
                } else {
                    Log.w("EmployeeFragment", "⚠️ Не вдалося отримати список співробітників");
                }
                employeeAdapter.setEmployees(employeeList);
                employeeAdapter.clearSelection();
                btnEditEmployee.setEnabled(false);
                btnDeleteEmployee.setEnabled(false);
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.e("EmployeeFragment", "❌ Помилка завантаження співробітників", t);
                Toast.makeText(getContext(), "Помилка з'єднання з сервером", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openAddEmployeeActivity() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), AddEmployeeActivity.class);
            startActivityForResult(intent, REQUEST_ADD_EMPLOYEE);
        }
    }

    private void openEditEmployeeActivity() {
        Employee selected = employeeAdapter.getSelectedEmployee();
        if (selected == null) {
            Toast.makeText(getContext(), "Оберіть співробітника", Toast.LENGTH_SHORT).show();
            return;
        }
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), EditEmployeeActivity.class);
            intent.putExtra(Constants.KEY_EMPLOYEE, selected);
            startActivityForResult(intent, REQUEST_EDIT_EMPLOYEE);
        }
    }

    private void deleteSelectedEmployee() {
        Employee selected = employeeAdapter.getSelectedEmployee();
        if (selected == null) {
            Toast.makeText(getContext(), "Оберіть співробітника", Toast.LENGTH_SHORT).show();
            return;
        }

        employeeService.deleteEmployee(selected.getEmployeeId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "✅ Співробітника видалено", Toast.LENGTH_SHORT).show();
                    loadEmployees();
                } else {
                    Toast.makeText(getContext(), "❌ Помилка при видаленні", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("EmployeeFragment", "❌ Помилка при видаленні", t);
                Toast.makeText(getContext(), "Помилка з'єднання з сервером", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK &&
                (requestCode == REQUEST_ADD_EMPLOYEE || requestCode == REQUEST_EDIT_EMPLOYEE)) {
            loadEmployees();
        }
    }
}
