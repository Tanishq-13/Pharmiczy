package com.example.pharmiczy.Apis.responses;

import com.example.pharmiczy.DataModels.CartItem;
import java.util.List;
// AllCartResponse.java
public class AllCartResponse {
    private String _id;
    private String userId;
    private List<CartResponse> items;
    private int totalPrice;

    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartResponse> getItems() {
        return items;
    }

    public void setItems(List<CartResponse> items) {
        this.items = items;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
