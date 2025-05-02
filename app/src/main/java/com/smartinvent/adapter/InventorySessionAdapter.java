package com.smartinvent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartinvent.R;
import com.smartinvent.activity.InventorySessionDetailsActivity;
import com.smartinvent.model.InventorySession;

import java.util.List;

public class InventorySessionAdapter extends RecyclerView.Adapter<InventorySessionAdapter.ViewHolder> {



    private List<InventorySession> inventorySessions;
    private Context context;
    private OnSessionClickListener listener;

    public interface OnSessionClickListener {
        void onSessionClick(InventorySession session);
    }

    public InventorySessionAdapter(List<InventorySession> inventorySessions, Context context, OnSessionClickListener listener) {
        this.inventorySessions = inventorySessions;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InventorySessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inventory_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventorySessionAdapter.ViewHolder holder, int position) {
        InventorySession session = inventorySessions.get(position);
        holder.sessionName.setText(session.getName());
        holder.sessionStatus.setText(session.getStatusName());
        holder.sessionStartTime.setText(session.getStartTime());
        holder.sessionEndTime.setText(session.getEndTime());

        holder.detailsButton.setOnClickListener(v -> {
            // Перевірка наявності опису
            if (session.getDescription() != null && !session.getDescription().isEmpty()) {
                // Якщо опис є, відображаємо його через Toast
                Toast.makeText(context, session.getDescription(), Toast.LENGTH_LONG).show();
            } else {
                // Якщо опису немає
                Toast.makeText(context, "Опис відсутній", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if ("Активна".equalsIgnoreCase(session.getStatusName())) {
                Intent intent = new Intent(context, InventorySessionDetailsActivity.class);
                intent.putExtra("SESSION_ID", session.getInventorySessionId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inventorySessions.size();
    }

    public void updateList(List<InventorySession> newSessions) {
        this.inventorySessions = newSessions;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sessionName, sessionStatus, sessionStartTime, sessionEndTime;
        ImageButton detailsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionName = itemView.findViewById(R.id.session_name);
            sessionStatus = itemView.findViewById(R.id.session_status);
            sessionStartTime = itemView.findViewById(R.id.session_start_date);
            sessionEndTime = itemView.findViewById(R.id.session_end_date);
            detailsButton = itemView.findViewById(R.id.btn_description);
        }
    }
}
