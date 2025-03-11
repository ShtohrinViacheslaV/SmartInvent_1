package com.smartinvent.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartinvent.R;
import com.smartinvent.fragment.*;

public class MainActivity extends AppCompatActivity {

    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        isAdmin = "ADMIN".equals(sharedPreferences.getString("role", "USER"));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Завантажуємо головний фрагмент
        if (savedInstanceState == null) {
            Fragment startFragment = isAdmin ? new AdminHomeFragment() : new UserHomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, startFragment)
                    .commit();
        }

        // Обробка натискання на вкладки
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (isAdmin) {
                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new AdminHomeFragment();
                } else if (item.getItemId() == R.id.nav_product) {
                    selectedFragment = new ProductFragment();
                } else if (item.getItemId() == R.id.nav_scanner) {
                    selectedFragment = new MainScannerFragment();
                } else if (item.getItemId() == R.id.nav_employee) {
                    selectedFragment = new EmployeeFragment();
                } else if (item.getItemId() == R.id.nav_more) {
                    selectedFragment = new MoreFragment();
                }
            }
            else {
                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new UserHomeFragment();
                } else if (item.getItemId() == R.id.nav_product) {
                    selectedFragment = new ProductFragment();
                } else if (item.getItemId() == R.id.nav_scanner) {
                    selectedFragment = new MainScannerFragment();
                } else if (item.getItemId() == R.id.nav_inventory) {
                    selectedFragment = new InventoryFragment();
                } else if (item.getItemId() == R.id.nav_more) {
                    selectedFragment = new MoreFragment();
                }
            }


            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Вибираємо правильний `BottomNavigationView`
        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(isAdmin ? R.menu.bottom_nav_menu_admin : R.menu.bottom_nav_menu_user);
    }
}



//
//            if (isAdmin) {
//                switch (item.getItemId()) {
//                    case R.id.nav_home:
//                        selectedFragment = new AdminHomeFragment();
//                        break;
//                    case R.id.nav_product:
//                        selectedFragment = new ProductFragment();
//                        break;
//                    case R.id.nav_scanner:
//                        selectedFragment = new MainScannerFragment();
//                        break;
//                    case R.id.nav_employee:
//                        selectedFragment = new EmployeeFragment();
//                        break;
//                    case R.id.nav_more:
//                        selectedFragment = new MoreFragment();
//                        break;
//                }
//            } else {
//                switch (item.getItemId()) {
//                    case R.id.nav_home:
//                        selectedFragment = new UserHomeFragment();
//                        break;
//                    case R.id.nav_product:
//                        selectedFragment = new ProductFragment();
//                        break;
//                    case R.id.nav_scanner:
//                        selectedFragment = new MainScannerFragment();
//                        break;
//                    case R.id.nav_inventory:
//                        selectedFragment = new InventoryFragment();
//                        break;
//                    case R.id.nav_more:
//                        selectedFragment = new MoreFragment();
//                        break;
//                }
// }