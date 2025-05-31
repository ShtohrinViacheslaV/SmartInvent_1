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
import androidx.recyclerview.widget.RecyclerView;

import com.smartinvent.R;
import com.smartinvent.activity.InventoryProductCheckActivity;
import com.smartinvent.model.Constants;
import com.smartinvent.model.InventoryProductResultDto;

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
            if (result.getProductId() != null) {
                Intent intent = new Intent(context, InventoryProductCheckActivity.class);
                intent.putExtra(Constants.KEY_INVENTORY_SESSION_ID, result.getInventorySessionId());
                intent.putExtra(Constants.KEY_PRODUCT_ID, result.getProductId());
                Log.d("SessionID InventorySessionProductAdapter", String.valueOf(result.getInventorySessionId()));
                Log.d("ProductID InventorySessionProductAdapter", String.valueOf(result.getProductId()));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Немає інформації про продукт", Toast.LENGTH_SHORT).show();
            }
        });
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
