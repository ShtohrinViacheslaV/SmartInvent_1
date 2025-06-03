package com.smartinvent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.smartinvent.R;
import com.smartinvent.activity.InventoryProductCheckActivity;
import com.smartinvent.activity.InventoryProductReviewActivity;
import com.smartinvent.model.Constants;
import com.smartinvent.model.InventoryProductResultDto;
import com.smartinvent.model.InventoryProductStatusEnum;

import java.util.List;


public class InventoryProductReviewAdapter extends RecyclerView.Adapter<InventoryProductReviewAdapter.ProductViewHolder> {

    private final List<InventoryProductResultDto> productList;
    private final Context context;

    public InventoryProductReviewAdapter(List<InventoryProductResultDto> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_review, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<InventoryProductResultDto> newList) {
        productList.clear();
        productList.addAll(newList);
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productStatus, productWorkId, productStorage, productCategory;
        MaterialButton confirmButton, rejectButton, infoButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productStatus = itemView.findViewById(R.id.productStatus);
            productWorkId = itemView.findViewById(R.id.productWorkId);
            productStorage = itemView.findViewById(R.id.productStorage);
            productCategory = itemView.findViewById(R.id.productCategory);

            confirmButton = itemView.findViewById(R.id.confirmButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            infoButton = itemView.findViewById(R.id.infoButton);
        }

        public void bind(InventoryProductResultDto result) {
            InventoryProductStatusEnum status = result.getStatus();

            // Статус
            if (status != null) {
                productStatus.setText(status.getDescription());

                int backgroundColor;
                switch (status) {
                    case CONFIRMED:
                        backgroundColor = ContextCompat.getColor(context, R.color.lightGreen);
                        break;
                    case MODIFIED:
                        backgroundColor = ContextCompat.getColor(context, R.color.lightBlue);
                        break;
                    case ADDED:
                        backgroundColor = ContextCompat.getColor(context, R.color.lightYellow);
                        break;
                    case NOT_FOUND:
                        backgroundColor = ContextCompat.getColor(context, android.R.color.white);
                        break;
                    default:
                        backgroundColor = ContextCompat.getColor(context, android.R.color.holo_red_light);
                        break;
                }
                itemView.setBackgroundColor(backgroundColor);
            }

            // Інформація про продукт
            if (result.getProductId() != null) {
                productName.setText(result.getProductName());
                productWorkId.setText(result.getProductWorkId());
                productStorage.setText(result.getStorageName());
                productCategory.setText(result.getCategoryName());
            } else {
                productName.setText("Невідомий продукт");
                productWorkId.setText("-");
                productStorage.setText("-");
                productCategory.setText("-");
                productStatus.setText("-");
            }

            // Обробка натискань
            infoButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, InventoryProductReviewActivity.class);
                intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, result.getInventorySessionId());
                intent.putExtra(Constants.KEY_PRODUCT_ID, result.getProductId());
                context.startActivity(intent);
            });

            confirmButton.setOnClickListener(v -> {
                Toast.makeText(context, "Підтверджено: " + result.getProductName(), Toast.LENGTH_SHORT).show();
                // Тут можна додати логіку оновлення статусу або виклику API
            });

            rejectButton.setOnClickListener(v -> {
                Toast.makeText(context, "Відхилено: " + result.getProductName(), Toast.LENGTH_SHORT).show();
                // Тут можна додати логіку для скасування/відхилення
            });
        }
    }
}

