package com.smartinvent.activity;



import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.smartinvent.R;
import com.smartinvent.model.AuthRequest;
import com.smartinvent.model.AuthResponse;
import com.smartinvent.network.ApiService;
import com.smartinvent.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Locale;

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

        apiService = ApiClient.getClient().create(ApiService.class);
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//
//        setContentView(R.layout.activity_login);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });


    public void login(View v) {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        apiService.login(new AuthRequest(email, password)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    sharedPreferences.edit()
                            .putLong("employeeId", authResponse.getEmployeeId())
                            .putString("role", authResponse.getRole())
                            .apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
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

//    public void login(View v) {
//        String email = emailInput.getText().toString().trim();
//        String password = passwordInput.getText().toString().trim();
//        boolean isAdminLogin = v.getId() == R.id.login_admin;
//
//        AuthRequest request = new AuthRequest(email, password);
//
//        apiService.login(request).enqueue(new Callback<AuthResponse>() {
//            @Override
//            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    AuthResponse authResponse = response.body();
//
//                    if (isAdminLogin && !authResponse.getRole().equals("ADMIN")) {
//                        Toast.makeText(LoginActivity.this, "Недостатньо прав", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    // Зберігаємо дані користувача
//                    sharedPreferences.edit()
//                            .putLong("employeeId", authResponse.getEmployeeId())
//                            .putString("role", authResponse.getRole())
//                            .putString("firstName", authResponse.getFirstName())
//                            .putString("lastName", authResponse.getLastName())
//                            .apply();
//
//                    // Переходимо на відповідну сторінку
//                    Intent intent = new Intent(LoginActivity.this,
//                            isAdminLogin ? AdminHomeActivity.class : UserHomeActivity.class);
//                    startActivity(intent);
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



//        ImageButton btnEnglish = findViewById(R.id.btn_english);
//        ImageButton btnUkrainian = findViewById(R.id.btn_ukrainian);
//
//        btnEnglish.setOnClickListener(v -> setLocale("en"));
//        btnUkrainian.setOnClickListener(v -> setLocale("uk"));
//
//        loadLocale();
    //}


//    public void loginAsUser(View v){
//        Intent intent = new Intent(this, UserHomeActivity.class);
//        startActivity(intent);
//    }
//
//    public void loginAsAdmin(View v){
//        Intent intent = new Intent(this, AdminHomeActivity.class);
//        startActivity(intent);
//    }

    public void signUpDatabase(View v){
        Intent intent = new Intent(this, DatabaseConfigActivity.class);
        startActivity(intent);
    }

    public void signUpCompanyPage1(View v){
        Intent intent = new Intent(this, SignUpCompanyActivity1.class);
        startActivity(intent);
    }

    public void forgotPassword(View v){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}



//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import com.smartinvent.R;
//
//public class LoginActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//
//        setContentView(R.layout.activity_login);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
////        ImageButton btnEnglish = findViewById(R.id.btn_english);
////        ImageButton btnUkrainian = findViewById(R.id.btn_ukrainian);
////
////        btnEnglish.setOnClickListener(v -> setLocale("en"));
////        btnUkrainian.setOnClickListener(v -> setLocale("uk"));
////
////        loadLocale();
////    }
////
////    private void setLocale(String lang) {
////        Locale locale = new Locale(lang);
////        Locale.setDefault(locale);
////        Resources res = getResources();
////        Configuration config = new Configuration(res.getConfiguration());
////        config.setLocale(locale);
////        res.updateConfiguration(config, res.getDisplayMetrics());
////
////        // Save the selected language in SharedPreferences
////        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
////        editor.putString("My_Lang", lang);
////        editor.apply();
////
////        // Reload the activity to apply the new language
////        recreate();
////    }
////
////    private void loadLocale() {
////        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
////        String language = prefs.getString("My_Lang", "");
////        setLocale(language);
////    }
////
//
//
//    public void loginAsUser(View v){
//        Intent intent = new Intent(this, UserHomeActivity.class);
//        startActivity(intent);
//    }
//
//    public void loginAsAdmin(View v){
//        Intent intent = new Intent(this, AdminHomeActivity.class);
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
//
//
//
//
//}