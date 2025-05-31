package com.smartinvent.activity;

import androidx.fragment.app.Fragment;
import com.smartinvent.fragment.BottomNavigationAdminFragment;

public class AdminHomeActivity extends BaseHomeActivity {
    @Override
    protected Fragment getBottomNavigationFragment() {
        return new BottomNavigationAdminFragment();
    }
}
