package com.smartinvent.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartinvent.R;

public abstract class BaseHomeActivity extends AppCompatActivity {
    protected abstract Fragment getBottomNavigationFragment(); // Фрагмент навігації для кожного типу користувача

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Загальний макет для обох ролей

        // Додаємо відповідний фрагмент навігації
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottomNavigationView, getBottomNavigationFragment())
                .commit();
    }
}
