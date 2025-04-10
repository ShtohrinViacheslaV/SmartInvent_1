package com.smartinvent.adapter;

import android.graphics.Color;
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
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onProductDeselected(); // Додаємо колбек для зняття вибору

    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = (productList != null) ? productList : new ArrayList<>();
        this.listener = listener;
    }

    public void setProducts(List<Product> products) {
        this.productList = (products != null) ? products : new ArrayList<>();
        selectedPosition = RecyclerView.NO_POSITION; // Скидаємо вибір після оновлення списку
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.productWorkId.setText(product.getProductWorkId());
        holder.category.setText(product.getCategory() != null ? product.getCategory().getName() : "Без категорії");
        holder.storage.setText(product.getStorage() != null ? product.getStorage().getName() : "Без складу");

        holder.count.setText(String.valueOf(product.getCount()));



        // Виділяємо вибраний елемент
        holder.itemView.setBackgroundColor(selectedPosition == holder.getAdapterPosition() ?
                Color.parseColor("#DFF0D8") : Color.WHITE);

        // Обробник кліку на елемент
        holder.itemView.setOnClickListener(v -> {
            if (selectedPosition == holder.getAdapterPosition()) {
                // Якщо вже вибраний, знімаємо вибір
                selectedPosition = RecyclerView.NO_POSITION;
                notifyDataSetChanged();
                if (listener != null) {listener.onProductDeselected();}
            } else {
                final int previousPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);

                if (listener != null) {
                    listener.onProductClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, productWorkId, storage, category, count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            productWorkId = itemView.findViewById(R.id.productWorkId);
            storage = itemView.findViewById(R.id.productStorage);
            category = itemView.findViewById(R.id.productCategory);
            count = itemView.findViewById(R.id.productCount);
        }
    }

    public Product getSelectedProduct() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return productList.get(selectedPosition);
        }
        return null;
    }
}


//public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
//
//    private List<Product> productList = new ArrayList<>();
//    private final OnProductClickListener listener;
//
//    public interface OnProductClickListener {
//        void onProductClick(Product product);
//    }
//
//    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
//        this.productList = (productList != null) ? productList : new ArrayList<>();
//        this.listener = listener;
//    }
//
//    public void setProducts(List<Product> products) {
//        this.productList = (products != null) ? products : new ArrayList<>();
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        if (productList == null || productList.isEmpty()) return;
//
//        Product product = productList.get(position);
//        holder.name.setText(product.getName());
//        holder.description.setText(product.getDescription());
//        holder.productWorkId.setText(product.getProductWorkId());
//        holder.count.setText(String.valueOf(product.getCount()));
//
//        holder.itemView.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onProductClick(product);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return productList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView name, description, productWorkId, count;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.productName);
//            description = itemView.findViewById(R.id.productDescription);
//            productWorkId = itemView.findViewById(R.id.productWorkId);
//            count = itemView.findViewById(R.id.productCount);
//        }
//    }
//}
