package com.daryl.api;

public class Product {
    private int id;
    private String name;
    private String description;
    private int amount;

    public Product(int id, String name, String description, int amount){
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
