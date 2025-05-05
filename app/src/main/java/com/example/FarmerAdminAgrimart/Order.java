package com.example.FarmerAdminAgrimart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private String customerName;
    private String phoneNumber;
    private String orderId;
    private double sellingPrice;
    private String orderDate;
    private String status;
    private int stepNumber;
    private String customerAddress;
    private double listPrice;
    private String deliveryAgentPhone;
    private List<OrderItem> items;

    public Order(String customerName, String phoneNumber, String orderId,
                 double sellingPrice, String orderDate, String status, int stepNumber) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.orderId = orderId;
        this.sellingPrice = sellingPrice;
        this.orderDate = orderDate;
        this.status = status;
        this.stepNumber = stepNumber;
        this.customerAddress = "123 Main St, Hyderabad, Telangana, 500001"; // Default address
        this.listPrice = sellingPrice * 1.2; // Default list price calculation
        this.deliveryAgentPhone = "9999999999"; // Default delivery agent phone
        this.items = new ArrayList<>();
    }

    // Getters and setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public double getListPrice() {
        return listPrice;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }

    public String getDeliveryAgentPhone() {
        return deliveryAgentPhone;
    }

    public void setDeliveryAgentPhone(String deliveryAgentPhone) {
        this.deliveryAgentPhone = deliveryAgentPhone;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void addItem(OrderItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
    }
}