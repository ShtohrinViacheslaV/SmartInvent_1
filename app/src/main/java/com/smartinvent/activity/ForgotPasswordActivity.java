package com.smartinvent.activity;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.R;
import com.smartinvent.model.ForgotPasswordRequest;
import com.smartinvent.network.ApiClient;
import com.smartinvent.network.ApiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editEmployeeWorkId, editPhone, editEmail;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editEmployeeWorkId = findViewById(R.id.editEmployeeWorkId);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> sendForgotPasswordRequest());
    }

    private void sendForgotPasswordRequest() {
        String employeeWorkId = editEmployeeWorkId.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if (employeeWorkId.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Усі поля обов'язкові", Toast.LENGTH_SHORT).show();
            return;
        }

        ForgotPasswordRequest request = new ForgotPasswordRequest(employeeWorkId, phone, email);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.forgotPassword(request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Новий пароль надіслано на пошту", Toast.LENGTH_LONG).show();
                    finish();
                } else if (response.code() == 404) {
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Неправильні дані. Для оновлення паролю зверніться до адміністратора.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Сталася помилка. Спробуйте пізніше.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Помилка підключення: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}