//package com.smartinvent.ui;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.smartinvent.R;
//import com.smartinvent.adapter.ScanAdapter;
//import com.smartinvent.viewmodel.InventoryScanViewModel;
//
//public class ScanFragment extends Fragment {
//
//    private InventoryScanViewModel scanViewModel;
//    private ScanAdapter adapter;
//
//    public ScanFragment() {}
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_scan, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        scanViewModel = new ViewModelProvider(this).get(InventoryScanViewModel.class);
//
//        RecyclerView recyclerView = view.findViewById(R.id.scanRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter = new ScanAdapter();
//        recyclerView.setAdapter(adapter);
//
//        Button syncButton = view.findViewById(R.id.syncButton);
//        syncButton.setOnClickListener(v -> {
//            scanViewModel.sendScanToServer(new InventoryScan(/*дані*/));
//        });
//
//        scanViewModel.getLocalScansByProduct(1L).observe(getViewLifecycleOwner(), scans -> {
//            adapter.setScans(scans);
//        });
//    }
//}
