package com.daryl.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.daryl.FunkoShopApplication;
import com.daryl.api.User;
import com.daryl.core.Body;
import com.daryl.core.JwtHelper;
import com.daryl.db.UserDAO;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import javax.ws.rs.core.Response;

public class UserService {
    private UserDAO userDAO;
    private final int COST = 12;

    public UserService(){
        userDAO = FunkoShopApplication.jdbiCon.onDemand(UserDAO.class);
        userDAO.createTable();
    }

    // LOGIN
    public Response login(String email, String password) {
        Body body = new Body();
        String pass = userDAO.getPasswordFromEmail(email);

        if(pass == null){
            return createResponse(body, Response.Status.BAD_REQUEST, MessageService.EMAIL_PASS_INVALID_COMBI, null);
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), pass);
        if(!result.validFormat || !result.verified){
            return createResponse(body, Response.Status.BAD_REQUEST, MessageService.EMAIL_PASS_INVALID_COMBI, null);
        }

        User user = userDAO.getUserFromEmail(email);
        body.setContent(user);
        body.setMessage(MessageService.LOGIN_OK);
        user.setAuthToken(JwtHelper.createAuthToken(user.getId()));
        return body.build();
    }

    // REGISTER
    public Response register(String email, String password, String name){
        Body body = new Body();
        if(!checkIfEmailIsValid(email)){
            return createResponse(body, Response.Status.BAD_REQUEST, MessageService.EMAIL_NOT_VALID, null);
        }

        if(!checkPasswordLength(password)){
            return createResponse(body, Response.Status.BAD_REQUEST, MessageService.PASSWORD_LENGHT_TO_SHORT, null);
        }

        name = trimName(name);
        String hashedPass = BCrypt.withDefaults().hashToString(COST, password.toCharArray());

        return createUser(email, name, hashedPass, body);
    }

    private Response createUser(String email, String name, String password, Body body) {
        try {
            int id = userDAO.createUser(email, password, name);
            User user = userDAO.getUserFromId(id);
            return createResponse(body, Response.Status.OK, MessageService.ACCOUNT_CREATED, user);
        } catch (UnableToExecuteStatementException e) {
            return createResponse(body, Response.Status.BAD_REQUEST, MessageService.EMAIL_ALREADY_EXISTS, null);
        }
    }

    private boolean checkIfEmailIsValid(String email){
        return email.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    }

    private boolean checkPasswordLength(String password){
        return password.length() >= 6;
    }

    private String trimName(String name){
        name = name.trim();
        name = name.replace("<", "&lt;");
        name = name.replace(">", "&gt;");
        return name;
    }

    private Response createResponse(Body body, Response.Status status, String message, Object content){
        if(content != null){
            body.setContent(content);
        }
        body.setStatus(status);
        body.setMessage(message);
        return body.build();
    }
}
