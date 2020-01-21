package com.daryl.api;

import java.util.ArrayList;

public class Product {
    private int id;
    private String name;
    private String description;
    private ArrayList<Image> images;
    private String price;

    public Product(int id, String name, String description, String price){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(){

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

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
