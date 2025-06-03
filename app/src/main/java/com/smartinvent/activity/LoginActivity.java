package com.smartinvent.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.R;
import com.smartinvent.config.ApiConfig;
import com.smartinvent.config.DatabaseConfig;
import com.smartinvent.config.DbConfigManager;
import com.smartinvent.model.AuthRequest;
import com.smartinvent.model.AuthResponse;
import com.smartinvent.model.Constants;
import com.smartinvent.model.RoleEnum;
import com.smartinvent.network.ApiClient;
import com.smartinvent.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText workIdInput, passwordInput;
    private ApiService apiService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        workIdInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        updateApiClient(); // Оновлюємо API клієнт при старті
    }

    private void updateApiClient() {
        if (DbConfigManager.isConfigAvailable(this)) {
            DatabaseConfig config = DbConfigManager.loadConfig(this);
            if (config == null) {
                Toast.makeText(this, "Будь ласка, налаштуйте базу даних!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, DatabaseConfigActivity.class));
                finish();
                return;
            }
            String apiUrl = ApiConfig.getBaseUrl();

            if (apiUrl == null || (!apiUrl.startsWith("http://") && !apiUrl.startsWith("https://"))) {
                Toast.makeText(this, "Невірний URL сервера API!", Toast.LENGTH_LONG).show();
                return;
            }

            System.out.println("API URL: " + apiUrl);
            ApiConfig.setBaseUrl(apiUrl);
            apiService = ApiClient.getService();
        } else {
            apiService = null;
        }
    }

    public void login(View v) {
        try {

            String workIdStr = workIdInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            boolean isAdminLogin = v.getId() == R.id.login_admin;

            if (workIdStr.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Будь ласка, введіть логін і пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            int employeeWorkId;

            try {
                employeeWorkId = Integer.parseInt(workIdStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Невірний формат Work ID", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!DbConfigManager.isConfigAvailable(this)) {
                handleNoDatabase(isAdminLogin);
                return;
            }

            if (apiService == null) {
                Toast.makeText(this, "Немає підключення до API", Toast.LENGTH_SHORT).show();
                return;
            }

            authenticateUser(employeeWorkId, password, isAdminLogin);
        }
        catch (Exception e) {
            Log.e("LoginActivity", "Помилка у login()", e);
            Toast.makeText(this, "Помилка авторизації", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleNoDatabase(boolean isAdminLogin) {
        if (isAdminLogin) {
            new AlertDialog.Builder(this)
                    .setTitle("Немає підключення до БД")
                    .setMessage("Ви можете або налаштувати підключення, або увійти без нього.")
                    .setPositiveButton("Налаштувати", (dialog, which) ->
                            startActivity(new Intent(this, DatabaseConfigActivity.class)))
                    .setNegativeButton("Ігнорувати", (dialog, which) ->
                            openAdminPanelWithoutDatabase())
                    .show();
        } else {
            Toast.makeText(this, "Немає підключення до БД. Зверніться до адміністратора.", Toast.LENGTH_LONG).show();
        }
    }

    private void authenticateUser(int employeeWorkId, String password, boolean isAdminLogin) {
        apiService.login(new AuthRequest(employeeWorkId, password)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    if (isAdminLogin && authResponse.getRole() != RoleEnum.ADMIN) {
                        Toast.makeText(LoginActivity.this, "Недостатньо прав для входу як адміністратор", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    sharedPreferences.edit()
                            .putLong(Constants.KEY_EMPLOYEE_ID, authResponse.getEmployeeId())
                            .putString("role", authResponse.getRole().name())
                            .putString("firstName", authResponse.getFirstName())
                            .putString("lastName", authResponse.getLastName())
                            .putLong(Constants.KEY_COMPANY_ID, authResponse.getCompanyId())
                            .apply();

                    Log.d("LoginActivity", "employeeId: " + authResponse.getEmployeeId());
                    Log.d("LoginActivity", "role: " + authResponse.getRole().name());
                    Log.d("LoginActivity", "firstName: " + authResponse.getFirstName());
                    Log.d("LoginActivity", "lastName: " + authResponse.getLastName());
                    Log.d("LoginActivity", "company_id: " + authResponse.getCompanyId());

//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));


                    Class<?> targetActivity = isAdminLogin ? AdminHomeActivity.class : UserHomeActivity.class;
                    startActivity(new Intent(LoginActivity.this, targetActivity));
                    finish();


//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.putExtra("isAdmin", authResponse.getRole() == RoleEnum.ADMIN);
//                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Невірний логін або пароль", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Помилка з'єднання", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openAdminPanelWithoutDatabase() {
        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
        finish();
    }

    public void signUpDatabase(View v) {
        startActivity(new Intent(this, DatabaseConfigActivity.class));
    }

        public void forgotPassword(View v) {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }
}

