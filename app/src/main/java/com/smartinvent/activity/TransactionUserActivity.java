package com.smartinvent.activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.smartinvent.R;
import com.smartinvent.adapter.TransactionAdapter;
import com.smartinvent.model.Transaction;
import com.smartinvent.model.TransactionTypeEnum;
import com.smartinvent.service.TransactionService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionUserActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button sortButton, filterButton, resetButton;
    private RecyclerView recyclerView;
    private TransactionService transactionService;
    private TransactionAdapter adapter;
    private List<Transaction> allTransactions = new ArrayList<>();
    private List<Transaction> filteredTransactions = new ArrayList<>();
    private Long loggedInEmployeeId;

    // Фільтри
    private String searchQuery = "";
    private TransactionTypeEnum selectedTransactionType = null;
    private String selectedProductWorkId = null;
    private LocalDateTime dateFrom = null, dateTo = null;
    private boolean sortByDateAsc = true;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        loggedInEmployeeId = sharedPreferences.getLong("employeeId", -1);  // Ініціалізація поля класу

        if (loggedInEmployeeId == -1) {
            Toast.makeText(this, "Користувач не авторизований", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        searchEditText = findViewById(R.id.searchEditText);
        sortButton = findViewById(R.id.sortButton);
        filterButton = findViewById(R.id.filterButton);
        resetButton = findViewById(R.id.resetButton);
        recyclerView = findViewById(R.id.transactionRecyclerView);

        adapter = new TransactionAdapter(filteredTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        transactionService = new TransactionService();

        loadTransactions();


        searchEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString();
                applyFilters();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        });

        sortButton.setOnClickListener(v -> showSortDialog());
        filterButton.setOnClickListener(v -> showFilterDialog());
        resetButton.setOnClickListener(v -> resetFilters());
    }


    private void loadTransactions() {
        transactionService.getTransactionsByEmployeeId(loggedInEmployeeId, new TransactionService.TransactionsCallback() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                allTransactions.clear();
                allTransactions.addAll(transactions);
                applyFilters();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(TransactionUserActivity.this, "Помилка завантаження транзакцій: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void applyFilters() {
        filteredTransactions.clear();

        for (Transaction t : allTransactions) {
            if (!matchesSearch(t, searchQuery)) continue;
            if (selectedTransactionType != null && t.getType() != selectedTransactionType) continue;
            if (selectedProductWorkId != null && !selectedProductWorkId.equalsIgnoreCase(t.getProduct().getProductWorkId())) continue;
            if (dateFrom != null && t.getTransactionDate().isBefore(dateFrom)) continue;
            if (dateTo != null && t.getTransactionDate().isAfter(dateTo)) continue;

            filteredTransactions.add(t);
        }

        filteredTransactions.sort((a, b) -> {
            if (sortByDateAsc) return a.getTransactionDate().compareTo(b.getTransactionDate());
            else return b.getTransactionDate().compareTo(a.getTransactionDate());
        });

        adapter.notifyDataSetChanged();
    }

    private boolean matchesSearch(Transaction t, String query) {
        if (query == null || query.trim().isEmpty()) return true;
        query = query.toLowerCase();

        return t.getProduct().getName().toLowerCase().contains(query)
                || t.getProduct().getProductWorkId().toLowerCase().contains(query)
                || t.getEmployee().getLastName().toLowerCase().contains(query)
                || t.getEmployee().getEmployeeWorkId().toLowerCase().contains(query);
    }

    private void resetFilters() {
        searchQuery = "";
        selectedTransactionType = null;
        selectedProductWorkId = null;
        dateFrom = null;
        dateTo = null;
        sortByDateAsc = true;

        searchEditText.setText("");
        applyFilters();
    }

    private void showSortDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_transaction_sort, null);
        dialog.setContentView(view);

        RadioGroup sortGroup = view.findViewById(R.id.sortGroup);
        Button applyBtn = view.findViewById(R.id.applySortBtn);

        sortGroup.check(sortByDateAsc ? R.id.radioAsc : R.id.radioDesc);

        applyBtn.setOnClickListener(v -> {
            sortByDateAsc = (sortGroup.getCheckedRadioButtonId() == R.id.radioAsc);
            applyFilters();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showFilterDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_user_transaction_filter, null);
        dialog.setContentView(view);

        RadioGroup transactionTypeGroup = view.findViewById(R.id.transactionTypeGroup);
        EditText productWorkIdInput = view.findViewById(R.id.productWorkIdInput);
        TextView fromDateText = view.findViewById(R.id.fromDateText);
        TextView toDateText = view.findViewById(R.id.toDateText);
        Button applyBtn = view.findViewById(R.id.applyFilterBtn);

        fromDateText.setOnClickListener(v -> showDatePicker(fromDateText, true));
        toDateText.setOnClickListener(v -> showDatePicker(toDateText, false));

        applyBtn.setOnClickListener(v -> {
            int checkedId = transactionTypeGroup.getCheckedRadioButtonId();
            if (checkedId == R.id.radioArrival) {
                selectedTransactionType = TransactionTypeEnum.ARRIVAL;
            } else if (checkedId == R.id.radioDeparture) {
                selectedTransactionType = TransactionTypeEnum.DEPARTURE;
            } else if (checkedId == R.id.radioUpdate) {
                selectedTransactionType = TransactionTypeEnum.UPDATE;
            } else {
                selectedTransactionType = null;
            }

            selectedProductWorkId = productWorkIdInput.getText().toString().isEmpty() ? null :
                    productWorkIdInput.getText().toString();



            try {
                dateFrom = fromDateText.getText().toString().isEmpty() ? null :
                        LocalDateTime.parse(fromDateText.getText().toString(), dtf);

                dateTo = toDateText.getText().toString().isEmpty() ? null :
                        LocalDateTime.parse(toDateText.getText().toString(), dtf);

            } catch (Exception e) {
                e.printStackTrace();
            }

            applyFilters();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showDatePicker(TextView target, boolean isFrom) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            target.setText(dtf.format(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
