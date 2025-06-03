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
                    selectedFragment = new AdminMoreFragment();
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
                    selectedFragment = new InventorySessionsFragment();
                } else if (item.getItemId() == R.id.nav_more) {
                    selectedFragment = new UserMoreFragment();
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

//package com.smartinvent.activity;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.smartinvent.R;
//import com.smartinvent.fragment.*;
//
//public class MainActivity extends AppCompatActivity {
//
//    private boolean isAdmin;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Визначаємо роль — або з Intent, або з SharedPreferences
//        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
//
//        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
//        bottomNav.getMenu().clear();
//        bottomNav.inflateMenu(isAdmin ? R.menu.bottom_nav_menu_admin : R.menu.bottom_nav_menu_user);
//
//        if (savedInstanceState == null) {
//            Fragment defaultFragment = isAdmin ? new AdminHomeFragment() : new UserHomeFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, defaultFragment)
//                    .commit();
//        }
//
//        bottomNav.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment = getFragmentForItem(item.getItemId());
//            if (selectedFragment != null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, selectedFragment)
//                        .commit();
//                return true;
//            }
//            return false;
//        });
//    }
//
//    private Fragment getFragmentForItem(int itemId) {
//        if (isAdmin) {
//            switch (itemId) {
//                case R.id.nav_home:
//                    return new AdminHomeFragment();
//                case R.id.nav_product:
//                    return new ProductFragment();
//                case R.id.nav_scanner:
//                    return new MainScannerFragment();
//                case R.id.nav_employee:
//                    return new EmployeeFragment();
//                case R.id.nav_more:
//                    return new AdminMoreFragment();
//            }
//        } else {
//            switch (itemId) {
//                case R.id.nav_home:
//                    return new UserHomeFragment();
//                case R.id.nav_product:
//                    return new ProductFragment();
//                case R.id.nav_scanner:
//                    return new MainScannerFragment();
//                case R.id.nav_inventory:
//                    return new InventorySessionsFragment();
//                case R.id.nav_more:
//                    return new UserMoreFragment();
//            }
//        }
//        return null;
//    }
//}
//
////package com.smartinvent.activity;
////
////import android.content.SharedPreferences;
////import android.os.Bundle;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.fragment.app.Fragment;
////import com.google.android.material.bottomnavigation.BottomNavigationView;
////import com.smartinvent.R;
////import com.smartinvent.fragment.*;
////
////public class MainActivity extends AppCompatActivity {
////
////    private boolean isAdmin;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
////        isAdmin = "ADMIN".equals(sharedPreferences.getString("role", "USER"));
////
////        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
////
////        // Завантаження меню відповідно до ролі
////        bottomNavigationView.getMenu().clear();
////        bottomNavigationView.inflateMenu(isAdmin
////                ? R.menu.bottom_nav_menu_admin
////                : R.menu.bottom_nav_menu_user
////        );
////
////        // Початковий фрагмент
////        if (savedInstanceState == null) {
////            Fragment startFragment = isAdmin ? new AdminHomeFragment() : new UserHomeFragment();
////            getSupportFragmentManager().beginTransaction()
////                    .replace(R.id.fragment_container, startFragment)
////                    .commit();
////        }
////
////        // Обробка натискання на пункти меню
////        bottomNavigationView.setOnItemSelectedListener(item -> {
////            Fragment selectedFragment = null;
////
////            if (isAdmin) {
////                if (item.getItemId() == R.id.nav_home) {
////                    selectedFragment = new AdminHomeFragment();
////                } else if (item.getItemId() == R.id.nav_product) {
////                    selectedFragment = new ProductFragment();
////                } else if (item.getItemId() == R.id.nav_scanner) {
////                    selectedFragment = new MainScannerFragment();
////                } else if (item.getItemId() == R.id.nav_employee) {
////                    selectedFragment = new EmployeeFragment();
////                } else if (item.getItemId() == R.id.nav_more) {
////                    selectedFragment = new AdminMoreFragment();
////                }
////            }
////            else {
////                if (item.getItemId() == R.id.nav_home) {
////                    selectedFragment = new UserHomeFragment();
////                } else if (item.getItemId() == R.id.nav_product) {
////                    selectedFragment = new ProductFragment();
////                } else if (item.getItemId() == R.id.nav_scanner) {
////                    selectedFragment = new MainScannerFragment();
////                } else if (item.getItemId() == R.id.nav_inventory) {
////                    selectedFragment = new InventorySessionsFragment();
////                } else if (item.getItemId() == R.id.nav_more) {
////                    selectedFragment = new UserMoreFragment();
////                }
////            }
////
////
////            if (selectedFragment != null) {
////                getSupportFragmentManager().beginTransaction()
////                        .replace(R.id.fragment_container, selectedFragment)
////                        .commit();
////                return true;
////            }
////            return false;
////        });
////    }
////}
////
////
////
//////package com.smartinvent.activity;
//////
//////import android.content.SharedPreferences;
//////import android.os.Bundle;
//////import androidx.annotation.NonNull;
//////import androidx.appcompat.app.AppCompatActivity;
//////import androidx.fragment.app.Fragment;
//////import com.google.android.material.bottomnavigation.BottomNavigationView;
//////import com.smartinvent.R;
//////import com.smartinvent.fragment.*;
//////
//////public class MainActivity extends AppCompatActivity {
//////
//////    private boolean isAdmin;
//////
//////    @Override
//////    protected void onCreate(Bundle savedInstanceState) {
//////        super.onCreate(savedInstanceState);
//////        setContentView(R.layout.activity_main);
//////
//////        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
//////        isAdmin = "ADMIN".equals(sharedPreferences.getString("role", "USER"));
//////
//////        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//////
//////        // Завантажуємо головний фрагмент
//////        if (savedInstanceState == null) {
//////            Fragment startFragment = isAdmin ? new AdminHomeFragment() : new UserHomeFragment();
//////            getSupportFragmentManager().beginTransaction()
//////                    .replace(R.id.fragment_container, startFragment)
//////                    .commit();
//////        }
//////
//////        // Обробка натискання на вкладки
//////        bottomNavigationView.setOnItemSelectedListener(item -> {
//////            Fragment selectedFragment = null;
//////
//////            if (isAdmin) {
//////                if (item.getItemId() == R.id.nav_home) {
//////                    selectedFragment = new AdminHomeFragment();
//////                } else if (item.getItemId() == R.id.nav_product) {
//////                    selectedFragment = new ProductFragment();
//////                } else if (item.getItemId() == R.id.nav_scanner) {
//////                    selectedFragment = new MainScannerFragment();
//////                } else if (item.getItemId() == R.id.nav_employee) {
//////                    selectedFragment = new EmployeeFragment();
//////                } else if (item.getItemId() == R.id.nav_more) {
//////                    selectedFragment = new AdminMoreFragment();
//////                }
//////            }
//////            else {
//////                if (item.getItemId() == R.id.nav_home) {
//////                    selectedFragment = new UserHomeFragment();
//////                } else if (item.getItemId() == R.id.nav_product) {
//////                    selectedFragment = new ProductFragment();
//////                } else if (item.getItemId() == R.id.nav_scanner) {
//////                    selectedFragment = new MainScannerFragment();
//////                } else if (item.getItemId() == R.id.nav_inventory) {
//////                    selectedFragment = new InventorySessionsFragment();
//////                } else if (item.getItemId() == R.id.nav_more) {
//////                    selectedFragment = new UserMoreFragment();
//////                }
//////            }
//////
//////
//////            if (selectedFragment != null) {
//////                getSupportFragmentManager().beginTransaction()
//////                        .replace(R.id.fragment_container, selectedFragment)
//////                        .commit();
//////                return true;
//////            }
//////            return false;
//////        });
//////
//////        // Вибираємо правильний `BottomNavigationView`
//////        bottomNavigationView.getMenu().clear();
//////        bottomNavigationView.inflateMenu(isAdmin ? R.menu.bottom_nav_menu_admin : R.menu.bottom_nav_menu_user);
//////    }
//////}
//////
//////
