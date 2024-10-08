package com.example.testbotom.Database;

public class Feedback {
    private int id;
    private String hoTen;
    private String sdt;
    private String email;
    private String binhLuan;

    // Constructor
    public Feedback(int id, String hoTen, String sdt, String email, String binhLuan) {
        this.id = id;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.binhLuan = binhLuan;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public String getEmail() {
        return email;
    }

    public String getBinhLuan() {
        return binhLuan;
    }
}
