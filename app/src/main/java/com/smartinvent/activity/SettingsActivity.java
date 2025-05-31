package com.smartinvent.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.smartinvent.R;
import com.smartinvent.model.Employee;
import com.smartinvent.service.EmployeeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private EmployeeService employeeService = new EmployeeService();
    private long employeeId;

    // Поля для відображення інформації про працівника
    private TextView tvFirstName, tvLastName, tvPhone, tvEmail, tvRole, tvEmployeeWorkId;

    // Поля для зміни пароля
    private TextInputEditText etOldPassword, etNewPassword, etConfirmNewPassword;
    private Button btnChangePassword;

    // Поля для зміни телефону
    private TextInputEditText etNewPhone;
    private Button btnChangePhone;

    // Поля для зміни email
    private TextInputEditText etNewEmail;
    private Button btnChangeEmail;

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        employeeId = sharedPreferences.getLong("employeeId", -1);  // Ініціалізація поля класу

        if (employeeId == -1) {
            Toast.makeText(this, "Користувач не авторизований", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadEmployeeInfo();
        setupListeners();
    }


    private void initViews() {
        // Працівник
        tvFirstName = findViewById(R.id.employeeFirstName);
        tvLastName = findViewById(R.id.employeeLastName);
        tvPhone = findViewById(R.id.employeePhoneNumber);
        tvEmail = findViewById(R.id.employeeEmail);
        tvRole = findViewById(R.id.employeeRole);
        tvEmployeeWorkId = findViewById(R.id.empoyeeWorkId);

        // Пароль
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        // Телефон
        etNewPhone = findViewById(R.id.etNewPhone);
        btnChangePhone = findViewById(R.id.btnChangePhone);

        // Email
        etNewEmail = findViewById(R.id.etNewEmail);
        btnChangeEmail = findViewById(R.id.btnChangeEmail);  // додай у XML цю кнопку, якщо її нема

        btnLogout = findViewById(R.id.btnLogout);

    }

    private void loadEmployeeInfo() {
        employeeService.getEmployeeById(employeeId, new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Employee employee = response.body();

                    runOnUiThread(() -> {
                        tvFirstName.setText(employee.getFirstName());
                        tvLastName.setText(employee.getLastName());
                        tvPhone.setText(employee.getPhone());
                        tvEmail.setText(employee.getEmail());
                        tvRole.setText(employee.getRole().toString());
                        tvEmployeeWorkId.setText(String.valueOf(employee.getEmployeeWorkId()));
                    });

                } else {
                    runOnUiThread(() -> Toast.makeText(SettingsActivity.this, "Не вдалося завантажити інформацію про працівника", Toast.LENGTH_LONG).show());
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                runOnUiThread(() -> Toast.makeText(SettingsActivity.this, "Помилка мережі: " + t.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void setupListeners() {

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // очищає стек
            startActivity(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            String oldPass = etOldPassword.getText().toString().trim();
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmNewPassword.getText().toString().trim();

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Усі поля пароля мають бути заповнені", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Новий пароль і підтвердження не збігаються", Toast.LENGTH_SHORT).show();
                return;
            }

            employeeService.updatePassword(employeeId, oldPass, newPass, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> Toast.makeText(SettingsActivity.this, "Пароль змінено успішно", Toast.LENGTH_SHORT).show());
                    } else {
                        String error = "Помилка зміни пароля";
                        if (response.errorBody() != null) {
                            try {
                                error = response.errorBody().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        String finalError = error;
                        runOnUiThread(() -> Toast.makeText(SettingsActivity.this, finalError, Toast.LENGTH_LONG).show());
                    }
                }


                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    runOnUiThread(() -> Toast.makeText(SettingsActivity.this, "Помилка мережі: " + t.getMessage(), Toast.LENGTH_LONG).show());
                }
            });
        });

        btnChangePhone.setOnClickListener(v -> {
            String phone = etNewPhone.getText().toString().trim();

            if (!Patterns.PHONE.matcher(phone).matches()) {
                Toast.makeText(this, "Некоректний номер телефону", Toast.LENGTH_SHORT).show();
                return;
            }

            // Припустимо, email не змінюємо тут, отримаємо з текстового поля або старого профілю
            String currentEmail = tvEmail.getText().toString();

            employeeService.updateContactInfo(employeeId, currentEmail, phone, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            Toast.makeText(SettingsActivity.this, "Номер телефону змінено", Toast.LENGTH_SHORT).show();
                            tvPhone.setText(phone);
                        });
                    } else {
                        String error = "Помилка зміни номера телефону";
                        if (response.errorBody() != null) {
                            try {
                                error = response.errorBody().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        String finalError = error;
                        runOnUiThread(() -> Toast.makeText(SettingsActivity.this, finalError, Toast.LENGTH_LONG).show());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    runOnUiThread(() -> Toast.makeText(SettingsActivity.this, "Помилка мережі: " + t.getMessage(), Toast.LENGTH_LONG).show());
                }
            });
        });

        if (btnChangeEmail != null) {
            btnChangeEmail.setOnClickListener(v -> {
                String email = etNewEmail.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Некоректний email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Телефон беремо з поточного відображення
                String currentPhone = tvPhone.getText().toString();

                employeeService.updateContactInfo(employeeId, email, currentPhone, new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                Toast.makeText(SettingsActivity.this, "Email змінено", Toast.LENGTH_SHORT).show();
                                tvEmail.setText(email);
                            });
                        } else {
                            String error = "Помилка зміни email";
                            if (response.errorBody() != null) {
                                try {
                                    error = response.errorBody().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            String finalError = error;
                            runOnUiThread(() -> Toast.makeText(SettingsActivity.this, finalError, Toast.LENGTH_LONG).show());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        runOnUiThread(() -> Toast.makeText(SettingsActivity.this, "Помилка мережі: " + t.getMessage(), Toast.LENGTH_LONG).show());
                    }
                });
            });
        }
    }
}
