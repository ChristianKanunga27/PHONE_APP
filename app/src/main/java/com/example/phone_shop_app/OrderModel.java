package com.example.phone_shop_app;

import java.util.Date;

public class OrderModel {
    private String phoneId;
    private String phoneName;
    private double price;
    private String userId;
    private String userEmail;
    private String userPhoneNumber;
    private String orderId;
    private Date orderDate;

    public OrderModel() {
        // Default constructor required for calls to DataSnapshot.getValue(OrderModel.class)
    }

    public OrderModel(String phoneId, String phoneName, double price, String userId, String userEmail, String userPhoneNumber) {
        this.phoneId = phoneId;
        this.phoneName = phoneName;
        this.price = price;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.orderDate = new Date(); // Set the order date to the current time
    }

    // Getters and Setters
    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
