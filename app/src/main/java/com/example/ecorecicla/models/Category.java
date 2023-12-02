package com.example.ecorecicla.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Category {
    private int id;
    private int userId;
    private String quantity;
    private String date;
    private String price;
    private String categoryName;

    public Category(int id, int userId, String quantity, String date, String price, String categoryName) {
        this.id = id;
        this.userId = userId;
        this.quantity = quantity;
        this.date = date;
        this.price = price;
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void updateFrom(Category other) {
        if (other != null) {
            this.id = other.id;
            this.userId = other.userId;
            this.quantity = other.quantity;
            this.date = other.date;
            this.price = other.price;
        }
    }
}
