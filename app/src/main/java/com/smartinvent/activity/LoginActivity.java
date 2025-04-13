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
import com.smartinvent.network.ApiClient;
import com.smartinvent.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;
import io.sentry.Sentry;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText workIdInput, passwordInput;
    private ApiService apiService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sentry.captureException(new Exception("Test exception from SmartInvent"));

        findViewById(android.R.id.content).getViewTreeObserver().addOnGlobalLayoutListener(() -> {
      try {
        throw new Exception("This app uses Sentry! :)");
      } catch (Exception e) {
        Sentry.captureException(e);
      }
    });

        setContentView(R.layout.activity_login);

        Timber.d("Initializing login screen");

        workIdInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        updateApiClient();
    }

    private void updateApiClient() {

        Log.d(TAG, "updateApiClient: Checking database config");


        if (DbConfigManager.isConfigAvailable(this)) {
            DatabaseConfig config = DbConfigManager.loadConfig(this);
            if (config == null) {
                Toast.makeText(this, "Please configure the database!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, DatabaseConfigActivity.class));
                finish();
                return;
            }
            String apiUrl = ApiConfig.getBaseUrl();

            if (apiUrl == null || (!apiUrl.startsWith("http://") && !apiUrl.startsWith("https://"))) {
                Timber.e("Invalid API URL: %s", apiUrl);
                Toast.makeText(this, "Invalid API server URL!", Toast.LENGTH_LONG).show();
                return;
            }

            Timber.d("Using API URL: %s", apiUrl);
            ApiConfig.setBaseUrl(apiUrl);
            apiService = ApiClient.getService();
        } else {
            Timber.w("No database config available");
            apiService = null;
        }
    }

    public void login(View v) {
        try {

            String workIdStr = workIdInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            boolean isAdminLogin = v.getId() == R.id.login_admin;

            Timber.d("Attempting login, isAdmin = %s", isAdminLogin);

            if (workIdStr.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter login and password", Toast.LENGTH_SHORT).show();
                Timber.w("Empty login or password");
                return;
            }

            int employeeWorkId;

            try {
                employeeWorkId = Integer.parseInt(workIdStr);
            } catch (NumberFormatException e) {
                Log.e(TAG, "login: Invalid Work ID format", e);
                Toast.makeText(this, "Invalid Work ID format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!DbConfigManager.isConfigAvailable(this)) {
                Log.w(TAG, "login: Database config is missing");
                handleNoDatabase(isAdminLogin);
                return;
            }

            if (apiService == null) {
                Log.e(TAG, "login: API service is null");
                Toast.makeText(this, "No connection to API", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "login: Sending authentication request");
            authenticateUser(employeeWorkId, password, isAdminLogin);
        }
        catch (Exception e) {
            Log.e(TAG, "login: Exception during login", e);
            Toast.makeText(this, "Authentication error", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleNoDatabase(boolean isAdminLogin) {
        Log.d(TAG, "handleNoDatabase: isAdmin = " + isAdminLogin);

        if (isAdminLogin) {
            new AlertDialog.Builder(this)
                    .setTitle("No database connection")
                    .setMessage("You can either configure the connection or log in without it.")
                    .setPositiveButton("Configure", (dialog, which) -> {
                        Log.d(TAG, "handleNoDatabase: Redirecting to config");
                        startActivity(new Intent(this, DatabaseConfigActivity.class));
                    })
                    .setNegativeButton("Ignore", (dialog, which) -> {
                        Log.d(TAG, "handleNoDatabase: Opening admin panel without DB");
                        openAdminPanelWithoutDatabase();
                    })
                    .show();
        } else {
            Log.w(TAG, "handleNoDatabase: Non-admin attempted login without DB config");
            Toast.makeText(this, "No DB connection. Contact the administrator.", Toast.LENGTH_LONG).show();
        }
    }

    private void authenticateUser(int employeeWorkId, String password, boolean isAdminLogin) {
        apiService.login(new AuthRequest(employeeWorkId, password)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "authenticateUser: Login successful. Role: " + authResponse.getRole());

                    if (isAdminLogin && !"ADMIN".equals(authResponse.getRole())) {
                        Log.w(TAG, "authenticateUser: Insufficient privileges for admin login");
                        Toast.makeText(LoginActivity.this, "Insufficient rights for admin login", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    sharedPreferences.edit()
                            .putLong("employeeId", authResponse.getEmployeeId())
                            .putString("role", authResponse.getRole())
                            .putString("firstName", authResponse.getFirstName())
                            .putString("lastName", authResponse.getLastName())
                            .apply();

                    Class<?> targetActivity = isAdminLogin ? AdminHomeActivity.class : UserHomeActivity.class;
                    Log.d(TAG, "authenticateUser: Redirecting to " + targetActivity.getSimpleName());
                    startActivity(new Intent(LoginActivity.this, targetActivity));
                    finish();
                } else {
                    Log.w(TAG, "authenticateUser: Invalid login credentials");
                    Toast.makeText(LoginActivity.this, "Invalid login or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "authenticateUser: API call failed", t);
                Toast.makeText(LoginActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openAdminPanelWithoutDatabase() {
        Log.d(TAG, "openAdminPanelWithoutDatabase: Redirecting to AdminHomeActivity");
        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
        finish();
    }

    public void signUpDatabase(View v) {
        Log.d(TAG, "signUpDatabase: Opening DatabaseConfigActivity");
        startActivity(new Intent(this, DatabaseConfigActivity.class));
    }

        public void forgotPassword(View v) {
            Log.d(TAG, "forgotPassword: Opening ForgotPasswordActivity");
            startActivity(new Intent(this, ForgotPasswordActivity.class));
    }
}