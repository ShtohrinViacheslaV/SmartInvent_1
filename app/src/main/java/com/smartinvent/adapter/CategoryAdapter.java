package com.smartinvent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartinvent.R;
import com.smartinvent.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<Category> categoryList;
    private final OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION; // -1, якщо нічого не вибрано

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public CategoryAdapter(List<Category> categoryList, OnItemClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category, listener);

        // Підсвічуємо вибраний елемент
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.selectedItemBackground));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.transparent));
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // Оновлюємо вибрану позицію із зовнішнього класу
    public void setSelectedPosition(int position) {
        int oldPosition = selectedPosition;
        selectedPosition = position;
        if (oldPosition != RecyclerView.NO_POSITION) notifyItemChanged(oldPosition);
        if (selectedPosition != RecyclerView.NO_POSITION) notifyItemChanged(selectedPosition);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, descriptionText;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textCategoryName);
            descriptionText = itemView.findViewById(R.id.textCategoryDescription);
        }

        public void bind(Category category, OnItemClickListener listener) {
            nameText.setText(category.getName());
            descriptionText.setText(category.getDescription());

            itemView.setOnClickListener(v -> listener.onItemClick(category));
        }
    }
}
