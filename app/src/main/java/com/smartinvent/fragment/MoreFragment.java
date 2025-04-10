package com.smartinvent.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.smartinvent.R;

public class MoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_more, container, false);

        final Button btnCategories = view.findViewById(R.id.btnCategories);
        final Button btnStorages = view.findViewById(R.id.btnStorages);
        final Button btnPrintouts = view.findViewById(R.id.btnPrintouts);
        final Button btnTransactions = view.findViewById(R.id.btnTransactions);

        btnCategories.setOnClickListener(v -> openFragment(new CategoryFragment()));
        btnStorages.setOnClickListener(v -> openFragment(new StorageFragment()));
        btnPrintouts.setOnClickListener(v -> openFragment(new PrintoutFragment()));
        btnTransactions.setOnClickListener(v -> openFragment(new TransactionFragment()));

        return view;
    }

    private void openFragment(Fragment fragment) {
        final FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);  // Замінити на твій ID контейнера
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
