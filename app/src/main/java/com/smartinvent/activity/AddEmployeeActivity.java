package com.smartinvent.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.R;
import com.smartinvent.model.Company;
import com.smartinvent.model.Constants;
import com.smartinvent.model.Employee;
import com.smartinvent.model.RoleEnum;
import com.smartinvent.service.EmployeeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText addFirstName, addLastName, addEmail, addPhone, addEmployeeWorkId;
    private AutoCompleteTextView spinnerRole;
    private Button btnSave, btnCancel;
    private EmployeeService employeeService = new EmployeeService();
    private Long companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        // Ініціалізація елементів
        addFirstName = findViewById(R.id.add_first_name);
        addLastName = findViewById(R.id.add_last_name);
        addEmail = findViewById(R.id.add_email);
        addPhone = findViewById(R.id.add_phone);
        addEmployeeWorkId = findViewById(R.id.add_employee_work_id);
        spinnerRole = findViewById(R.id.spinner_role);
        btnSave = findViewById(R.id.btn_save_employee);
        btnCancel = findViewById(R.id.btn_cancel_employee);

        // Отримання companyId з SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        companyId = prefs.getLong(Constants.KEY_COMPANY_ID, -1);
        if (companyId == -1) {
            Toast.makeText(this, "❌ Company ID не знайдено", Toast.LENGTH_SHORT).show();
            finish();
        }



        ArrayAdapter<RoleEnum> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, RoleEnum.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        btnSave.setOnClickListener(v -> validateAndCheckUniqueness());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void validateAndCheckUniqueness() {

        String firstName = addFirstName.getText().toString().trim();
        String lastName = addLastName.getText().toString().trim();
        String email = addEmail.getText().toString().trim();
        String phone = addPhone.getText().toString().trim();
        String employeeWorkId = addEmployeeWorkId.getText().toString().trim();

        String selectedRoleName = spinnerRole.getText().toString();
        RoleEnum role;
        try {
            role = RoleEnum.valueOf(selectedRoleName);
        } catch (IllegalArgumentException e) {
            role = null;
        }

        // Перевірка на порожні поля
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || employeeWorkId.isEmpty() || role == null) {
            Toast.makeText(this, "❗ Будь ласка, заповніть усі поля", Toast.LENGTH_SHORT).show();
            return;
        }

        final RoleEnum roleFinal = role;


        // Запит на перевірку унікальності
        employeeService.isUniqueEmployee(companyId, email, phone, employeeWorkId, new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    createEmployee(firstName, lastName, email, phone, employeeWorkId, roleFinal);
                } else {
                    Toast.makeText(AddEmployeeActivity.this, "❌ Співробітник з такими даними вже існує", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(AddEmployeeActivity.this, "❌ Помилка перевірки унікальності", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createEmployee(String firstName, String lastName, String email, String phone, String employeeWorkId, RoleEnum role) {

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPhone(phone);
        employee.setEmployeeWorkId(employeeWorkId);
        employee.setRole(role);
        employee.setCompany(new Company(companyId));
        employee.setPasswordHash(null);

        employeeService.createEmployee(employee, new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEmployeeActivity.this, "✅ Співробітника додано", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddEmployeeActivity.this, "❌ Помилка створення співробітника", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(AddEmployeeActivity.this, "❌ Серверна помилка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
