package com.example.ecorecicla.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecorecicla.R;
import com.example.ecorecicla.models.Category;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categoryList;
    private ChipGroup chipGroup;

    public CategoryAdapter(List<Category> categoryList, ChipGroup chipGroup) {
        this.categoryList = categoryList;
        this.chipGroup = chipGroup;
    }

    // Inflar el diseño del elemento de categoría
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    // Vincular datos a la vista del elemento
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bindCategory(category);
    }

    // Devolver la cantidad de elementos en la lista
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvQuantity;
        private final TextView tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar las vistas dentro del elemento de la categoría
            tvDate = itemView.findViewById(R.id.tvDate);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }

        // Establecer datos en las vistas
        public void bindCategory(Category category) {
            tvDate.setText(category.getDate());
            tvQuantity.setText(category.getQuantity() + " Kg");
            tvPrice.setText(category.getPrice() + " $");
        }
    }

    public void addFilterChips() {
        // Obtener el contexto del ChipGroup
        Context context = chipGroup.getContext();

        // Crear y agregar Chips al ChipGroup
        addChip("Fecha", R.drawable.ic_close, (v -> {
            resetChips();
            setFilterByDate(true);
            setFilterByQuantity(false);
            setFilterByPrice(false);
            sortByDate();
        }));

        addChip("Cantidad", R.drawable.ic_close, (v -> {
            resetChips();
            setFilterByDate(false);
            setFilterByQuantity(true);
            setFilterByPrice(false);
            sortByQuantity();
        }));

        addChip("Valor obtenido", R.drawable.ic_close, (v -> {
            resetChips();
            setFilterByDate(false);
            setFilterByQuantity(false);
            setFilterByPrice(true);
            sortByPrice();
        }));
    }

    // Crear un Chip y agregarlo al ChipGroup
    private void addChip(String text, int chipIconResId, View.OnClickListener onClickListener) {
        Chip chip = new Chip(chipGroup.getContext());
        chip.setText(text);
        chip.setChipIconVisible(false); // Para asegurarte de que inicialmente no se muestre el icono
        Drawable chipIcon = ContextCompat.getDrawable(chipGroup.getContext(), chipIconResId);
        chip.setChipIcon(chipIcon);
        chip.setOnClickListener(v -> {
            resetChips();
            chip.setChipIconVisible(true);
            onClickListener.onClick(v);
        });
        chipGroup.addView(chip);
    }

    public void resetChips() {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setChipIconVisible(false);
        }
    }

    // Establecer el filtro por fecha y notificar cambios
    public void setFilterByDate(boolean filterByDate) {
        notifyDataSetChanged();
    }

    // Establecer el filtro por cantidad y notificar cambios
    public void setFilterByQuantity(boolean filterByQuantity) {
        notifyDataSetChanged();
    }

    // Establecer el filtro por precio y notificar cambios
    public void setFilterByPrice(boolean filterByPrice) {
        notifyDataSetChanged();
    }

    // Ordenar la lista por fecha de forma descendente
    public void sortByDate() {
        categoryList.sort((category1, category2) -> category2.getDate().compareTo(category1.getDate()));
        notifyDataSetChanged();
    }

    // Ordenar la lista por cantidad de forma descendente
    public void sortByQuantity() {
        categoryList.sort((category1, category2) -> {
            double quantity1 = Double.parseDouble(category1.getQuantity());
            double quantity2 = Double.parseDouble(category2.getQuantity());

            // Compara las cantidades de forma descendente (de mayor a menor)
            return Double.compare(quantity2, quantity1);
        });
        notifyDataSetChanged();
    }

    // Ordenar la lista por precio de forma descendente
    public void sortByPrice() {
        categoryList.sort((category1, category2) -> {
            double quantity1 = Double.parseDouble(category1.getPrice());
            double quantity2 = Double.parseDouble(category2.getPrice());

            // Compara las cantidades de forma descendente (de mayor a menor)
            return Double.compare(quantity2, quantity1);
        });
        notifyDataSetChanged();
    }
}