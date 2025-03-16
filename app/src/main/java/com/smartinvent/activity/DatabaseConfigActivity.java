package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.smartinvent.R;
import com.smartinvent.config.DatabaseConfig;
import com.smartinvent.config.DbConfigManager;
import com.smartinvent.network.ApiClient;
import com.smartinvent.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatabaseConfigActivity extends AppCompatActivity {
    private EditText hostInput, portInput, databaseInput, userInput, passwordInput, urlInput;
    private Button testButton, saveButton;
    private RadioGroup connectionTypeGroup;
    private RadioButton radioManual, radioUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("DatabaseConfigActivity onCreate ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_config);

        connectionTypeGroup = findViewById(R.id.connectionTypeGroup);
        radioManual = findViewById(R.id.radio_manual);
        radioUrl = findViewById(R.id.radio_url);
        hostInput = findViewById(R.id.hostInput);
        portInput = findViewById(R.id.portInput);
        databaseInput = findViewById(R.id.databaseInput);
        userInput = findViewById(R.id.userInput);
        passwordInput = findViewById(R.id.passwordInput);
        urlInput = findViewById(R.id.urlInput);
        testButton = findViewById(R.id.testButton);
        saveButton = findViewById(R.id.saveButton);


        DbConfigManager.checkSavedConfig(this);///////////////////////////////////////////////

        // Завантаження збережених даних
        loadSavedConfig();
        DbConfigManager.checkSavedConfig(this);//////////////////////////////////////////////рщщ

        connectionTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_manual) {
                setManualInputVisibility(View.VISIBLE);
            } else {
                setManualInputVisibility(View.GONE);
            }
        });

        testButton.setOnClickListener(v -> testConnection(false));
        saveButton.setOnClickListener(v -> testConnection(true));
    }

    private void setManualInputVisibility(int visibility) {
        System.out.println("DatabaseConfigActivity setManualInputVisibility ");

        hostInput.setVisibility(visibility);
        portInput.setVisibility(visibility);
        databaseInput.setVisibility(visibility);
        userInput.setVisibility(visibility);
        passwordInput.setVisibility(visibility);
        urlInput.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
    }



    private void testConnection(boolean saveAfterSuccess) {
        System.out.println("DatabaseConfigActivity testConnection ");

        DatabaseConfig config = getConfigFromFields();
        if (radioManual.isChecked() &&
                (config.getHost().isEmpty() || config.getPort().isEmpty() || config.getDatabase().isEmpty() ||
                        config.getUsername().isEmpty() || config.getPassword().isEmpty()) ||
                (radioUrl.isChecked() && config.getUrl().isEmpty())) {
            Toast.makeText(this, "Заповніть всі поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.testDbConnection(config);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DatabaseConfigActivity.this, "Підключення успішне!", Toast.LENGTH_SHORT).show();
                    if (saveAfterSuccess) {
                        saveConfig(config);
                    }
                } else {
                    Toast.makeText(DatabaseConfigActivity.this, "Помилка підключення: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DatabaseConfigActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void saveConfig(DatabaseConfig config) {
        System.out.println("DatabaseConfigActivity saveConfig ");
        DbConfigManager.checkSavedConfig(this);/////////////////////////////////////////////////////////////////

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        System.out.println("Перевіряємо таблиці з конфігом: " + config);

        // Перевіряємо, чи є таблиці
        Call<Boolean> call = apiService.checkDatabaseTables(config);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                System.out.println("Отримано відповідь від checkDatabaseTables: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    boolean tablesExist = response.body();
                    System.out.println("Чи існують таблиці: " + tablesExist);

                    if (tablesExist) {
                        System.out.println("✅ База готова до використання.");
                        showTableOptionsDialog(config);
                    } else {
                        System.out.println("⚠️ Проблема з базою, деякі таблиці можуть бути відсутні!");
                        createDatabaseTables(config);
                    }
                } else {
                    System.out.println("Помилка: відповідь порожня або неуспішна");
                    Toast.makeText(DatabaseConfigActivity.this, "Помилка при перевірці БД", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("Помилка запиту: " + t.getMessage());
                Toast.makeText(DatabaseConfigActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void showTableOptionsDialog(DatabaseConfig config) {
        System.out.println("DatabaseConfigActivity showTableOptionsDialog ");

        new AlertDialog.Builder(this)
                .setTitle("База даних вже існує")
                .setMessage("База даних вже містить таблиці. Що ви хочете зробити?")
                .setPositiveButton("Очистити дані", (dialog, which) -> clearDatabase(config))
                .setNegativeButton("Зберегти дані", (dialog, which) -> {
                    DbConfigManager.saveConfig(DatabaseConfigActivity.this, config);
                    Toast.makeText(DatabaseConfigActivity.this, "Налаштування збережені", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DatabaseConfigActivity.this, SignUpCompanyActivity1.class));
                })
                .setNeutralButton("Скасувати", null)
                .show();
    }

    private void clearDatabase(DatabaseConfig config) {
        System.out.println("DatabaseConfigActivity clearDatabase ");

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.clearDatabase(config);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    createDatabaseTables(config);

                } else {
                    Toast.makeText(DatabaseConfigActivity.this, "Не вдалося очистити БД", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DatabaseConfigActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createDatabaseTables(DatabaseConfig config) {
        System.out.println("DatabaseConfigActivity createDatabaseTables ");

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.initializeDatabase(config);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    DbConfigManager.saveConfig(DatabaseConfigActivity.this, config);
                    Toast.makeText(DatabaseConfigActivity.this, "База даних створена", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DatabaseConfigActivity.this, SignUpCompanyActivity1.class));
                } else {
                    Toast.makeText(DatabaseConfigActivity.this, "Помилка при створенні БД", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DatabaseConfigActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadSavedConfig() {
        System.out.println("DatabaseConfigActivity loadSavedConfig ");
        DbConfigManager.checkSavedConfig(this);///////////////////////////////////////////////

        DatabaseConfig savedConfig = DbConfigManager.loadConfig(this);
        if (savedConfig != null) {
            hostInput.setText(savedConfig.getHost());
            portInput.setText(savedConfig.getPort());
            databaseInput.setText(savedConfig.getDatabase());
            userInput.setText(savedConfig.getUsername());
            passwordInput.setText(savedConfig.getPassword());
            urlInput.setText(savedConfig.getUrl());
        }
        DbConfigManager.checkSavedConfig(this);///////////////////////////////////////////////

    }

    private DatabaseConfig getConfigFromFields() {
        System.out.println("DatabaseConfigActivity getConfigFromFields ");

        return new DatabaseConfig(
                hostInput.getText().toString(),
                portInput.getText().toString(),
                databaseInput.getText().toString(),
                userInput.getText().toString(),
                passwordInput.getText().toString(),
                urlInput.getText().toString()
        );
    }
}




//private void saveConfig(DatabaseConfig config) {
//    ApiService apiService = ApiClient.getClient().create(ApiService.class);
//
//    System.out.println("Перевіряємо таблиці з конфігом: " + config);
//
//    // Перевіряємо, чи є таблиці
//    Call<Boolean> call = apiService.checkDatabaseTables(config);
//    call.enqueue(new Callback<Boolean>() {
//        @Override
//        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//            System.out.println("Отримано відповідь від checkDatabaseTables: " + response.code());
//
//            if (response.isSuccessful() && response.body() != null) {
//                boolean tablesExist = response.body();
//                System.out.println("Чи існують таблиці: " + tablesExist);
//
//                // Додаємо перевірку: якщо це перший вхід, не показувати діалогове вікно
//                if (!DbConfigManager.isConfigSaved(DatabaseConfigActivity.this)) {
//                    System.out.println("🔹 Перший вхід, створюємо таблиці...");
//                    createDatabaseTables(config);
//                } else if (tablesExist) {
//                    System.out.println("✅ База готова до використання.");
//                    showTableOptionsDialog(config);
//                } else {
//                    System.out.println("⚠️ Проблема з базою, деякі таблиці можуть бути відсутні!");
//                    createDatabaseTables(config);
//                }
//            } else {
//                System.out.println("Помилка: відповідь порожня або неуспішна");
//                Toast.makeText(DatabaseConfigActivity.this, "Помилка при перевірці БД", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onFailure(Call<Boolean> call, Throwable t) {
//            System.out.println("Помилка запиту: " + t.getMessage());
//            Toast.makeText(DatabaseConfigActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    });
//}


//    private void saveConfig(DatabaseConfig config) {
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//        apiService.initializeDatabase(config).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(DatabaseConfigActivity.this, "Таблиці створені!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(DatabaseConfigActivity.this, "Помилка створення таблиць", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(DatabaseConfigActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }




//    private void testConnection(boolean saveAfterSuccess) {
//        DatabaseConfig config = getConfigFromFields();
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//        Call<Void> call = apiService.testDbConnection(config);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(DatabaseConfigActivity.this, "Підключення успішне!", Toast.LENGTH_SHORT).show();
//
//                    if (saveAfterSuccess) {
//                        saveConfig(config);
//                    }
//                } else {
//                    Toast.makeText(DatabaseConfigActivity.this, "Помилка підключення: " + response.code(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(DatabaseConfigActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                t.printStackTrace();
//            }
//        });
//    }

//    private void saveConfig(DatabaseConfig config) {
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//        Call<Void> call = apiService.initializeDatabase(config); // Запит на створення таблиць
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    DbConfigManager.saveConfig(DatabaseConfigActivity.this, config);
//                    Toast.makeText(DatabaseConfigActivity.this, "Налаштування збережені", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(DatabaseConfigActivity.this, SignUpCompanyActivity1.class));
//                } else {
//                    Toast.makeText(DatabaseConfigActivity.this, "Помилка при ініціалізації БД: " + response.code(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(DatabaseConfigActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                t.printStackTrace();
//            }
//        });
//    }



//package com.smartinvent.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.*;
//import androidx.appcompat.app.AppCompatActivity;
//import com.smartinvent.R;
//import com.smartinvent.config.DatabaseConfig;
//import com.smartinvent.config.DbConfigManager;
//import com.smartinvent.network.ApiClient;
//import com.smartinvent.network.ApiService;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class DatabaseConfigActivity extends AppCompatActivity {
//    private EditText hostInput, portInput, userInput, passwordInput, urlInput;
//    private Button testButton, saveButton;
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
//        hostInput = findViewById(R.id.hostInput);
//        portInput = findViewById(R.id.portInput);
//        userInput = findViewById(R.id.userInput);
//        passwordInput = findViewById(R.id.passwordInput);
//        urlInput = findViewById(R.id.urlInput);
//        testButton = findViewById(R.id.testButton);
//        saveButton = findViewById(R.id.saveButton);
//
//        // Завантаження збережених даних
//        loadSavedConfig();
//
//        connectionTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            if (checkedId == R.id.radio_manual) {
//                setManualInputVisibility(View.VISIBLE);
//            } else {
//                setManualInputVisibility(View.GONE);
//            }
//        });
//
//        testButton.setOnClickListener(v -> testConnection());
//        saveButton.setOnClickListener(v -> saveConfig());
//    }
//
//    private void setManualInputVisibility(int visibility) {
//        hostInput.setVisibility(visibility);
//        portInput.setVisibility(visibility);
//        userInput.setVisibility(visibility);
//        passwordInput.setVisibility(visibility);
//        urlInput.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
//    }
//
//    private void testConnection() {
//        DatabaseConfig config = getConfigFromFields();
//
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//        Call<Void> call = apiService.testDbConnection(config);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(DatabaseConfigActivity.this, "Підключення успішне!", Toast.LENGTH_SHORT).show();
//                    DbConfigManager.saveConfig(DatabaseConfigActivity.this, config);
//                } else {
//                    Toast.makeText(DatabaseConfigActivity.this, "Помилка підключення: " + response.code(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(DatabaseConfigActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                t.printStackTrace();
//            }
//
//        });
//    }
//
//    private void saveConfig() {
//        DatabaseConfig config = getConfigFromFields();
//
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//        Call<Void> call = apiService.testDbConnection(config);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    // ✅ Підключення успішне → зберігаємо та переходимо далі
//                    DbConfigManager.saveConfig(DatabaseConfigActivity.this, config);
//                    Toast.makeText(DatabaseConfigActivity.this, "Налаштування збережені", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(DatabaseConfigActivity.this, SignUpCompanyActivity1.class));
//                } else {
//                    // ❌ Помилка підключення → показуємо помилку
//                    Toast.makeText(DatabaseConfigActivity.this, "Помилка підключення: " + response.code(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                // ❌ Помилка підключення → показуємо повідомлення
//                Toast.makeText(DatabaseConfigActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                t.printStackTrace();
//            }
//        });
//    }
//
//    private void loadSavedConfig() {
//        DatabaseConfig savedConfig = DbConfigManager.loadConfig(this);
//        if (!savedConfig.getHost().isEmpty()) {
//            hostInput.setText(savedConfig.getHost());
//            portInput.setText(savedConfig.getPort());
//            userInput.setText(savedConfig.getUsername());
//            passwordInput.setText(savedConfig.getPassword());
//            urlInput.setText(savedConfig.getUrl());
//        }
//    }
//
//    private DatabaseConfig getConfigFromFields() {
//        return new DatabaseConfig(
//                hostInput.getText().toString(),
//                portInput.getText().toString(),
//                userInput.getText().toString(),
//                passwordInput.getText().toString(),
//                urlInput.getText().toString()
//        );
//    }
//}












//package com.smartinvent.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.*;
//import androidx.appcompat.app.AppCompatActivity;
//import com.smartinvent.R;
//import com.smartinvent.config.DatabaseConfig;
//import com.smartinvent.config.DbConfigManager;
//import com.smartinvent.network.ApiClient;
//import com.smartinvent.network.ApiService;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class DatabaseConfigActivity extends AppCompatActivity {
//    private EditText hostInput, portInput, userInput, passwordInput, urlInput;
//    private Button testButton, saveButton;
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
//        hostInput = findViewById(R.id.hostInput);
//        portInput = findViewById(R.id.portInput);
//        userInput = findViewById(R.id.userInput);
//        passwordInput = findViewById(R.id.passwordInput);
//        urlInput = findViewById(R.id.urlInput);
//        testButton = findViewById(R.id.testButton);
//        saveButton = findViewById(R.id.saveButton);
//
//        connectionTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            if (checkedId == R.id.radio_manual) {
//                setManualInputVisibility(View.VISIBLE);
//            } else {
//                setManualInputVisibility(View.GONE);
//            }
//        });
//
//        testButton.setOnClickListener(v -> testConnection());
//        saveButton.setOnClickListener(v -> saveConfig());
//    }
//
//    private void setManualInputVisibility(int visibility) {
//        hostInput.setVisibility(visibility);
//        portInput.setVisibility(visibility);
//        userInput.setVisibility(visibility);
//        passwordInput.setVisibility(visibility);
//        urlInput.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
//    }
//
//    public void testDbConnection(View view) {
//        DatabaseConfig config = getConfigFromFields();
//
//        ApiService apiService = ApiClient.getClient().create(ApiService.class);
//        Call<Void> call = apiService.testDbConnection(config);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(DatabaseConfigActivity.this, "Підключення успішне!", Toast.LENGTH_SHORT).show();
//
//                    // ✅ Якщо підключення успішне — зберігаємо налаштування
//                    DbConfigManager.saveConfig(DatabaseConfigActivity.this, config);
//                } else {
//                    Toast.makeText(DatabaseConfigActivity.this, "Помилка підключення", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(DatabaseConfigActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//        private void testConnection() {
//        DatabaseConfig config = new DatabaseConfig(
//                hostInput.getText().toString(),
//                portInput.getText().toString(),
//                userInput.getText().toString(),
//                passwordInput.getText().toString(),
//                urlInput.getText().toString()
//        );
//
//        ApiClient.getService().testDbConnection(config).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(DatabaseConfigActivity.this, "Підключення успішне", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(DatabaseConfigActivity.this, "Помилка підключення", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(DatabaseConfigActivity.this, "Не вдалося підключитися", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    private void saveConfig() {
//        DatabaseConfig config = new DatabaseConfig(
//                hostInput.getText().toString(),
//                portInput.getText().toString(),
//                userInput.getText().toString(),
//                passwordInput.getText().toString(),
//                urlInput.getText().toString()
//        );
//
//        DbConfigManager.saveConfig(this, config);
//        Toast.makeText(this, "Налаштування збережені", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, MainActivity.class));
//    }
//}

//package com.smartinvent.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.*;
//import androidx.appcompat.app.AppCompatActivity;
//import com.smartinvent.R;
//import com.smartinvent.config.DatabaseConfig;
//import com.smartinvent.config.DbConfigManager;
//import com.smartinvent.network.ApiClient;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class DatabaseConfigActivity extends AppCompatActivity {
//    private EditText hostInput, portInput, userInput, passwordInput, urlInput;
//    private Button testButton, saveButton;
////    private DatabaseConfigViewModel viewModel;
//    private Spinner dbTypeSpinner;
//    private RadioGroup connectionTypeGroup;
//    private RadioButton radioManual, radioUrl;
//    private DbConfigManager dbConfigManager;
//
//
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
////        dbTypeSpinner = findViewById(R.id.dbTypeSpinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.db_types, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dbTypeSpinner.setAdapter(adapter);
//
//        hostInput = findViewById(R.id.hostInput);
//        portInput = findViewById(R.id.portInput);
//        userInput = findViewById(R.id.userInput);
//        passwordInput = findViewById(R.id.passwordInput);
//        urlInput = findViewById(R.id.urlInput);
//
//
//
//        testButton.setOnClickListener(v -> testConnection());
//        saveButton.setOnClickListener(v -> saveConfig());
//    }
//
//
////        saveButton = findViewById(R.id.saveButton);
//
////        viewModel = new ViewModelProvider(this).get(DatabaseConfigViewModel.class);
////
////        connectionTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
////            toggleManualInput(checkedId == R.id.radio_manual);
////        });
////
////        saveButton.setOnClickListener(v -> saveDatabaseConfig());
////    }
//
//
//
//
//
////    private EditText urlInput, userInput, passwordInput;
////    private Button testButton, saveButton;
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_database_config);
////
////        urlInput = findViewById(R.id.urlInput);
////        userInput = findViewById(R.id.userInput);
////        passwordInput = findViewById(R.id.passwordInput);
////        testButton = findViewById(R.id.testButton);
////        saveButton = findViewById(R.id.saveButton);
////
////        testButton.setOnClickListener(v -> testConnection());
////        saveButton.setOnClickListener(v -> saveConfig());
////    }
//
//    private void testConnection() {
//        DatabaseConfig config = new DatabaseConfig(
//                hostInput.getText().toString(),
//                portInput.getText().toString(),
//                userInput.getText().toString(),
//                passwordInput.getText().toString(),
//                urlInput.getText().toString()
//                );
//
//        ApiClient.getService().testDbConnection(config).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(DatabaseConfigActivity.this, "Підключення успішне", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(DatabaseConfigActivity.this, "Помилка підключення", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(DatabaseConfigActivity.this, "Не вдалося підключитися", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveConfig() {
//        DatabaseConfig config = new DatabaseConfig(
//                hostInput.getText().toString(),
//                portInput.getText().toString(),
//                userInput.getText().toString(),
//                passwordInput.getText().toString(),
//                urlInput.getText().toString()
//        );
//
//        DbConfigManager.saveConfig(this, config);
//        Toast.makeText(this, "Налаштування збережені", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, SignUpCompanyActivity1.class));
//    }
//}
//
//
////
////
//////package com.smartinvent.activity;
//////
//////import android.content.Intent;
//////import android.os.Bundle;
//////import android.view.View;
//////import android.widget.*;
//////
//////import androidx.appcompat.app.AppCompatActivity;
//////import androidx.lifecycle.ViewModelProvider;
//////
//////import com.smartinvent.R;
//////import com.smartinvent.viewmodel.DatabaseConfigViewModel;
//////
//////public class DatabaseConfigActivity extends AppCompatActivity {
//////
//////    private EditText hostInput, portInput, databaseInput, userInput, passwordInput, urlInput;
//////    private Button saveButton;
//////    private DatabaseConfigViewModel viewModel;
//////    private Spinner dbTypeSpinner;
//////    private RadioGroup connectionTypeGroup;
//////    private RadioButton radioManual, radioUrl;
//////
//////    @Override
//////    protected void onCreate(Bundle savedInstanceState) {
//////        super.onCreate(savedInstanceState);
//////        setContentView(R.layout.activity_database_config);
//////
//////        connectionTypeGroup = findViewById(R.id.connectionTypeGroup);
//////        radioManual = findViewById(R.id.radio_manual);
//////        radioUrl = findViewById(R.id.radio_url);
//////
//////        dbTypeSpinner = findViewById(R.id.dbTypeSpinner);
//////        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//////                R.array.db_types, android.R.layout.simple_spinner_item);
//////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//////        dbTypeSpinner.setAdapter(adapter);
//////
//////        hostInput = findViewById(R.id.hostInput);
//////        portInput = findViewById(R.id.portInput);
//////        databaseInput = findViewById(R.id.databaseInput);
//////        userInput = findViewById(R.id.userInput);
//////        passwordInput = findViewById(R.id.passwordInput);
//////        urlInput = findViewById(R.id.urlInput);
//////
//////        saveButton = findViewById(R.id.saveButton);
//////        viewModel = new ViewModelProvider(this).get(DatabaseConfigViewModel.class);
//////
//////        connectionTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
//////            toggleManualInput(checkedId == R.id.radio_manual);
//////        });
//////
//////        saveButton.setOnClickListener(v -> saveDatabaseConfig());
//////    }
//////
//////    private void toggleManualInput(boolean manual) {
//////        int visibility = manual ? View.VISIBLE : View.GONE;
//////        dbTypeSpinner.setVisibility(visibility);
//////        hostInput.setVisibility(visibility);
//////        portInput.setVisibility(visibility);
//////        databaseInput.setVisibility(visibility);
//////        userInput.setVisibility(visibility);
//////        passwordInput.setVisibility(visibility);
//////        urlInput.setVisibility(manual ? View.GONE : View.VISIBLE);
//////    }
//////
//////    private void saveDatabaseConfig() {
//////        boolean isUrl = radioUrl.isChecked();
//////
//////        if (isUrl) {
//////            String url = urlInput.getText().toString().trim();
//////            if (url.isEmpty()) {
//////                Toast.makeText(this, "Введіть URL підключення!", Toast.LENGTH_SHORT).show();
//////                return;
//////            }
//////            viewModel.saveDatabaseUrl(url, success -> {
//////                if (success) {
//////                    Toast.makeText(this, "Конфігурація збережена!", Toast.LENGTH_SHORT).show();
//////                    goToAdminHome();
//////                } else {
//////                    Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
//////                }
//////            });
//////        } else {
//////            String dbType = dbTypeSpinner.getSelectedItem().toString();
//////            String host = hostInput.getText().toString().trim();
//////            String port = portInput.getText().toString().trim();
//////            String database = databaseInput.getText().toString().trim();
//////            String user = userInput.getText().toString().trim();
//////            String password = passwordInput.getText().toString().trim();
//////
//////            if (host.isEmpty() || port.isEmpty() || database.isEmpty() || user.isEmpty() || password.isEmpty()) {
//////                Toast.makeText(this, "Заповніть всі поля!", Toast.LENGTH_SHORT).show();
//////                return;
//////            }
//////
//////            viewModel.saveDatabaseConfig(dbType, host, port, database, user, password, success -> {
//////                if (success) {
//////                    Toast.makeText(this, "Конфігурація збережена!", Toast.LENGTH_SHORT).show();
//////                    goToAdminHome();
//////                } else {
//////                    Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
//////                }
//////            });
//////        }
//////    }
//////
//////    private void goToAdminHome() {
//////        startActivity(new Intent(this, AdminHomeActivity.class));
//////        finish();
//////    }
//////
//////        public void backToSignUpCompanyPage2(View v){
//////        Intent intent = new Intent(this, SignUpCompanyActivity2.class);
//////        startActivity(intent);
//////    }
//////}
////
////
//////package com.smartinvent.activity;
//////
//////import android.content.Intent;
//////import android.os.Bundle;
//////import android.view.View;
//////import android.widget.*;
//////
//////import androidx.appcompat.app.AppCompatActivity;
//////import androidx.lifecycle.ViewModelProvider;
//////
//////import com.smartinvent.R;
//////import com.smartinvent.network.DatabaseConfigRequest;
//////import com.smartinvent.viewmodel.DatabaseConfigViewModel;
//////
//////public class DatabaseConfigActivity extends AppCompatActivity {
//////
//////    private EditText hostInput, portInput, databaseInput, userInput, passwordInput, urlInput;
//////    private Button saveButton;
//////    private DatabaseConfigViewModel viewModel;
//////    private Spinner dbTypeSpinner;
//////    private RadioGroup connectionTypeGroup;
//////    private RadioButton radioManual, radioUrl;
//////
//////    @Override
//////    protected void onCreate(Bundle savedInstanceState) {
//////        super.onCreate(savedInstanceState);
//////        setContentView(R.layout.activity_database_config);
//////
//////        connectionTypeGroup = findViewById(R.id.connectionTypeGroup);
//////        radioManual = findViewById(R.id.radio_manual);
//////        radioUrl = findViewById(R.id.radio_url);
//////
//////        dbTypeSpinner = findViewById(R.id.dbTypeSpinner);
//////        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//////                R.array.db_types, android.R.layout.simple_spinner_item);
//////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//////        dbTypeSpinner.setAdapter(adapter);
//////
//////        hostInput = findViewById(R.id.hostInput);
//////        portInput = findViewById(R.id.portInput);
//////        databaseInput = findViewById(R.id.databaseInput);
//////        userInput = findViewById(R.id.userInput);
//////        passwordInput = findViewById(R.id.passwordInput);
//////        urlInput = findViewById(R.id.urlInput);
//////
//////        saveButton = findViewById(R.id.saveButton);
//////        viewModel = new ViewModelProvider(this).get(DatabaseConfigViewModel.class);
//////
//////        // Обробка перемикання між "ручним введенням" та "введенням URL"
//////        connectionTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
//////            if (checkedId == R.id.radio_manual) {
//////                toggleManualInput(true);
//////            } else {
//////                toggleManualInput(false);
//////            }
//////        });
//////
//////        saveButton.setOnClickListener(v -> saveDatabaseConfig());
//////    }
//////
//////    private void toggleManualInput(boolean manual) {
//////        int visibility = manual ? View.VISIBLE : View.GONE;
//////        dbTypeSpinner.setVisibility(visibility);
//////        hostInput.setVisibility(visibility);
//////        portInput.setVisibility(visibility);
//////        databaseInput.setVisibility(visibility);
//////        userInput.setVisibility(visibility);
//////        passwordInput.setVisibility(visibility);
//////        urlInput.setVisibility(manual ? View.GONE : View.VISIBLE);
//////    }
//////
//////
////
//////
//////    private void saveDatabaseConfig() {
//////        String dbType = dbTypeSpinner.getSelectedItem().toString();
//////        String host = hostInput.getText().toString().trim();
//////        String port = portInput.getText().toString().trim();
//////        String database = databaseInput.getText().toString().trim();
//////        String user = userInput.getText().toString().trim();
//////        String password = passwordInput.getText().toString().trim();
//////        String url = urlInput.getText().toString().trim();
//////
//////        if (radioUrl.isChecked()) {
//////            if (url.isEmpty()) {
//////                Toast.makeText(this, "Введіть URL підключення!", Toast.LENGTH_SHORT).show();
//////                return;
//////            }
//////        } else {
//////            if (host.isEmpty() || port.isEmpty() || database.isEmpty() || user.isEmpty() || password.isEmpty()) {
//////                Toast.makeText(this, "Заповніть всі поля!", Toast.LENGTH_SHORT).show();
//////                return;
//////            }
//////
//////            DatabaseConfigRequest request = new DatabaseConfigRequest(dbType, host, port, database, user, password);
//////            viewModel.saveDatabaseConfig(request, success -> {
//////                if (success) {
//////                    Toast.makeText(this, "Конфігурація збережена!", Toast.LENGTH_SHORT).show();
//////                    startActivity(new Intent(this, AdminHomeActivity.class));
//////                    finish();
//////                } else {
//////                    Toast.makeText(this, "Помилка збереження!", Toast.LENGTH_SHORT).show();
//////                }
//////            });
//////        }
//////    }
//////}
