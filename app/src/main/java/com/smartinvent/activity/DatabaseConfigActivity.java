package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.R;
import com.smartinvent.config.DatabaseConfig;
import com.smartinvent.config.DbConfigManager;
import com.smartinvent.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatabaseConfigActivity extends AppCompatActivity {
    private EditText urlInput, userInput, passwordInput;
    private Button testButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_config);

        urlInput = findViewById(R.id.urlInput);
        userInput = findViewById(R.id.userInput);
        passwordInput = findViewById(R.id.passwordInput);
        testButton = findViewById(R.id.testButton);
        saveButton = findViewById(R.id.saveButton);

        testButton.setOnClickListener(v -> testConnection());
        saveButton.setOnClickListener(v -> saveConfig());
    }

    private void testConnection() {
        DatabaseConfig config = new DatabaseConfig(
                urlInput.getText().toString(),
                userInput.getText().toString(),
                passwordInput.getText().toString()
        );

        ApiClient.getService().testDbConnection(config).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DatabaseConfigActivity.this, "Підключення успішне", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DatabaseConfigActivity.this, "Помилка підключення", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DatabaseConfigActivity.this, "Не вдалося підключитися", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveConfig() {
        DatabaseConfig config = new DatabaseConfig(
                urlInput.getText().toString(),
                userInput.getText().toString(),
                passwordInput.getText().toString()
        );

        DbConfigManager.saveConfig(this, config);
        Toast.makeText(this, "Налаштування збережені", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, AdminHomeActivity.class));
    }
}




//package com.smartinvent.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.*;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.smartinvent.R;
//import com.smartinvent.viewmodel.DatabaseConfigViewModel;
//
//public class DatabaseConfigActivity extends AppCompatActivity {
//
//    private EditText hostInput, portInput, databaseInput, userInput, passwordInput, urlInput;
//    private Button saveButton;
//    private DatabaseConfigViewModel viewModel;
//    private Spinner dbTypeSpinner;
//    private RadioGroup connectionTypeGroup;
//    private RadioButton radioManual, radioUrl;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_database_config);
//
//        connectionTypeGroup = findViewById(R.id.connectionTypeGroup);
//        radioManual = findViewById(R.id.radio_manual);
//        radioUrl = findViewById(R.id.radio_url);
//
//        dbTypeSpinner = findViewById(R.id.dbTypeSpinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.db_types, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dbTypeSpinner.setAdapter(adapter);
//
//        hostInput = findViewById(R.id.hostInput);
//        portInput = findViewById(R.id.portInput);
//        databaseInput = findViewById(R.id.databaseInput);
//        userInput = findViewById(R.id.userInput);
//        passwordInput = findViewById(R.id.passwordInput);
//        urlInput = findViewById(R.id.urlInput);
//
//        saveButton = findViewById(R.id.saveButton);
//        viewModel = new ViewModelProvider(this).get(DatabaseConfigViewModel.class);
//
//        connectionTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            toggleManualInput(checkedId == R.id.radio_manual);
//        });
//
//        saveButton.setOnClickListener(v -> saveDatabaseConfig());
//    }
//
//    private void toggleManualInput(boolean manual) {
//        int visibility = manual ? View.VISIBLE : View.GONE;
//        dbTypeSpinner.setVisibility(visibility);
//        hostInput.setVisibility(visibility);
//        portInput.setVisibility(visibility);
//        databaseInput.setVisibility(visibility);
//        userInput.setVisibility(visibility);
//        passwordInput.setVisibility(visibility);
//        urlInput.setVisibility(manual ? View.GONE : View.VISIBLE);
//    }
//
//    private void saveDatabaseConfig() {
//        boolean isUrl = radioUrl.isChecked();
//
//        if (isUrl) {
//            String url = urlInput.getText().toString().trim();
//            if (url.isEmpty()) {
//                Toast.makeText(this, "Введіть URL підключення!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            viewModel.saveDatabaseUrl(url, success -> {
//                if (success) {
//                    Toast.makeText(this, "Конфігурація збережена!", Toast.LENGTH_SHORT).show();
//                    goToAdminHome();
//                } else {
//                    Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            String dbType = dbTypeSpinner.getSelectedItem().toString();
//            String host = hostInput.getText().toString().trim();
//            String port = portInput.getText().toString().trim();
//            String database = databaseInput.getText().toString().trim();
//            String user = userInput.getText().toString().trim();
//            String password = passwordInput.getText().toString().trim();
//
//            if (host.isEmpty() || port.isEmpty() || database.isEmpty() || user.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Заповніть всі поля!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            viewModel.saveDatabaseConfig(dbType, host, port, database, user, password, success -> {
//                if (success) {
//                    Toast.makeText(this, "Конфігурація збережена!", Toast.LENGTH_SHORT).show();
//                    goToAdminHome();
//                } else {
//                    Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    private void goToAdminHome() {
//        startActivity(new Intent(this, AdminHomeActivity.class));
//        finish();
//    }
//
//        public void backToSignUpCompanyPage2(View v){
//        Intent intent = new Intent(this, SignUpCompanyActivity2.class);
//        startActivity(intent);
//    }
//}


