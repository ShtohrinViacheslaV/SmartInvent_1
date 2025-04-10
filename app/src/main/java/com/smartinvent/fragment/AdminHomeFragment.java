package com.smartinvent.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.smartinvent.R;

public class AdminHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        // Обробка натискань на картки
        final MaterialCardView cardProducts = view.findViewById(R.id.cardProducts);
        final MaterialCardView cardInventory = view.findViewById(R.id.cardInventory);
        final MaterialCardView cardReports = view.findViewById(R.id.cardReports);

        cardProducts.setOnClickListener(v -> openProducts());
        cardInventory.setOnClickListener(v -> openInventory());
        cardReports.setOnClickListener(v -> openReports());

        return view;
    }

    private void openProducts() {
        Toast.makeText(getContext(), "Перехід до Продуктів", Toast.LENGTH_SHORT).show();
    }

    private void openInventory() {
        Toast.makeText(getContext(), "Перехід до Інвентаризації", Toast.LENGTH_SHORT).show();
    }

    private void openReports() {
        Toast.makeText(getContext(), "Перехід до Звітів", Toast.LENGTH_SHORT).show();
    }
}
