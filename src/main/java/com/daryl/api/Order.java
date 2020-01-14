package com.daryl.api;

import org.joda.time.DateTime;

import java.sql.Timestamp;

public class Order {
    public int id;
    public String userName;
    public String productName;
    public Timestamp orderDate;

    public Order() {}

    public Order(int id, String userName, String productName, Timestamp orderDate) {
        this.id = id;
        this.userName = userName;
        this.productName = productName;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Timestamp getOrder_date() {
        return orderDate;
    }

    public void setOrder_date(Timestamp order_date) {
        this.orderDate = order_date;
    }
}
