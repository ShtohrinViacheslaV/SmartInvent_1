package com.smartinvent.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartinvent.R;

public abstract class BaseHomeActivity extends AppCompatActivity {
    protected abstract Fragment getBottomNavigationFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottomNavigationView, getBottomNavigationFragment())
                .commit();
    }
}
