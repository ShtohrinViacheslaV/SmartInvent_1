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

//package com.smartinvent.activity;
//
//import android.os.Bundle;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.smartinvent.R;
//import com.smartinvent.fragment.*;
//
//
//public abstract class BaseHomeActivity extends AppCompatActivity {
//
//    protected abstract boolean isAdmin();
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Ініціалізація правильного стартового фрагмента
//        if (savedInstanceState == null) {
//            Fragment defaultFragment = isAdmin() ? new AdminHomeFragment() : new UserHomeFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, defaultFragment)
//                    .commit();
//        }
//
//        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
//
//        // Завантаження відповідного меню
//        bottomNav.getMenu().clear();
//        bottomNav.inflateMenu(isAdmin() ? R.menu.bottom_nav_menu_admin : R.menu.bottom_nav_menu_user);
//
//        bottomNav.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment = null;
//
//            if (isAdmin()) {
//                if (item.getItemId() == R.id.nav_home) {
//                    selectedFragment = new AdminHomeFragment();
//                } else if (item.getItemId() == R.id.nav_product) {
//                    selectedFragment = new ProductFragment();
//                } else if (item.getItemId() == R.id.nav_scanner) {
//                    selectedFragment = new MainScannerFragment();
//                } else if (item.getItemId() == R.id.nav_employee) {
//                    selectedFragment = new EmployeeFragment();
//                } else if (item.getItemId() == R.id.nav_more) {
//                    selectedFragment = new AdminMoreFragment();
//                }
//            }
//            else {
//                if (item.getItemId() == R.id.nav_home) {
//                    selectedFragment = new UserHomeFragment();
//                } else if (item.getItemId() == R.id.nav_product) {
//                    selectedFragment = new ProductFragment();
//                } else if (item.getItemId() == R.id.nav_scanner) {
//                    selectedFragment = new MainScannerFragment();
//                } else if (item.getItemId() == R.id.nav_inventory) {
//                    selectedFragment = new InventorySessionsFragment();
//                } else if (item.getItemId() == R.id.nav_more) {
//                    selectedFragment = new UserMoreFragment();
//                }
//            }
//
//
//            if (selectedFragment != null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, selectedFragment)
//                        .commit();
//                return true;
//            }
//            return false;
//        });
//    }
//}
//
//
//
//
////
////package com.smartinvent.activity;
////
////import android.os.Bundle;
////import androidx.annotation.Nullable;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.fragment.app.Fragment;
////import com.google.android.material.bottomnavigation.BottomNavigationView;
////import com.smartinvent.R;
////import com.smartinvent.fragment.AdminHomeFragment;
////import com.smartinvent.fragment.UserHomeFragment;
////
////public abstract class BaseHomeActivity extends AppCompatActivity {
////    protected abstract Fragment getBottomNavigationFragment();
////
////    @Override
////    protected void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////        if (savedInstanceState == null) {
////            Fragment defaultFragment;
////
////            if (this instanceof AdminHomeActivity) {
////                defaultFragment = new AdminHomeFragment();
////            } else {
////                defaultFragment = new UserHomeFragment();
////            }
////
////            getSupportFragmentManager().beginTransaction()
////                    .replace(R.id.fragment_container, defaultFragment)
////                    .commit();
////        }
////
////        // Тут можна, наприклад, ініціалізувати BottomNavigationView і слухач
////        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
////        bottomNav.setOnItemSelectedListener(item -> {
////            Fragment selectedFragment = getBottomNavigationFragment();
////            getSupportFragmentManager().beginTransaction()
////                    .replace(R.id.fragment_container, selectedFragment)
////                    .commit();
////            return true;
////        });
////    }
////
////}
