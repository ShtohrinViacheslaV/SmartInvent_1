package com.smartinvent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smartinvent.R;
import com.smartinvent.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList = new ArrayList<>();
    private final OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = (productList != null) ? productList : new ArrayList<>();
        this.listener = listener;
    }

    public void setProducts(List<Product> products) {
        this.productList = (products != null) ? products : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (productList == null || productList.isEmpty()) return;

        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.description.setText(product.getDescription());
        holder.productWorkId.setText(product.getProductWorkId());
        holder.count.setText(String.valueOf(product.getCount()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, productWorkId, count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            description = itemView.findViewById(R.id.productDescription);
            productWorkId = itemView.findViewById(R.id.productWorkId);
            count = itemView.findViewById(R.id.productCount);
        }
    }
}
