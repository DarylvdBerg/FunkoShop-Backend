package com.daryl.service;

import com.daryl.FunkoShopApplication;
import com.daryl.core.Body;
import com.daryl.db.UserDAO;

import javax.ws.rs.core.Response;

public class UserService {
    private UserDAO userDAO;

    public UserService(){
        userDAO = FunkoShopApplication.jdbiCon.onDemand(UserDAO.class);
        userDAO.createTable();
    }

    public Response login(String email, String password) {
        Body body = new Body();

        return body.build();
    }
}
