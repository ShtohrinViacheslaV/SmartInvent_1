package com.smartinvent.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.Navigation;
import com.google.android.material.card.MaterialCardView;
import com.smartinvent.R;
import com.smartinvent.activity.*;

public class UserHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);


        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String firstName = prefs.getString("firstName", "");
        String lastName = prefs.getString("lastName", "");

        // Формуємо привітання і встановлюємо у TextView
        TextView titleText = view.findViewById(R.id.titleText);
        titleText.setText("Вітаю, " + firstName + " " + lastName + "!");


        // Прив'язуємо картки
        MaterialCardView cardInventory = view.findViewById(R.id.cardInventory);
        MaterialCardView cardSettings = view.findViewById(R.id.cardSettings);
        MaterialCardView cardProduct = view.findViewById(R.id.cardProduct);
        MaterialCardView cardStorage = view.findViewById(R.id.cardStorage);
        MaterialCardView cardCategory = view.findViewById(R.id.cardCategory);
        MaterialCardView cardQRScanner = view.findViewById(R.id.cardQRScanner);

        // Встановлюємо обробники кліків
        cardInventory.setOnClickListener(v -> openFragment(new InventorySessionsFragment()));
        cardSettings.setOnClickListener(v -> openActivity(SettingsActivity.class));
        cardProduct.setOnClickListener(v -> openFragment(new ProductFragment()));
        cardStorage.setOnClickListener(v -> openActivity(ManageStoragesActivity.class));
        cardCategory.setOnClickListener(v -> openActivity(ManageCategoriesActivity.class));
        cardQRScanner.setOnClickListener(v -> openFragment(new MainScannerFragment()));

        return view;
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(getContext(), activityClass);
        startActivity(intent);
    }

    private void openFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // ID контейнера у вашому layout
                .addToBackStack(null)
                .commit();
    }
}