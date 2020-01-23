package com.daryl.api;

public class UserAddress {
    private int id;
    private int userId;
    private String streetAddress;
    private String zipCode;
    private String district;

    public UserAddress() {}

    public UserAddress(int id, int userId, String streetAdres, String zipCode, String district) {
        this.id = id;
        this.userId = userId;
        this.streetAddress = streetAdres;
        this.zipCode = zipCode;
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStreetAdres() {
        return streetAddress;
    }

    public void setStreetAdres(String streetAdres) {
        this.streetAddress = streetAdres;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

}

