package com.smartinvent.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartinvent.R;
import com.smartinvent.adapter.CategoryAdapter;
import com.smartinvent.model.Category;
import com.smartinvent.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoriesActivity extends AppCompatActivity implements CategoryAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categoryList = new ArrayList<>();
    private CategoryService categoryService;

    private Button btnSave, btnCancel;
    private ImageButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private EditText editName, editDescription;
    private View formLayout;
    private TextView formTitle;

    private Category selectedCategory = null;
    private boolean isEditMode = false; // флаг: редагування чи створення
    private int selectedPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        categoryService = new CategoryService();

        // Ініціалізація елементів
        recyclerView = findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(categoryList, this);
        recyclerView.setAdapter(adapter);

        btnAdd = findViewById(R.id.buttonAddCategory);
        btnEdit = findViewById(R.id.buttonEditCategory);
        btnDelete = findViewById(R.id.buttonDeleteCategory);
        btnRefresh = findViewById(R.id.buttonRefreshCategories);
        btnSave = findViewById(R.id.buttonSaveCategory);
        btnCancel = findViewById(R.id.buttonCancel);

        editName = findViewById(R.id.editTextCategoryName);
        editDescription = findViewById(R.id.editTextCategoryDescription);
        formLayout = findViewById(R.id.formLayout);
        formTitle = findViewById(R.id.formTitle);

        // Обробники кнопок
        btnAdd.setOnClickListener(v -> showFormForCreate());
        btnEdit.setOnClickListener(v -> {
            if (selectedCategory != null) {
                showFormForEdit();
            } else {
                Toast.makeText(this, "Оберіть категорію для редагування", Toast.LENGTH_SHORT).show();
            }
        });
        btnDelete.setOnClickListener(v -> {
            if (selectedCategory != null) {
                deleteSelectedCategory();
            } else {
                Toast.makeText(this, "Оберіть категорію для видалення", Toast.LENGTH_SHORT).show();
            }
        });
        btnRefresh.setOnClickListener(v -> loadCategories());
        btnSave.setOnClickListener(v -> saveCategory());
        btnCancel.setOnClickListener(v -> hideForm());

        updateActionButtonsState();
        loadCategories();
    }

    private void loadCategories() {
        categoryService.getAllCategories(new CategoryService.CategoryCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                categoryList.clear();
                categoryList.addAll(categories);
                adapter.notifyDataSetChanged();

                selectedCategory = null;
                selectedPosition = RecyclerView.NO_POSITION;
                adapter.setSelectedPosition(selectedPosition);

                updateActionButtonsState();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ManageCategoriesActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFormForCreate() {
        isEditMode = false;
        selectedCategory = null;
        selectedPosition = RecyclerView.NO_POSITION;
        adapter.setSelectedPosition(selectedPosition);
        updateActionButtonsState();

        formTitle.setText("Створити категорію");
        editName.setText("");
        editDescription.setText("");
        formLayout.setVisibility(View.VISIBLE);

        setUiEnabled(false); // Блокуємо інтерфейс крім форми
    }


    private void showFormForEdit() {
        isEditMode = true;

        formTitle.setText("Редагувати категорію");
        editName.setText(selectedCategory.getName());
        editDescription.setText(selectedCategory.getDescription());
        formLayout.setVisibility(View.VISIBLE);

        setUiEnabled(false); // Блокуємо інтерфейс крім форми
    }

    private void hideForm() {
        formLayout.setVisibility(View.GONE);
        editName.setText("");
        editDescription.setText("");
        selectedCategory = null;
        selectedPosition = RecyclerView.NO_POSITION;
        adapter.setSelectedPosition(selectedPosition);
        updateActionButtonsState();

        setUiEnabled(true); // Розблокуємо інтерфейс
    }

    private void setUiEnabled(boolean enabled) {
        recyclerView.setEnabled(enabled);
        btnAdd.setEnabled(enabled);
        btnEdit.setEnabled(enabled && selectedCategory != null);
        btnDelete.setEnabled(enabled && selectedCategory != null);
        btnRefresh.setEnabled(enabled);
    }

    private void saveCategory() {
        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Назва обов'язкова", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode && selectedCategory != null) {
            selectedCategory.setName(name);
            selectedCategory.setDescription(description);
            categoryService.updateCategory(selectedCategory, new CategoryService.SingleCategoryCallback() {
                @Override
                public void onSuccess(Category category) {
                    Toast.makeText(ManageCategoriesActivity.this, "Категорію оновлено", Toast.LENGTH_SHORT).show();
                    hideForm();
                    loadCategories();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(ManageCategoriesActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Category newCategory = new Category(null, name, description);
            categoryService.createCategory(newCategory, new CategoryService.SingleCategoryCallback() {
                @Override
                public void onSuccess(Category category) {
                    Toast.makeText(ManageCategoriesActivity.this, "Категорію створено", Toast.LENGTH_SHORT).show();
                    hideForm();
                    loadCategories();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(ManageCategoriesActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteSelectedCategory() {
        categoryService.deleteCategory(selectedCategory.getCategoryId(), new CategoryService.SimpleCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(ManageCategoriesActivity.this, "Категорію видалено", Toast.LENGTH_SHORT).show();
                selectedCategory = null;
                loadCategories();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ManageCategoriesActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(Category category) {
        selectedCategory = category;

        selectedPosition = categoryList.indexOf(category);
        adapter.setSelectedPosition(selectedPosition);

        Toast.makeText(this, "Обрано: " + category.getName(), Toast.LENGTH_SHORT).show();

        updateActionButtonsState();
    }

    private void updateActionButtonsState() {
        boolean hasSelection = selectedCategory != null;
        btnEdit.setEnabled(hasSelection);
        btnDelete.setEnabled(hasSelection);
    }
}
