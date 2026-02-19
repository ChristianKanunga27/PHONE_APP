package com.example.phone_shop_app;

public class PhoneModel {
    public String id;         // Firestore document ID
    public String name;
    public String description;
    public double price;
    public String imageUrl;

    public PhoneModel() { } // Empty constructor required for Firestore

    public PhoneModel(String name, String description, double price, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
