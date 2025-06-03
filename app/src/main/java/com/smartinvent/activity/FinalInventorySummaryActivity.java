package com.smartinvent.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.smartinvent.service.InventoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FinalInventorySummaryActivity extends AppCompatActivity {

    private AnyChartView finalChartView;
    private Button btnFinishAndSave;
    private InventoryService inventoryService;

    private long sessionId;

    private TextView cardTotalProducts, cardConfirmed, cardModified, cardAdded, cardNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_inventory_summary);

        sessionId = getIntent().getLongExtra(Constants.KEY_INVENTORY_SESSION_ID, -1);
        inventoryService = new InventoryService();

        finalChartView = findViewById(R.id.final_chart_view);
        btnFinishAndSave = findViewById(R.id.btnFinishAndSave);

        cardTotalProducts = findViewById(R.id.cardTotalProducts).findViewById(R.id.tvValue);
        cardConfirmed = findViewById(R.id.cardConfirmed).findViewById(R.id.tvValue);
        cardModified = findViewById(R.id.cardModified).findViewById(R.id.tvValue);
        cardAdded = findViewById(R.id.cardAdded).findViewById(R.id.tvValue);
        cardNotFound = findViewById(R.id.cardNotFound).findViewById(R.id.tvValue);

        loadSummaryData();

        btnFinishAndSave.setOnClickListener(v -> completeInventorySession());
    }

    private void loadSummaryData() {
        inventoryService.getStatusCounts(sessionId, new InventoryService.StatusCountCallback() {
            @Override
            public void onSuccess(Map<String, Long> statusCounts) {
                long total = 0;
                for (Long count : statusCounts.values()) {
                    total += count;
                }

                cardTotalProducts.setText(String.valueOf(total));
                cardConfirmed.setText(String.valueOf(statusCounts.getOrDefault("CONFIRMED", 0L)));
                cardModified.setText(String.valueOf(statusCounts.getOrDefault("MODIFIED", 0L)));
                cardAdded.setText(String.valueOf(statusCounts.getOrDefault("ADDED", 0L)));
                cardNotFound.setText(String.valueOf(statusCounts.getOrDefault("NOT_CHECKED", 0L)));

                drawChart(statusCounts);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FinalInventorySummaryActivity.this,
                        "Помилка при завантаженні статистики: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawChart(Map<String, Long> data) {
        Pie pie = AnyChart.pie();

        List<DataEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            entries.add(new ValueDataEntry(mapStatusToUkrainian(entry.getKey()), entry.getValue()));
        }

        pie.data(entries);
        pie.title("Підсумок статусів товарів");
        finalChartView.setChart(pie);
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
                return "Не знайдені";
            default:
                return status;
        }
    }

    private void completeInventorySession() {
        inventoryService.completeAndBackupSession(sessionId, new InventoryService.StringCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(FinalInventorySummaryActivity.this,
                            message, Toast.LENGTH_SHORT).show();

                    // Переходимо на сторінку з усіма сесіями
                    Intent intent = new Intent(FinalInventorySummaryActivity.this, InventorySessionAdminActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(FinalInventorySummaryActivity.this,
                            "Не вдалося завершити сесію: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }




}
