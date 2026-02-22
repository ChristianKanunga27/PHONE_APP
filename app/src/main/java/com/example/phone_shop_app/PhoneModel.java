package com.example.phone_shop_app;

public class PhoneModel {
    public String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;

    public PhoneModel() { }

    public PhoneModel(String name, String description, double price, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}