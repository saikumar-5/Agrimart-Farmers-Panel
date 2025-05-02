package com.example.FarmerAdminAgrimart;
public class Customer {
    private String name;
    private String phoneNumber;
    private double spentAmount;
    private int orderCount;
    private String memberSinceDate;

    public Customer(String name, String phoneNumber, double spentAmount, int orderCount, String memberSinceDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.spentAmount = spentAmount;
        this.orderCount = orderCount;
        this.memberSinceDate = memberSinceDate;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getSpentAmount() {
        return spentAmount;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public String getMemberSinceDate() {
        return memberSinceDate;
    }
}
