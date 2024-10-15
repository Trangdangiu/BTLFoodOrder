package com.example.testbotom.Database;

public class OrderItem {
    private int orderItemId;       // order_item_id
    private String orderCode;       // order_code
    private String fullName;        // full_name
    private String phoneNumber;     // phone_number
    private String address;         // address
    private String menu;            // menu
    private String orderDate;       // order_date
    private double totalAmount;      // total_amount
    private String paymentMethod;    // payment_method
    private boolean isDelivery;      // isDelivery (trạng thái checkbox giao hàng)

    // Constructor
    public OrderItem(int orderItemId, String orderCode, String fullName, String phoneNumber,
                     String address, String menu, String orderDate, double totalAmount,
                     String paymentMethod, boolean isDelivery) {
        this.orderItemId = orderItemId;
        this.orderCode = orderCode;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.menu = menu;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.isDelivery = isDelivery;  // Khởi tạo trạng thái giao hàng
    }

    // Getters and Setters
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isDelivery() {
        return isDelivery;
    }

    public void setDelivery(boolean delivery) {
        isDelivery = delivery;
    }
}
