package com.smartinvent.activity;

import androidx.fragment.app.Fragment;
import com.smartinvent.fragment.BottomNavigationUserFragment;

public class UserHomeActivity extends BaseHomeActivity {
    @Override
    protected Fragment getBottomNavigationFragment() {
        return new BottomNavigationUserFragment();
    }
}

