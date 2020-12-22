package com.example.shopingonline;

public class Product {

    private String imageName;
    private String imageURL;

    public Product(String name, String url) {
        this.setImageName(name);
        this.setImageURL(url);
    }

    public Product() {

    }


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
