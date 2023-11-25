package com.example.ecorecicla;

public class BatteryTotalValues {
    private double totalQuantity;
    private double totalPrice;

    public BatteryTotalValues(double totalQuantity, double totalPrice) {
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
