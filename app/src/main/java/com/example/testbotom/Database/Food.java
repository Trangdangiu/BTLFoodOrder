package com.example.testbotom.Database;

import android.net.Uri;

public class Food {
    private int id;
    private String name;
    private int price;
    private String image;
    private String description;

    public Food(int id, String name, int price, String image, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
