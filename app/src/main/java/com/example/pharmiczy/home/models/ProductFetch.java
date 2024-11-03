package com.example.pharmiczy.home.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ProductFetch implements Serializable {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private double price;

    @SerializedName("category")
    private String category;

    @SerializedName("stock")
    private int stock;

    @SerializedName("image")
    private String image;

    @SerializedName("createdAt")
    private String createdAt;

    // Add originalPrice and discount
    @SerializedName("original_price")
    private double original_price;  // Ensure your API provides this

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public int getStock() { return stock; }
    public String getImage() { return image; }
    public String getCreatedAt() { return createdAt; }
    public double getOriginalPrice() { return original_price; }

//     Calculate the discount based on price and original price
    public int getDiscount() {
        if (original_price > price) {
            return (int) ((1 - (price / original_price)) * 100);
        }
        return 0;
    }
}
