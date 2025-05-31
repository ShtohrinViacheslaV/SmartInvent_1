package com.smartinvent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.smartinvent.R;
import com.smartinvent.activity.*;

public class UserMoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_more, container, false);

        Button btnCategories = view.findViewById(R.id.btnCategories);
        Button btnStorages = view.findViewById(R.id.btnStorages);
        Button btnTransactions = view.findViewById(R.id.btnTransactions);
        Button btnSettings = view.findViewById(R.id.btnSettings);



        btnCategories.setOnClickListener(v -> openActivity(ManageCategoriesActivity.class));
        btnStorages.setOnClickListener(v -> openActivity(ManageStoragesActivity.class));
        btnTransactions.setOnClickListener(v -> openActivity(TransactionUserActivity.class));
        btnSettings.setOnClickListener(v -> openActivity(SettingsActivity.class));

        return view;
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(getContext(), activityClass);
        startActivity(intent);
    }
}
