package com.smartinvent.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartinvent.R;
import com.smartinvent.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private List<Employee> employeeList = new ArrayList<>();
    private final OnEmployeeClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnEmployeeClickListener {
        void onEmployeeClick(Employee employee);
        void onEmployeeDeselected();
    }

    public EmployeeAdapter(List<Employee> employeeList, OnEmployeeClickListener listener) {
        this.employeeList = (employeeList != null) ? employeeList : new ArrayList<>();
        this.listener = listener;
    }

    public void setEmployees(List<Employee> employees) {
        this.employeeList = (employees != null) ? employees : new ArrayList<>();
        selectedPosition = RecyclerView.NO_POSITION;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.firstName.setText(employee.getFirstName());
        holder.lastName.setText(employee.getLastName());
        holder.employeeWorkId.setText(employee.getEmployeeWorkId());
        holder.phone.setText(employee.getPhone());
        holder.role.setText(employee.getRole().toString());

        holder.itemView.setBackgroundColor(selectedPosition == holder.getAdapterPosition() ?
                Color.parseColor("#D0E8FF") : Color.WHITE);

        holder.itemView.setOnClickListener(v -> {
            if (selectedPosition == holder.getAdapterPosition()) {
                selectedPosition = RecyclerView.NO_POSITION;
                notifyDataSetChanged();
                if (listener != null) listener.onEmployeeDeselected();
            } else {
                int previousPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
                if (listener != null) listener.onEmployeeClick(employee);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName, employeeWorkId, phone, role;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.employeeFirstName);
            lastName = itemView.findViewById(R.id.employeeLastName);
            employeeWorkId = itemView.findViewById(R.id.employeeWorkId);
            phone = itemView.findViewById(R.id.employeePhone);
            role = itemView.findViewById(R.id.employeeRole);
        }
    }

    public Employee getSelectedEmployee() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return employeeList.get(selectedPosition);
        }
        return null;
    }

    public void clearSelection() {
        int previous = selectedPosition;
        selectedPosition = RecyclerView.NO_POSITION;
        notifyItemChanged(previous);
    }

}
