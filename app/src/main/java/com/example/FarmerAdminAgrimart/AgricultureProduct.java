package com.example.FarmerAdminAgrimart;

public class AgricultureProduct {
    private String id;
    private String name;
    private String category;
    private String price;
    private String location;
    private int instock;
    private String packagingType;
    private String shippingType;
    private String imageUrl;

    // Constructor without id and imageUrl (for new products)
    public AgricultureProduct()
    {

    }
    public AgricultureProduct(String name, String category, String price, String location,
                   int instock, String packagingType, String shippingType) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.location = location;
        this.instock = instock;
        this.packagingType = packagingType;
        this.shippingType = shippingType;
    }

    // Full constructor
    public AgricultureProduct(String id, String name, String category, String price, String location,
                   int instock, String packagingType, String shippingType, String imageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.location = location;
        this.instock = instock;
        this.packagingType = packagingType;
        this.shippingType = shippingType;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getInstock() {
        return instock;
    }

    public void setInstock(int instock) {
        this.instock = instock;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public void setPackagingType(String packagingType) {
        this.packagingType = packagingType;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}