package com.example.phone_shop_app;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class OrderModel {
    private String orderId;
    private String phoneId;
    private String phoneName;
    private double price;
    private String userId;
    private String userEmail;
    private String userPhoneNumber; // Added field
    private @ServerTimestamp Date orderDate;

    public OrderModel() {
        // Required for Firestore
    }

    public OrderModel(String phoneId, String phoneName, double price, String userId, String userEmail, String userPhoneNumber) {
        this.phoneId = phoneId;
        this.phoneName = phoneName;
        this.price = price;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getPhoneId() { return phoneId; }
    public String getPhoneName() { return phoneName; }
    public double getPrice() { return price; }
    public String getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }
    public String getUserPhoneNumber() { return userPhoneNumber; } // Added getter
    public Date getOrderDate() { return orderDate; }

    // Setters
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setPhoneId(String phoneId) { this.phoneId = phoneId; }
    public void setPhoneName(String phoneName) { this.phoneName = phoneName; }
    public void setPrice(double price) { this.price = price; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setUserPhoneNumber(String userPhoneNumber) { this.userPhoneNumber = userPhoneNumber; } // Added setter
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
}
