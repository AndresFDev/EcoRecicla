package com.example.ecorecicla.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Stats {
    private Map<String, CategoryStats> categoryStats;
    private String maxCategory;

    public Stats() {
        this.categoryStats = new HashMap<>();
    }

    public void addCategoryStats(String category, double quantity, double price) {
        CategoryStats stats = new CategoryStats(category, quantity, price);
        categoryStats.put(category, stats);
    }

    public CategoryStats getCategoryStats(String category) {
        return categoryStats.get(category);
    }

    public Set<String> getCategories() {
        return categoryStats.keySet();
    }
    public String getCategoryForPosition(int position) {
        // Convierte las categorías a una lista para acceder por posición
        List<String> categoryList = new ArrayList<>(categoryStats.keySet());
        if (position >= 0 && position < categoryList.size()) {
            return categoryList.get(position);
        }
        return null;
    }

    public void setMaxCategory(String maxCategory) {
        this.maxCategory = maxCategory;
    }

    public String getMaxCategory() {
        return maxCategory;
    }

    public class CategoryStats {
        private String category;
        private double totalQuantity;
        private double totalPrice;

        public CategoryStats(String category, double totalQuantity, double totalPrice) {
            this.category = category;
            this.totalQuantity = totalQuantity;
            this.totalPrice = totalPrice;
        }

        public String getCategory() {
            return category;
        }
        public double getTotalQuantity() {
            return totalQuantity;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }

}