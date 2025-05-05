package com.example.FarmerAdminAgrimart;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private String name;
    private double price;
    private int quantity;
    private int imageResourceId;

    public OrderItem(String name, double price, int quantity, int imageResourceId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}