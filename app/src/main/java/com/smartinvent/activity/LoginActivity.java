package com.smartinvent.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private ApiService apiService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
//        updateApiClient(); // Оновлюємо API клієнт при старті
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


            ApiConfig.setBaseUrl(DbConfigManager.loadConfig(this).getUrl());
            apiService = ApiClient.getService();
        } else {
            apiService = null;
        }
    }

    public void login(View v) {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        boolean isAdminLogin = v.getId() == R.id.login_admin;

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Будь ласка, введіть логін і пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!DbConfigManager.isConfigAvailable(this)) {
            handleNoDatabase(isAdminLogin);
            return;
        }

        authenticateUser(email, password, isAdminLogin);
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

    private void authenticateUser(String email, String password, boolean isAdminLogin) {
        apiService.login(new AuthRequest(email, password)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    if (isAdminLogin && !"ADMIN".equals(authResponse.getRole())) {
                        Toast.makeText(LoginActivity.this, "Недостатньо прав для входу як адміністратор", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    sharedPreferences.edit()
                            .putLong("employeeId", authResponse.getEmployeeId())
                            .putString("role", authResponse.getRole())
                            .putString("firstName", authResponse.getFirstName())
                            .putString("lastName", authResponse.getLastName())
                            .apply();

                    Class<?> targetActivity = isAdminLogin ? AdminHomeActivity.class : UserHomeActivity.class;
                    startActivity(new Intent(LoginActivity.this, targetActivity));
                    finish();
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



//package com.smartinvent.activity;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.smartinvent.R;
//import com.smartinvent.config.ApiConfig;
//import com.smartinvent.config.DatabaseConfig;
//import com.smartinvent.config.DbConfigManager;
//import com.smartinvent.model.AuthRequest;
//import com.smartinvent.model.AuthResponse;
//import com.smartinvent.network.ApiService;
//import com.smartinvent.network.ApiClient;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private EditText emailInput, passwordInput;
//    private ApiService apiService;
//    private SharedPreferences sharedPreferences;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        emailInput = findViewById(R.id.username);
//        passwordInput = findViewById(R.id.password);
//
//        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
//        updateApiClient(); // Оновлюємо API клієнт при старті
//    }
//
//    private void updateApiClient() {
//        DatabaseConfig dbConfig = DbConfigManager.loadConfig(this);
//        if (dbConfig != null) {
//            ApiConfig.setBaseUrl(dbConfig.getUrl());
//            apiService = ApiClient.getService();
//        }
//    }
//
//    public void login(View v) {
//        String email = emailInput.getText().toString().trim();
//        String password = passwordInput.getText().toString().trim();
//        boolean isAdminLogin = v.getId() == R.id.login_admin;
//
//        // Перевірка на заповненість полів
//        if (email.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "Будь ласка, введіть логін і пароль", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Перевірка налаштувань підключення до БД перед входом
//        if (!DbConfigManager.isConfigAvailable(this)) {
//            Toast.makeText(this, "Немає налаштувань підключення до БД. Налаштуйте його спочатку!", Toast.LENGTH_LONG).show();
//            startActivity(new Intent(this, DatabaseConfigActivity.class));
//            return;
//        }
//
//        AuthRequest request = new AuthRequest(email, password);
//
//        apiService.login(request).enqueue(new Callback<AuthResponse>() {
//            @Override
//            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    AuthResponse authResponse = response.body();
//
//                    // Перевірка доступу: якщо це вхід для адміністратора, а роль ≠ "ADMIN"
//                    if (isAdminLogin && !"ADMIN".equals(authResponse.getRole())) {
//                        Toast.makeText(LoginActivity.this, "Недостатньо прав для входу як адміністратор", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    // Збереження даних користувача
//                    sharedPreferences.edit()
//                            .putLong("employeeId", authResponse.getEmployeeId())
//                            .putString("role", authResponse.getRole())
//                            .putString("firstName", authResponse.getFirstName())
//                            .putString("lastName", authResponse.getLastName())
//                            .apply();
//
//                    // Перехід на відповідну сторінку
//                    Class<?> targetActivity = isAdminLogin ? AdminHomeActivity.class : UserHomeActivity.class;
//                    startActivity(new Intent(LoginActivity.this, targetActivity));
//                    finish();
//                } else {
//                    Toast.makeText(LoginActivity.this, "Невірний логін або пароль", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AuthResponse> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "Помилка з'єднання", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void signUpDatabase(View v) {
//        startActivity(new Intent(this, DatabaseConfigActivity.class));
//    }
//
//    public void forgotPassword(View v) {
//        startActivity(new Intent(this, ForgotPasswordActivity.class));
//    }
//}



//package com.smartinvent.activity;
//
//
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//
//import android.widget.Toast;
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import com.smartinvent.R;
//import com.smartinvent.config.ApiConfig;
//import com.smartinvent.config.DatabaseConfig;
//import com.smartinvent.config.DbConfigManager;
//import com.smartinvent.model.AuthRequest;
//import com.smartinvent.model.AuthResponse;
//import com.smartinvent.network.ApiService;
//import com.smartinvent.network.ApiClient;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import java.util.Locale;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private EditText emailInput, passwordInput;
//    private ApiService apiService;
//    private SharedPreferences sharedPreferences;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        emailInput = findViewById(R.id.username);
//        passwordInput = findViewById(R.id.password);
//
//        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
//
//        // Перевірка підключення до БД
//        if (!DbConfigManager.isConfigAvailable(this)) {
//            Toast.makeText(this, "Немає налаштувань підключення до БД. Налаштуйте його спочатку!", Toast.LENGTH_LONG).show();
//            startActivity(new Intent(this, DatabaseConfigActivity.class));
//            finish();
//            return;
//        }
//
//        // Оновлюємо API клієнт
//        updateApiClient();
//    }
//
//    private void updateApiClient() {
//        DatabaseConfig dbConfig = DbConfigManager.loadConfig(this);
//        if (dbConfig != null) {
//            ApiConfig.setBaseUrl(dbConfig.getUrl());
//            apiService = ApiClient.getService();
//        }
//    }
//
//
//    public void login(View v) {
//        String email = emailInput.getText().toString().trim();
//        String password = passwordInput.getText().toString().trim();
//
//        apiService.login(new AuthRequest(email, password)).enqueue(new Callback<AuthResponse>() {
//            @Override
//            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    AuthResponse authResponse = response.body();
//
//                    sharedPreferences.edit()
//                            .putLong("employeeId", authResponse.getEmployeeId())
//                            .putString("role", authResponse.getRole())
//                            .apply();
//
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
//                } else {
//                    Toast.makeText(LoginActivity.this, "Невірний логін або пароль", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AuthResponse> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "Помилка з'єднання", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void signUpDatabase(View v){
//        Intent intent = new Intent(this, DatabaseConfigActivity.class);
//        startActivity(intent);
//    }
//
//    public void signUpCompanyPage1(View v){
//        Intent intent = new Intent(this, SignUpCompanyActivity1.class);
//        startActivity(intent);
//    }
//
//    public void forgotPassword(View v){
//        Intent intent = new Intent(this, ForgotPasswordActivity.class);
//        startActivity(intent);
//    }
//}
//
//
//
//
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_login);
////
////        emailInput = findViewById(R.id.username);
////        passwordInput = findViewById(R.id.password);
////
////        apiService = ApiClient.getClient().create(ApiService.class);
////        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
////    }
//
//
////    public void login(View v) {
////        String email = emailInput.getText().toString().trim();
////        String password = passwordInput.getText().toString().trim();
////
////        apiService.login(new AuthRequest(email, password)).enqueue(new Callback<AuthResponse>() {
////            @Override
////            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
////                if (response.isSuccessful() && response.body() != null) {
////                    AuthResponse authResponse = response.body();
////
////                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
////                    sharedPreferences.edit()
////                            .putLong("employeeId", authResponse.getEmployeeId())
////                            .putString("role", authResponse.getRole())
////                            .apply();
////
////                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
////                    startActivity(intent);
////                    finish();
////                } else {
////                    Toast.makeText(LoginActivity.this, "Невірний логін або пароль", Toast.LENGTH_SHORT).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<AuthResponse> call, Throwable t) {
////                Toast.makeText(LoginActivity.this, "Помилка з'єднання", Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
//
////    public void login(View v) {
////        String email = emailInput.getText().toString().trim();
////        String password = passwordInput.getText().toString().trim();
////        boolean isAdminLogin = v.getId() == R.id.login_admin;
////
////        AuthRequest request = new AuthRequest(email, password);
////
////        apiService.login(request).enqueue(new Callback<AuthResponse>() {
////            @Override
////            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
////                if (response.isSuccessful() && response.body() != null) {
////                    AuthResponse authResponse = response.body();
////
////                    if (isAdminLogin && !authResponse.getRole().equals("ADMIN")) {
////                        Toast.makeText(LoginActivity.this, "Недостатньо прав", Toast.LENGTH_SHORT).show();
////                        return;
////                    }
////
////                    // Зберігаємо дані користувача
////                    sharedPreferences.edit()
////                            .putLong("employeeId", authResponse.getEmployeeId())
////                            .putString("role", authResponse.getRole())
////                            .putString("firstName", authResponse.getFirstName())
////                            .putString("lastName", authResponse.getLastName())
////                            .apply();
////
////                    // Переходимо на відповідну сторінку
////                    Intent intent = new Intent(LoginActivity.this,
////                            isAdminLogin ? AdminHomeActivity.class : UserHomeActivity.class);
////                    startActivity(intent);
////                    finish();
////                } else {
////                    Toast.makeText(LoginActivity.this, "Невірний логін або пароль", Toast.LENGTH_SHORT).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<AuthResponse> call, Throwable t) {
////                Toast.makeText(LoginActivity.this, "Помилка з'єднання", Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
//
//
//
////        ImageButton btnEnglish = findViewById(R.id.btn_english);
////        ImageButton btnUkrainian = findViewById(R.id.btn_ukrainian);
////
////        btnEnglish.setOnClickListener(v -> setLocale("en"));
////        btnUkrainian.setOnClickListener(v -> setLocale("uk"));
////
////        loadLocale();
//    //}
//
//
////    public void loginAsUser(View v){
////        Intent intent = new Intent(this, UserHomeActivity.class);
////        startActivity(intent);
////    }
////
////    public void loginAsAdmin(View v){
////        Intent intent = new Intent(this, AdminHomeActivity.class);
////        startActivity(intent);
////    }
//
//
//
//
//
////
////import android.content.Intent;
////import android.os.Bundle;
////import android.view.View;
////import androidx.activity.EdgeToEdge;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.core.graphics.Insets;
////import androidx.core.view.ViewCompat;
////import androidx.core.view.WindowInsetsCompat;
////import com.smartinvent.R;
////
////public class LoginActivity extends AppCompatActivity {
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        EdgeToEdge.enable(this);
////
////        setContentView(R.layout.activity_login);
////
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
////            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
////            return insets;
////        });
////    }
////
//////        ImageButton btnEnglish = findViewById(R.id.btn_english);
//////        ImageButton btnUkrainian = findViewById(R.id.btn_ukrainian);
//////
//////        btnEnglish.setOnClickListener(v -> setLocale("en"));
//////        btnUkrainian.setOnClickListener(v -> setLocale("uk"));
//////
//////        loadLocale();
//////    }
//////
//////    private void setLocale(String lang) {
//////        Locale locale = new Locale(lang);
//////        Locale.setDefault(locale);
//////        Resources res = getResources();
//////        Configuration config = new Configuration(res.getConfiguration());
//////        config.setLocale(locale);
//////        res.updateConfiguration(config, res.getDisplayMetrics());
//////
//////        // Save the selected language in SharedPreferences
//////        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
//////        editor.putString("My_Lang", lang);
//////        editor.apply();
//////
//////        // Reload the activity to apply the new language
//////        recreate();
//////    }
//////
//////    private void loadLocale() {
//////        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
//////        String language = prefs.getString("My_Lang", "");
//////        setLocale(language);
//////    }
//////
////
////
////    public void loginAsUser(View v){
////        Intent intent = new Intent(this, UserHomeActivity.class);
////        startActivity(intent);
////    }
////
////    public void loginAsAdmin(View v){
////        Intent intent = new Intent(this, AdminHomeActivity.class);
////        startActivity(intent);
////    }
////
////    public void signUpCompanyPage1(View v){
////        Intent intent = new Intent(this, SignUpCompanyActivity1.class);
////        startActivity(intent);
////    }
////
////    public void forgotPassword(View v){
////        Intent intent = new Intent(this, ForgotPasswordActivity.class);
////        startActivity(intent);
////    }
////
////
////
////
////}