package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.R;
import com.smartinvent.model.Company;
import com.smartinvent.model.Constants;
import com.smartinvent.network.ApiClient;
import com.smartinvent.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class SignUpCompanyActivity1 extends AppCompatActivity {

    private EditText companyName, companyAddress, companyPhone, companyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_company_page_1);

        companyName = findViewById(R.id.input_company_name);
        companyAddress = findViewById(R.id.input_company_address);
        companyPhone = findViewById(R.id.input_phone_number);
        companyEmail = findViewById(R.id.input_email_address);
    }

    public void saveCompanyData(View v) {
        String name = companyName.getText().toString().trim();
        String address = companyAddress.getText().toString().trim();
        String phone = companyPhone.getText().toString().trim();
        String email = companyEmail.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть всі поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        Company company = new Company(name, address, phone, email);

        Log.d("SignUpCompanyActivity", "Company Data: " + company.toString());

        ApiService apiService = ApiClient.getService();
        apiService.createCompany(company).enqueue(new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {
                if (response.isSuccessful()) {
                    Company createdCompany = response.body();
                    Log.i("SignUpCompanyActivity", "Company registration successful: " + createdCompany);

                    Toast.makeText(SignUpCompanyActivity1.this, "Компанія зареєстрована!", Toast.LENGTH_SHORT).show();

                    Log.d("SignUpCompanyActivity1", "Sending company with ID: " + createdCompany.getCompanyId());

                    Intent intent = new Intent(SignUpCompanyActivity1.this, SignUpCompanyActivity2.class);
                    intent.putExtra(Constants.KEY_COMPANY, createdCompany);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("SignUpCompanyActivity", "Помилка реєстрації компанії: " + errorBody);
                    } catch (IOException e) {
                        Log.e("SignUpCompanyActivity", "Помилка читання помилки сервера", e);
                    }
                    Toast.makeText(SignUpCompanyActivity1.this, "Помилка: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                Toast.makeText(SignUpCompanyActivity1.this, "Проблема з'єднання з сервером", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Помилка підключення", t);
            }
        });
    }

    public void signUpCompanyPage2(View v) {
        saveCompanyData(v);
    }

    public void backToDatabase(View v) {
        startActivity(new Intent(this, DatabaseConfigActivity.class));
        finish();
    }

    public void backToLogin(View v) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}



//package com.smartinvent.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.smartinvent.R;
//import com.smartinvent.config.DbConfigManager;
//import com.smartinvent.model.Company;
//import com.smartinvent.network.ApiClient;
//import com.smartinvent.network.ApiService;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import java.io.IOException;
//
//public class SignUpCompanyActivity1 extends AppCompatActivity {
//
//    private EditText companyName, companyAddress, companyPhone, companyEmail, companyWebsite;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up_company_page_1);
//
//        companyName = findViewById(R.id.input_company_name);
//        companyAddress = findViewById(R.id.input_company_address);
//        companyPhone = findViewById(R.id.input_phone_number);
//        companyEmail = findViewById(R.id.input_email_address);
//        companyWebsite = findViewById(R.id.input_website_url);
//    }
//
//    public void saveCompanyData(View v) {
//        Company company = new Company(
//                companyName.getText().toString(),
//                companyAddress.getText().toString(),
//                companyPhone.getText().toString(),
//                companyEmail.getText().toString()
//        );
//        Log.d("SignUpCompanyActivity", "Company Data: " + company.toString());
//
//
//        ApiService apiService = ApiClient.getService();
//        apiService.createCompany(company).enqueue(new Callback<Company>() {
//            @Override
//            public void onResponse(Call<Company> call, Response<Company> response) {
//                if (response.isSuccessful()) {
//                    Company createdCompany = response.body();
//                    Log.i("SignUpCompanyActivity", "Company registration successful: " + createdCompany);
//
//                    Toast.makeText(SignUpCompanyActivity1.this, "Компанія зареєстрована!", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(SignUpCompanyActivity1.this, SignUpCompanyActivity2.class);
//                    intent.putExtra("company", createdCompany);  // Company має імплементувати Serializable
//                    startActivity(intent);
//                    finish();
//                }
//
//            } else {
//                    try {
//                        String errorBody = response.errorBody().string();
//                        Log.e("SignUpCompanyActivity", "Помилка реєстрації компанії: " + errorBody);
//                    } catch (IOException e) {
//                        Log.e("SignUpCompanyActivity", "Помилка читання помилки сервера", e);
//                    }
//                    Toast.makeText(SignUpCompanyActivity1.this, "Помилка реєстрації компанії: " + response.code(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<Company> call, Throwable t) {
//                Toast.makeText(SignUpCompanyActivity1.this, "Не вдалося підключитися до сервера", Toast.LENGTH_SHORT).show();
//                Log.e("API_ERROR", "Помилка підключення", t);
//            }
//        });
//    }
//
//    public void signUpCompanyPage2(View v) {
//        saveCompanyData(v);
//    }
//
//    public void backToDatabase(View v) {
//        startActivity(new Intent(this, DatabaseConfigActivity.class));
//        finish();
//    }
//
//    public void backToLogin(View v) {
//        startActivity(new Intent(this, LoginActivity.class));
//        finish();
//    }
//}
//
