package com.smartinvent.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.smartinvent.R;

public class InventoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        // Отримуємо TextView
        final TextView textView = view.findViewById(R.id.tvInventory);
        if (textView != null) {
            textView.setText("Сторінка для інвентаризації. Тут буде функціонал пізніше.");
        }

        return view;
    }
}
