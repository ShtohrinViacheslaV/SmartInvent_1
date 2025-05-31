package com.smartinvent.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartinvent.R;
import com.smartinvent.model.InventorySession;
import com.smartinvent.model.InventorySessionStatusEnum;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class InventorySessionAdapter extends RecyclerView.Adapter<InventorySessionAdapter.ViewHolder> {

    private List<InventorySession> inventorySessions;
    private Context context;
    private OnSessionClickListener listener;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inventory_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventorySession session = inventorySessions.get(position);
        holder.sessionName.setText(session.getName());

        // Статус
        String displayStatus = "Невідомий статус";
        InventorySessionStatusEnum statusEnum = session.getStatus();
        Log.d("InventoryAdapter", "Статус: " + session.getStatus());

        if (statusEnum != null) {
            switch (statusEnum) {
                case ACTIVE:
                    displayStatus = "Активна";
                    break;
                case PLANNED:
                    displayStatus = "Запланована";
                    break;
                case COMPLETED:
                    displayStatus = "Завершена";
                    break;
                case CANCELLED:
                    displayStatus = "Скасована";
                    break;
            }
        }
        holder.sessionStatus.setText(displayStatus);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm z")
                .withZone(ZoneId.systemDefault());

        LocalDateTime startTime = session.getStartTime();
        if (startTime != null) {
            ZonedDateTime zonedStartTime = startTime.atZone(ZoneId.systemDefault());
            holder.sessionStartTime.setText(formatter.format(zonedStartTime));
        } else {
            holder.sessionStartTime.setText("—");
        }

        LocalDateTime endTime = session.getEndTime();
        if (endTime != null) {
            ZonedDateTime zonedEndTime = endTime.atZone(ZoneId.systemDefault());
            holder.sessionEndTime.setText(formatter.format(zonedEndTime));
        } else {
            holder.sessionEndTime.setText("—");
        }


        // Кнопка відкриття сесії
        holder.openSessionButton.setOnClickListener(v -> listener.onSessionClick(session));

        // Кнопка опису
        holder.detailsButton.setOnClickListener(v -> {
            String description = session.getDescription();
            if (description != null && !description.isEmpty()) {
                Toast.makeText(context, description, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Опис відсутній", Toast.LENGTH_SHORT).show();
            }
        });

        // Клік по всьому елементу
        holder.itemView.setOnClickListener(v -> {
            if (statusEnum == InventorySessionStatusEnum.ACTIVE) {
                listener.onSessionClick(session);
            } else {
                Toast.makeText(context, "Ця сесія неактивна", Toast.LENGTH_SHORT).show();
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
        ImageView openSessionButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionName = itemView.findViewById(R.id.session_name);
            sessionStatus = itemView.findViewById(R.id.session_status);
            sessionStartTime = itemView.findViewById(R.id.session_start_date);
            sessionEndTime = itemView.findViewById(R.id.session_end_date);
            detailsButton = itemView.findViewById(R.id.btn_description);
            openSessionButton = itemView.findViewById(R.id.btnOpenSession);
        }
    }
}