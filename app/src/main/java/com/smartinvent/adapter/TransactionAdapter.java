package com.smartinvent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartinvent.R;
import com.smartinvent.model.Employee;
import com.smartinvent.model.Product;
import com.smartinvent.model.Transaction;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        // Тип транзакції
        if (transaction.getType() != null) {
            holder.transactionType.setText(transaction.getType().getDescription());
        } else {
            holder.transactionType.setText("Невідомий тип");
        }

        // Дата транзакції
        if (transaction.getTransactionDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            holder.transactionDate.setText(transaction.getTransactionDate().format(formatter));
        } else {
            holder.transactionDate.setText("Дата не вказана");
        }

        // Працівник
        Employee employee = transaction.getEmployee();
        if (employee != null) {
            holder.employeeFirstName.setText(nonNullOrDash(employee.getFirstName()));
            holder.employeeLastName.setText(nonNullOrDash(employee.getLastName()));
            holder.employeeWorkId.setText(nonNullOrDash(employee.getEmployeeWorkId()));
        } else {
            holder.employeeFirstName.setText("-");
            holder.employeeLastName.setText("-");
            holder.employeeWorkId.setText("-");
        }

        // Продукт
        Product product = transaction.getProduct();
        if (product != null) {
            holder.transactionProductName.setText(nonNullOrDash(product.getName()));
            holder.transactionProductWorkId.setText(nonNullOrDash(product.getProductWorkId()));
        } else {
            holder.transactionProductName.setText("-");
            holder.transactionProductWorkId.setText("-");
        }

        // Кількість
        String sign = transaction.getQuantity() > 0 ? "+" : "";
        holder.transactionQuantity.setText(sign + transaction.getQuantity());
    }

    private String nonNullOrDash(String value) {
        return value != null ? value : "-";
    }


    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView transactionType, transactionDate,
                employeeFirstName, employeeLastName, employeeWorkId,
                transactionProductName, transactionProductWorkId,
                transactionQuantity;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionType = itemView.findViewById(R.id.transactionType);
            transactionDate = itemView.findViewById(R.id.transactionDate);
            employeeFirstName = itemView.findViewById(R.id.employeeFirstName);
            employeeLastName = itemView.findViewById(R.id.employeeLastName);
            employeeWorkId = itemView.findViewById(R.id.employeeWorkId);
            transactionProductName = itemView.findViewById(R.id.transactionProductName);
            transactionProductWorkId = itemView.findViewById(R.id.transactionProductWorkId);
            transactionQuantity = itemView.findViewById(R.id.transactionQuantity);
        }
    }
}
