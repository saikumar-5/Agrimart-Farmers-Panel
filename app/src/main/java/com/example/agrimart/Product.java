package com.example.agrimart;

public class Product {
    private String name;
    private String quantity;
    private String price;
    private int imageResId; // drawable resource id

    public Product(String name, String quantity, String price, int imageResId) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getQuantity() { return quantity; }
    public String getPrice() { return price; }
    public int getImageResId() { return imageResId; }
}
