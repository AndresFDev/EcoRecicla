package com.example.ecorecicla.models;


public class EntryItem {
    private int id;
    private int userId;
    private String quantity;
    private String date;
    private String price;

    public EntryItem(int userId, String quantity, String date, String price) {
        this.id = id;
        this.userId = userId;
        this.quantity = quantity;
        this.date = date;
        this.price = price;
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
}