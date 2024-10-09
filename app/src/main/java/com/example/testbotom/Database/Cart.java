package com.example.testbotom.Database;

public class Cart {
    private int cartId;
    private int userId;
    private String createdAt;  // Ngày tạo giỏ hàng

    // Constructor, getter và setter
    public Cart(int cartId, int userId, String createdAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
