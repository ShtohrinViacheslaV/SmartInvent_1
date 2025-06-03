package com.smartinvent.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.smartinvent.R;
import com.smartinvent.activity.InventoryProductCheckActivity;
import com.smartinvent.model.Constants;
import com.smartinvent.model.InventoryProductResultDto;
import com.smartinvent.model.InventoryProductStatusEnum;

import java.util.List;

public class InventorySessionProductAdapter extends RecyclerView.Adapter<InventorySessionProductAdapter.ProductViewHolder> {

    private OnItemClickListener onItemClickListener;

    private final List<InventoryProductResultDto> productList;
    private final Context context;

    public InventorySessionProductAdapter(List<InventoryProductResultDto> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inventory_session_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        InventoryProductResultDto result = productList.get(position);

        InventoryProductStatusEnum status = result.getStatus();
        if (status != null) {
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
                default:
                    backgroundColor = ContextCompat.getColor(context, android.R.color.white);
                    break;
            }
            holder.itemView.setBackgroundColor(backgroundColor);
        }



        if (result.getProductId() != null) {
            holder.productName.setText(result.getProductName());
            holder.productWorkId.setText(result.getProductWorkId());
            holder.productStorage.setText(result.getStorageName());
            holder.productCategory.setText(result.getCategoryName());
            holder.productCount.setText(String.valueOf(result.getCount()));
        } else {
            holder.productName.setText("Невідомий продукт");
            holder.productWorkId.setText("-");
            holder.productStorage.setText("-");
            holder.productCategory.setText("-");
            holder.productCount.setText("0");
        }

        holder.productCount.setText(String.valueOf(result.getCount()));

        holder.itemView.setOnClickListener(v -> {
            if (result.getProductId() == null) {
                Toast.makeText(context, "Немає інформації про продукт", Toast.LENGTH_SHORT).show();
                return;
            }

            if (status != null && status != InventoryProductStatusEnum.NOT_FOUND) {
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("Товар вже перевірено")
                        .setMessage("Цей товар уже перевірено зі статусом «" + status.getDescription() + "». Ви впевнені, що хочете перевірити його повторно?\n\n" +
                                "Повторна перевірка змінить попередній результат для цього товару.")
                        .setPositiveButton("Перевірити повторно", (dialog, which) -> {
                            openCheckActivity(result);
                        })
                        .setNegativeButton("Скасувати", null)
                        .show();
            } else {
                openCheckActivity(result);
            }
        });
    }

    private void openCheckActivity(InventoryProductResultDto result) {
        Intent intent = new Intent(context, InventoryProductCheckActivity.class);
        intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, result.getInventorySessionId());
        intent.putExtra(Constants.KEY_PRODUCT_ID, result.getProductId());
        context.startActivity(intent);
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

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productWorkId, productStorage, productCategory, productCount;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productWorkId = itemView.findViewById(R.id.productWorkId);
            productStorage = itemView.findViewById(R.id.productStorage);
            productCategory = itemView.findViewById(R.id.productCategory);
            productCount = itemView.findViewById(R.id.productCount);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(InventoryProductResultDto product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }





}
