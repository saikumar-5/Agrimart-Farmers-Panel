package com.example.FarmerAdminAgrimart;

public class Product {
    private String name;
    private String quantity;
    private String price;
    private int imageResId;

    public Product(String name, String quantity, String price, int imageResId) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.imageResId = imageResId;
    }

    // Constructor to create from AgricultureProduct
    public Product(AgricultureProduct agricultureProduct) {
        this.name = agricultureProduct.getName();
        this.quantity = agricultureProduct.getPackagingType();
        this.price = "₹" + agricultureProduct.getPrice() + "/kg";
        this.imageResId = R.drawable.tomatoes; // Default image
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

    public int getImageResId() {
        return imageResId;
    }
}