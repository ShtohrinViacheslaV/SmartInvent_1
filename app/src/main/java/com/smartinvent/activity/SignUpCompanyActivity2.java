package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.R;
import com.smartinvent.model.Employee;
import com.smartinvent.network.ApiClient;
import com.smartinvent.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpCompanyActivity2 extends AppCompatActivity {

    private EditText adminWorkId, adminFirstName, adminLastName, adminEmail, adminPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_company_page_2);

        adminWorkId = findViewById(R.id.input_admin_worker_id);
        adminFirstName = findViewById(R.id.input_admin_first_name);
        adminLastName = findViewById(R.id.input_admin_last_name);
        adminEmail = findViewById(R.id.input_admin_email);
        adminPassword = findViewById(R.id.input_admin_password);
    }

    public void saveAdminData(View v) {
        final Employee admin = new Employee(
                adminFirstName.getText().toString(),
                adminLastName.getText().toString(),
                adminEmail.getText().toString(),
                adminWorkId.getText().toString(),
                adminPassword.getText().toString(),
                "ADMIN"
        );

        admin.setEmployeeWorkId(adminWorkId.getText().toString());
        admin.setPasswordHash(adminPassword.getText().toString());

        final ApiService apiService = ApiClient.getService();
        apiService.createEmployee(admin).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpCompanyActivity2.this, "Адміністратор зареєстрований!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpCompanyActivity2.this, AdminHomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignUpCompanyActivity2.this, "Помилка реєстрації адміністратора", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(SignUpCompanyActivity2.this, "Не вдалося підключитися до сервера", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Помилка підключення", t);
            }
        });
    }


    public void backToSignUpCompanyActivity1(View v) {
        startActivity(new Intent(this, SignUpCompanyActivity1.class));
        finish();
    }

    public void databaseActivity(View v) {
        saveAdminData(v);
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
//public class SignUpCompanyActivity2 extends AppCompatActivity {
//
//    private EditText adminWorkId, adminFirstName, adminLastName, adminEmail, adminPassword;
//    private static final String FILE_NAME = "admin_data.json";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up_company_page_2);
//
//        adminWorkId = findViewById(R.id.input_admin_worker_id);
//        adminFirstName = findViewById(R.id.input_admin_first_name);
//        adminLastName = findViewById(R.id.input_admin_last_name);
//        adminEmail = findViewById(R.id.input_admin_email);
//        adminPassword = findViewById(R.id.input_admin_password);
//
//        showInfoDialog();
//    }
//
//    private void showInfoDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("Sign Up administrator for your company")
//                .setMessage("Please provide the following information for the primary administrator.")
//                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
//                .show();
//    }
//
//    public void saveAdminData(View v) {
//        try {
//            JSONObject adminData = new JSONObject();
//            adminData.put("adminWorkId", adminWorkId.getText().toString());
//            adminData.put("adminFirstName", adminFirstName.getText().toString());
//            adminData.put("adminLastName", adminLastName.getText().toString());
//            adminData.put("adminEmail", adminEmail.getText().toString());
//            adminData.put("adminPassword", adminPassword.getText().toString());
//
//            File file = new File(getFilesDir(), FILE_NAME);
//            try (FileWriter writer = new FileWriter(file)) {
//                writer.write(adminData.toString());
//            }
//
//            Toast.makeText(this, "Admin data saved", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, AdminHomeActivity.class));
//            finish();
//
//        } catch (Exception e) {
//            Log.e("SignUpCompanyActivity2", "Error saving JSON data", e);
//            Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void backToSignUpCompanyActivity1(View v) {
//        startActivity(new Intent(this, SignUpCompanyActivity1.class));
//        finish();
//    }
//
//    public void databaseActivity(View v) {
//        saveAdminData(v);
//    }
//}


//package com.smartinvent.activity;
//
//
//
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import com.smartinvent.R;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class SignUpCompanyActivity2 extends AppCompatActivity {
//
//
//    private EditText adminWorkId, adminFirstName, adminLastName, adminEmail, adminPassword;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_sign_up_company_page_2);
//
//        adminWorkId = findViewById(R.id.input_admin_worker_id);
//        adminFirstName = findViewById(R.id.input_admin_first_name);
//        adminLastName = findViewById(R.id.input_admin_last_name);
//        adminEmail = findViewById(R.id.input_admin_email);
//        adminPassword = findViewById(R.id.input_admin_password);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        showInfoDialog();
//    }
//
//    private void showInfoDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Sign Up administrator for your company")
//                .setMessage(    "Great! Now that we've gathered the essential details about your company, it's time to set up the administrative account. The administrator will have full access to manage the organization's inventory, users, and settings within the application. Please provide the following information to establish the primary administrator for your company.")
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
//
//    public void saveAdminData(View v) {
//        String data = adminWorkId.getText().toString() + "," +
//                adminFirstName.getText().toString() + "," +
//                adminLastName.getText().toString() + "," +
//                adminEmail.getText().toString() + "," +
//                adminPassword.getText().toString();
//
//        try (FileOutputStream fos = openFileOutput("files/admin_data.xml", MODE_PRIVATE)) {
//            fos.write(data.getBytes());
//            Toast.makeText(this, "Admin data saved", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, DatabaseConfigActivity.class));
//        } catch (IOException e) {
//            Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
//        }
//        System.out.println("saveAdminData");
//
//    }
//
//
//
//    public void backToSignUpCompanyActivity1(View v){
//        Intent intent = new Intent(this, SignUpCompanyActivity1.class);
//        startActivity(intent);
//    }
//
////    public void firstLoginAsAdmin(View v){
////        Intent intent = new Intent(this, AdminHomeActivity.class);
////        startActivity(intent);
////    }
//
//
//    public void databaseActivity(View v){
//        saveAdminData(v);
//        Intent intent = new Intent(this, DatabaseConfigActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//
//
//
//}
