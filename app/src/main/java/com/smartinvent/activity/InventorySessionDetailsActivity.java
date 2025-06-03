package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.smartinvent.R;
import com.smartinvent.model.Constants;
import com.smartinvent.model.InventorySession;
import com.smartinvent.model.InventorySessionStatusEnum;
import com.smartinvent.service.InventoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventorySessionDetailsActivity extends AppCompatActivity {

    private TextView sessionName, sessionStatus, sessionDescription;
    private TextView employeeFirstName, employeeLastName, employeeWorkId;
    private TextView startTime, endTime;
    private AnyChartView anyChartView;
    private Button btnViewProducts, btnEndSession, btnCancelSession, btnReviewResults;

    private long sessionId;
    private InventoryService inventoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_session_details);

        // Ініціалізація елементів
        sessionName = findViewById(R.id.sessionName);
        sessionStatus = findViewById(R.id.sessionStatus);
        sessionDescription = findViewById(R.id.sessionDescription);
        employeeFirstName = findViewById(R.id.employeeFirstName);
        employeeLastName = findViewById(R.id.employeeLastName);
        employeeWorkId = findViewById(R.id.employeeWorkId);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);

        anyChartView = findViewById(R.id.any_chart_view);
        btnViewProducts = findViewById(R.id.btnViewProducts);
        btnEndSession = findViewById(R.id.btnEndSession);
        btnCancelSession = findViewById(R.id.btnCancelSession);

        sessionId = getIntent().getLongExtra(Constants.KEY_INVENTORY_SESSION_ID, -1);
        inventoryService = new InventoryService();

        loadSessionDetails();
        loadChartData();

        btnViewProducts.setOnClickListener(v -> {
            Intent intent = new Intent(this, InventoryProductListActivity.class);
            intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, sessionId);
            startActivity(intent);
        });

        btnEndSession.setOnClickListener(v -> completeSession());
        btnCancelSession.setOnClickListener(v -> cancelSession());

        btnReviewResults = findViewById(R.id.btnReviewResults);
        btnReviewResults.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewInventoryResultsActivity.class);
            intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, sessionId);
            startActivity(intent);
        });

    }

    private void loadSessionDetails() {
        inventoryService.getSessionDetails(sessionId, new InventoryService.InventorySessionCallback() {
            @Override
            public void onSuccess(InventorySession session) {
                sessionName.setText(session.getName());
                sessionStatus.setText(session.getStatus().toString());
                sessionDescription.setText(session.getDescription());

                if (session.getEmployee() != null) {
                    employeeFirstName.setText(session.getEmployee().getFirstName());
                    employeeLastName.setText(session.getEmployee().getLastName());
                    employeeWorkId.setText(String.valueOf(session.getEmployee().getEmployeeWorkId()));
                }

                if (session.getStatus() == InventorySessionStatusEnum.COMPLETED) {
                    btnEndSession.setVisibility(View.GONE);
                    btnCancelSession.setVisibility(View.GONE);
                    btnReviewResults.setVisibility(View.VISIBLE);
                } else {
                    btnEndSession.setVisibility(View.VISIBLE);
                    btnCancelSession.setVisibility(View.VISIBLE);
                    btnReviewResults.setVisibility(View.GONE);
                }


                startTime.setText(session.getStartTime() != null ? session.getStartTime().toString() : "-");
                endTime.setText(session.getEndTime() != null ? session.getEndTime().toString() : "-");
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplicationContext(), "Не вдалося завантажити сесію: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadChartData() {
        inventoryService.getStatusCounts(sessionId, new InventoryService.StatusCountCallback() {
            @Override
            public void onSuccess(Map<String, Long> statusCounts) {
                drawPieChart(statusCounts);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplicationContext(), "Не вдалося завантажити дані для графіку: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawPieChart(Map<String, Long> data) {
        Pie pie = AnyChart.pie();

        List<DataEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            entries.add(new ValueDataEntry(mapStatusToUkrainian(entry.getKey()), entry.getValue()));
        }

        pie.data(entries);
        pie.title("Статуси товарів у сесії");
        anyChartView.setChart(pie);
    }

    private String mapStatusToUkrainian(String status) {
        switch (status) {
            case "ADDED":
                return "Додані";
            case "CONFIRMED":
                return "Підтверджені";
            case "MODIFIED":
                return "Модифіковані";
            case "NOT_CHECKED":
                return "Не перевірені";
            default:
                return status;
        }
    }

    private void completeSession() {
        inventoryService.completeSession(sessionId, new InventoryService.InventorySessionCallback() {
            @Override
            public void onSuccess(InventorySession session) {
                Toast.makeText(getApplicationContext(), "Сесію завершено", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplicationContext(), "Не вдалося завершити сесію: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelSession() {
        inventoryService.cancelSession(sessionId, new InventoryService.InventorySessionCallback() {
            @Override
            public void onSuccess(InventorySession session) {
                Toast.makeText(getApplicationContext(), "Сесію скасовано", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplicationContext(), "Не вдалося скасувати сесію: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
