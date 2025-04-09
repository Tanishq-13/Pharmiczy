package com.example.pharmiczy.DataModels;
public class CartItem {
    private Medicine medicine;
    private int quantity;
    private int price;

    public CartItem(Medicine medicine, int quantity, int price) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.price = price;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
