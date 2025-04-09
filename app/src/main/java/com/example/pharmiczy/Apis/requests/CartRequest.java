package com.example.pharmiczy.Apis.requests;

public class CartRequest {
    private String medicineId;
    private int quantity;

    public CartRequest(String medicineId, int quantity) {
        this.medicineId = medicineId;
        this.quantity = quantity;
    }
}