//package com.smartinvent.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.*;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.smartinvent.R;
//import com.smartinvent.network.DatabaseConfigRequest;
//import com.smartinvent.viewmodel.DatabaseConfigViewModel;
//
//public class DatabaseConfigActivity extends AppCompatActivity {
//
//    private EditText hostInput, portInput, databaseInput, userInput, passwordInput, urlInput;
//    private Button saveButton;
//    private DatabaseConfigViewModel viewModel;
//    private Spinner dbTypeSpinner;
//    private RadioGroup connectionTypeGroup;
//    private RadioButton radioManual, radioUrl;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_database_config);
//
//        connectionTypeGroup = findViewById(R.id.connectionTypeGroup);
//        radioManual = findViewById(R.id.radio_manual);
//        radioUrl = findViewById(R.id.radio_url);
//
//        dbTypeSpinner = findViewById(R.id.dbTypeSpinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.db_types, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dbTypeSpinner.setAdapter(adapter);
//
//        hostInput = findViewById(R.id.hostInput);
//        portInput = findViewById(R.id.portInput);
//        databaseInput = findViewById(R.id.databaseInput);
//        userInput = findViewById(R.id.userInput);
//        passwordInput = findViewById(R.id.passwordInput);
//        urlInput = findViewById(R.id.urlInput);
//
//        saveButton = findViewById(R.id.saveButton);
//        viewModel = new ViewModelProvider(this).get(DatabaseConfigViewModel.class);
//
//        // Обробка перемикання між "ручним введенням" та "введенням URL"
//        connectionTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            if (checkedId == R.id.radio_manual) {
//                toggleManualInput(true);
//            } else {
//                toggleManualInput(false);
//            }
//        });
//
//        saveButton.setOnClickListener(v -> saveDatabaseConfig());
//    }
//
//    private void toggleManualInput(boolean manual) {
//        int visibility = manual ? View.VISIBLE : View.GONE;
//        dbTypeSpinner.setVisibility(visibility);
//        hostInput.setVisibility(visibility);
//        portInput.setVisibility(visibility);
//        databaseInput.setVisibility(visibility);
//        userInput.setVisibility(visibility);
//        passwordInput.setVisibility(visibility);
//        urlInput.setVisibility(manual ? View.GONE : View.VISIBLE);
//    }
//
//

//
//    private void saveDatabaseConfig() {
//        String dbType = dbTypeSpinner.getSelectedItem().toString();
//        String host = hostInput.getText().toString().trim();
//        String port = portInput.getText().toString().trim();
//        String database = databaseInput.getText().toString().trim();
//        String user = userInput.getText().toString().trim();
//        String password = passwordInput.getText().toString().trim();
//        String url = urlInput.getText().toString().trim();
//
//        if (radioUrl.isChecked()) {
//            if (url.isEmpty()) {
//                Toast.makeText(this, "Введіть URL підключення!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        } else {
//            if (host.isEmpty() || port.isEmpty() || database.isEmpty() || user.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Заповніть всі поля!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            DatabaseConfigRequest request = new DatabaseConfigRequest(dbType, host, port, database, user, password);
//            viewModel.saveDatabaseConfig(request, success -> {
//                if (success) {
//                    Toast.makeText(this, "Конфігурація збережена!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(this, AdminHomeActivity.class));
//                    finish();
//                } else {
//                    Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//}
