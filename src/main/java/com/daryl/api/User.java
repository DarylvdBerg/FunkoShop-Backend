package com.daryl.api;

import java.security.Principal;

public class User implements Principal {

    private int id;
    private String name;
    private String authToken;
    private String email;
    private int privilege;

    public User(int id, String name, String email, int privilege) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.privilege = privilege;
    }

    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    @Override
    public String getName() {
        return null;
    }
}
