package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.R;
import com.smartinvent.config.DbConfigManager;
import com.smartinvent.model.Company;
import com.smartinvent.network.ApiClient;
import com.smartinvent.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class SignUpCompanyActivity1 extends AppCompatActivity {

    private EditText companyName, companyAddress, companyPhone, companyEmail, companyWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_company_page_1);

        companyName = findViewById(R.id.input_company_name);
        companyAddress = findViewById(R.id.input_company_address);
        companyPhone = findViewById(R.id.input_phone_number);
        companyEmail = findViewById(R.id.input_email_address);
        companyWebsite = findViewById(R.id.input_website_url);
    }

    public void saveCompanyData(View v) {
        Company company = new Company(
                companyName.getText().toString(),
                companyAddress.getText().toString(),
                companyPhone.getText().toString(),
                companyEmail.getText().toString()
        );
        Log.d("SignUpCompanyActivity", "Company Data: " + company.toString());


        ApiService apiService = ApiClient.getService();
        apiService.createCompany(company).enqueue(new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {
                if (response.isSuccessful()) {
                    Log.i("SignUpCompanyActivity", "Company registration successful: " + response.body());
                    Toast.makeText(SignUpCompanyActivity1.this, "Компанія зареєстрована!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpCompanyActivity1.this, SignUpCompanyActivity2.class));
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("SignUpCompanyActivity", "Помилка реєстрації компанії: " + errorBody);
                    } catch (IOException e) {
                        Log.e("SignUpCompanyActivity", "Помилка читання помилки сервера", e);
                    }
                    Toast.makeText(SignUpCompanyActivity1.this, "Помилка реєстрації компанії: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                Toast.makeText(SignUpCompanyActivity1.this, "Не вдалося підключитися до сервера", Toast.LENGTH_SHORT).show();
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
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import com.smartinvent.R;
//import org.json.JSONObject;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//
//public class SignUpCompanyActivity1 extends AppCompatActivity {
//
//    private EditText companyName, companyAddress, companyPhone, companyEmail, companyWebsite;
//    private static final String FILE_NAME = "company_data.json";
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
//
//        showInfoDialog();
//    }
//
//    private void showInfoDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("Welcome to SmartInventory")
//                .setMessage("Please provide your company details.")
//                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
//                .show();
//    }
//
//    public void saveCompanyData(View v) {
//        try {
//            JSONObject companyData = new JSONObject();
//            companyData.put("companyName", companyName.getText().toString());
//            companyData.put("companyAddress", companyAddress.getText().toString());
//            companyData.put("companyPhone", companyPhone.getText().toString());
//            companyData.put("companyEmail", companyEmail.getText().toString());
//            companyData.put("companyWebsite", companyWebsite.getText().toString());
//
//            File file = new File(getFilesDir(), FILE_NAME);
//            try (FileWriter writer = new FileWriter(file)) {
//                writer.write(companyData.toString());
//            }
//
//            Toast.makeText(this, "Company data saved", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, SignUpCompanyActivity2.class));
//            finish();  // Закриваємо активність
//
//        } catch (Exception e) {
//            Log.e("SignUpCompanyActivity1", "Error saving JSON data", e);
//            Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
//        }
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



//package com.smartinvent.activity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.*;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import com.smartinvent.R;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class SignUpCompanyActivity1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
//
//    private EditText companyName, companyAddress, companyPhone, companyEmail, companyWebsite;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up_company_page_1);
//
//
//        companyName = findViewById(R.id.input_company_name);
//        companyAddress = findViewById(R.id.input_company_address);
//        companyPhone = findViewById(R.id.input_phone_number);
//        companyEmail = findViewById(R.id.input_email_address);
//        companyWebsite = findViewById(R.id.input_website_url);
//
//
//
//        showInfoDialog();
//    }
//
//
//
//    private void showInfoDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Welcome to SmartInventory")
//                .setMessage("Hello! To register your organization in our inventory management application, please provide the following details across several steps. This information will help us create your company profile, establish administrative access, and ensure the application functions effectively for your needs.")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    public void saveCompanyData(View v) {
//        String data = companyName.getText().toString() + "," +
//                companyAddress.getText().toString() + "," +
//                companyPhone.getText().toString() + "," +
//                companyEmail.getText().toString() + "," +
//                companyWebsite.getText().toString();
//
//        try (FileOutputStream fos = openFileOutput("files/company_data.xml", MODE_PRIVATE)) {
//            fos.write(data.getBytes());
//            Toast.makeText(this, "Company data saved", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, SignUpCompanyActivity2.class));
//        } catch (IOException e) {
//            Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
//        }
//        System.out.println("saveCompanyData");
//    }
//
//
//
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        String text = adapterView.getItemAtPosition(i).toString();
//        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
//
//    public void signUpCompanyPage2(View v){
//        saveCompanyData(v);
//
//        Intent intent = new Intent(this, SignUpCompanyActivity2.class);
//        startActivity(intent);
//    }
//
//
//    public void backToLogin(View v){
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//    }
//
//}
