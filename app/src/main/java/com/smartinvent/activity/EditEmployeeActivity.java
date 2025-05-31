package com.smartinvent.activity;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.R;
import com.smartinvent.model.Company;
import com.smartinvent.model.Employee;
import com.smartinvent.model.RoleEnum;
import com.smartinvent.service.EmployeeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditEmployeeActivity extends AppCompatActivity {

    private EditText editFirstName, editLastName, editEmail, editPhone, editEmployeeWorkId;
    private AutoCompleteTextView spinnerRole;
    private Button btnUpdate, btnCancel;

    private EmployeeService employeeService = new EmployeeService();
    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        // Ініціалізація елементів
        editFirstName = findViewById(R.id.edt_first_name);
        editLastName = findViewById(R.id.edt_last_name);
        editEmail = findViewById(R.id.edt_email);
        editPhone = findViewById(R.id.edt_phone);
        editEmployeeWorkId = findViewById(R.id.edt_employee_work_id);
        spinnerRole = findViewById(R.id.spinner_role);
        btnUpdate = findViewById(R.id.btn_update_employee);
        btnCancel = findViewById(R.id.btn_cancel_employee);

        ArrayAdapter<RoleEnum> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, RoleEnum.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        employee = getIntent().getParcelableExtra("employee");
        if (employee != null) {
            fillFields();
        } else {
            Toast.makeText(this, "❌ Дані співробітника не передані", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnUpdate.setOnClickListener(v -> validateAndCheckUniqueness());
        btnCancel.setOnClickListener(v -> finish());
    }


    private void validateAndCheckUniqueness() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String employeeWorkId = editEmployeeWorkId.getText().toString().trim();

        String selectedRoleName = spinnerRole.getText().toString();
        RoleEnum role;
        try {
            role = RoleEnum.valueOf(selectedRoleName);
        } catch (IllegalArgumentException e) {
            role = null;
        }

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || employeeWorkId.isEmpty() || role == null) {
            Toast.makeText(this, "❗ Заповніть усі поля", Toast.LENGTH_SHORT).show();
            return;
        }

        final RoleEnum roleFinal = role; // Оголошуємо final для використання в анонімному класі

        if (email.equals(employee.getEmail()) && phone.equals(employee.getPhone()) && employeeWorkId.equals(employee.getEmployeeWorkId())) {
            updateEmployee(firstName, lastName, email, phone, employeeWorkId, roleFinal);
            return;
        }

        employeeService.isUniqueEmployeeExclude(employee.getCompany().getCompanyId(), email, phone, employeeWorkId, employee.getEmployeeId(), new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    updateEmployee(firstName, lastName, email, phone, employeeWorkId, roleFinal);
                } else {
                    Toast.makeText(EditEmployeeActivity.this, "❌ Співробітник з такими даними вже існує", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(EditEmployeeActivity.this, "❌ Помилка перевірки унікальності", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void fillFields() {
        editFirstName.setText(employee.getFirstName());
        editLastName.setText(employee.getLastName());
        editEmail.setText(employee.getEmail());
        editPhone.setText(employee.getPhone());
        editEmployeeWorkId.setText(employee.getEmployeeWorkId());
        spinnerRole.setText(employee.getRole().name(), false); // для AutoCompleteTextView
    }

    private void updateEmployee(String firstName, String lastName, String email, String phone, String employeeWorkId, RoleEnum role) {
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPhone(phone);
        employee.setEmployeeWorkId(employeeWorkId);
        employee.setRole(role);

        employeeService.updateEmployee(employee.getEmployeeId(), employee, new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditEmployeeActivity.this, "✅ Співробітника оновлено", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditEmployeeActivity.this, "❌ Не вдалося оновити", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(EditEmployeeActivity.this, "❌ Серверна помилка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
