package com.daryl.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Image {
    private int id;
    private int productId;
    private String fileName;
    private String type;

    public Image() {}

    public Image(int id, int productId, String fileName, String type){
        this.id = id;
        this.productId = productId;
        this.fileName = fileName;
        this.type = type;
    }

    @JsonIgnore
    public String getDiskPath() {
        return String.format("%s%s", productId, fileName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
