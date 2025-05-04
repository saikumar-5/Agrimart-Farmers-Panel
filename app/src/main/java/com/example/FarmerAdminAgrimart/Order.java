package com.example.FarmerAdminAgrimart;

/**
 * Model class representing an order in the application
 */
public class Order {
    private String customerName;
    private String phoneNumber;
    private String orderId;
    private double amount;
    private String date;
    private String status;
    private int currentStep;

    /**
     * Constructor for Order
     *
     * @param customerName The name of the customer
     * @param phoneNumber The phone number of the customer
     * @param orderId The unique identifier for the order
     * @param amount The total amount of the order
     * @param date The date when the order was placed
     * @param status The current status of the order (text description)
     * @param currentStep The current step in the order progress (0-4)
     *                    0 = Confirmed
     *                    1 = Picked Up
     *                    2 = Shipped
     *                    3 = Out for Delivery
     *                    4 = Delivered
     */
    public Order(String customerName, String phoneNumber, String orderId,
                 double amount, String date, String status, int currentStep) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.orderId = orderId;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.currentStep = currentStep;
    }

    // Getters
    public String getCustomerName() {
        return customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    // Setters
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    // Helper method to update both status and currentStep together
    public void updateOrderStatus(String status, int currentStep) {
        this.status = status;
        this.currentStep = currentStep;
    }
}