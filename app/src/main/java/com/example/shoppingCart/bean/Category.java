package com.example.shoppingCart.bean;

public class Category {
    private int imageId;
    private String category;

    public Category() {

    }

    public Category(int imageId, String category) {
        this.imageId = imageId;
        this.category = category;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
