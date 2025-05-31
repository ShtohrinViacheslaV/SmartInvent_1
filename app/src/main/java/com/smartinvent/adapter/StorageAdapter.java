package com.smartinvent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartinvent.R;
import com.smartinvent.model.Storage;

import java.util.List;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.StorageViewHolder> {

    private final List<Storage> storageList;
    private final OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnItemClickListener {
        void onItemClick(Storage storage);
    }

    public StorageAdapter(List<Storage> storageList, OnItemClickListener listener) {
        this.storageList = storageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StorageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_storage, parent, false);
        return new StorageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StorageViewHolder holder, int position) {
        Storage storage = storageList.get(position);
        holder.bind(storage, listener);

        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.selectedItemBackground));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.transparent));
        }
    }

    @Override
    public int getItemCount() {
        return storageList.size();
    }

    public void setSelectedPosition(int position) {
        int oldPosition = selectedPosition;
        selectedPosition = position;
        if (oldPosition != RecyclerView.NO_POSITION) notifyItemChanged(oldPosition);
        if (selectedPosition != RecyclerView.NO_POSITION) notifyItemChanged(selectedPosition);
    }

    static class StorageViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, locationText, detailsText;

        public StorageViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textStorageName);
            locationText = itemView.findViewById(R.id.textStorageLocation);
            detailsText = itemView.findViewById(R.id.textStorageDetails);
        }

        public void bind(Storage storage, OnItemClickListener listener) {
            nameText.setText(storage.getName());
            locationText.setText(storage.getLocation());
            detailsText.setText(storage.getDetails());
            itemView.setOnClickListener(v -> listener.onItemClick(storage));
        }
    }
}
