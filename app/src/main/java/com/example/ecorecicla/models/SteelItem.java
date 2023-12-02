package com.example.ecorecicla.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SteelItem implements Parcelable{
    private int id;
    private int userId;
    private String quantity;
    private String date;
    private String price;
    private String subCategory;

    public SteelItem(int id, int userId, String quantity, String date, String price, String subCategory) {
        this.id = id;
        this.userId = userId;
        this.quantity = quantity;
        this.date = date;
        this.price = price;
        this.subCategory = subCategory;
    }

    protected SteelItem(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        quantity = in.readString();
        date = in.readString();
        price = in.readString();
        subCategory = in.readString();
    }

    public static final Parcelable.Creator<SteelItem> CREATOR = new Parcelable.Creator<SteelItem>() {
        @Override
        public SteelItem createFromParcel(Parcel in) {
            return new SteelItem(in);
        }

        @Override
        public SteelItem[] newArray(int size) {
            return new SteelItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(quantity);
        dest.writeString(date);
        dest.writeString(price);
        dest.writeString(subCategory);
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

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
}
