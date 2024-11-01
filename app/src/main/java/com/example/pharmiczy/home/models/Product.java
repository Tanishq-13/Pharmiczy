package com.example.pharmiczy.home.models;

public class Product {
    private String imageUrl;
    private String title;
    private String description;
    private String price,op,disc;

    public Product(String imageUrl, String title, String description, String price,String op,String disc) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.price = price;
        this.op=op;
        this.disc=disc;
    }

    public String getImageUrl() { return imageUrl; }

    public String getDisc() {
        return disc;
    }

    public String getOp() {
        return op;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
}
