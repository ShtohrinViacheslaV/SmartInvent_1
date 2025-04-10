package com.smartinvent.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartinvent.R;

public class BottomNavigationAdminFragment extends Fragment {
    public BottomNavigationAdminFragment() {
        super(R.layout.fragment_bottom_navigation_admin);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation_admin);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_admin_home) {
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

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
