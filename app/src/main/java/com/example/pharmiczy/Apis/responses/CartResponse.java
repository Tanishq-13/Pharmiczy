package com.example.pharmiczy.Apis.responses;

import com.example.pharmiczy.DataModels.Medicine;

import java.util.List;
// CartResponse.java
public class CartResponse {
    private Medicine medicineId;
    private int quantity;
    private int price;
    private String _id;

    // Getters and Setters
    public Medicine getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Medicine medicineId) {
        this.medicineId = medicineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
