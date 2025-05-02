package com.smartinvent.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.smartinvent.R;
import com.smartinvent.model.InventorySessionProduct;
import com.smartinvent.service.InventoryService;


import java.util.ArrayList;
import java.util.List;

public class InventorySessionProductAdapter extends RecyclerView.Adapter<InventorySessionProductAdapter.ProductViewHolder> {

    private List<InventorySessionProduct> products = new ArrayList<>();
    private OnProductClickListener listener;
    private InventoryService inventoryService;

    public interface OnProductClickListener {
        void onProductClick(InventorySessionProduct product);
    }

    public InventorySessionProductAdapter(OnProductClickListener listener, InventoryService inventoryService) {
        this.listener = listener;
        this.inventoryService = inventoryService;
    }

    public void setProducts(List<InventorySessionProduct> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory_session_product, parent, false);
        return new ProductViewHolder(view, listener, inventoryService); // Передаємо inventoryService в конструктор
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        InventorySessionProduct product = products.get(position);
        holder.bind(product); // Викликаємо bind для кожного продукту
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView productName, productWorkId, productStorage, productCategory, productButton;
        private OnProductClickListener listener;
        private InventoryService inventoryService; // Додаємо поле для зберігання сервісу

        public ProductViewHolder(View itemView, OnProductClickListener listener, InventoryService inventoryService) {
            super(itemView);
            this.listener = listener;
            this.inventoryService = inventoryService;

            cardView = itemView.findViewById(R.id.cardViewProduct);
            productName = itemView.findViewById(R.id.productName);
            productWorkId = itemView.findViewById(R.id.productWorkId);
            productStorage = itemView.findViewById(R.id.productStorage);
            productCategory = itemView.findViewById(R.id.productCategory);
            productButton = itemView.findViewById(R.id.productCount);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onProductClick((InventorySessionProduct) v.getTag());
                }
            });
        }

        public void bind(InventorySessionProduct product) {
            productName.setText(product.getName());
            productWorkId.setText(product.getProductWorkId());
            productStorage.setText(product.getStorageName());
            productCategory.setText(product.getCategoryName());
            productButton.setText("Перевірити");

            // Прив'язуємо об'єкт продукту до View для подальшого використання в клік-слухачі
            itemView.setTag(product);

            // Зміна кольору картки за статусом
            if (inventoryService.isLockedByAnotherUser(product)) {
                cardView.setCardBackgroundColor(Color.parseColor("#FFCDD2")); // Червоний
            } else if (inventoryService.isAdded(product)) {
                cardView.setCardBackgroundColor(Color.parseColor("#FFF59D")); // Жовтий
            } else if (inventoryService.isChanged(product)) {
                cardView.setCardBackgroundColor(Color.parseColor("#B3E5FC")); // Блакитний
            } else if (inventoryService.isConfirmed(product)) {
                cardView.setCardBackgroundColor(Color.parseColor("#C8E6C9")); // Зелений
            } else {
                cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF")); // Білий (не перевірений)
            }
        }
    }
}
