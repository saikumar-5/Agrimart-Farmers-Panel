package com.example.agrimart;

public class Product {
    private String name;
    private String quantity;
    private String price;
    private String imageUrl;

    public Product(String name, String quantity, String price, String imageUrl) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}