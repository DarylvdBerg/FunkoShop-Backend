package com.daryl.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.daryl.FunkoShopApplication;
import com.daryl.api.User;
import com.daryl.core.Body;
import com.daryl.core.JwtHelper;
import com.daryl.db.UserDAO;
import com.daryl.util.MessageUtil;
import com.daryl.util.PrivilegeUtil;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import javax.ws.rs.core.Response;

public class UserService {
    private UserDAO userDAO;
    private final int COST = 12;

    public UserService(){
        userDAO = FunkoShopApplication.jdbiCon.onDemand(UserDAO.class);
        userDAO.createTable();
    }

    public Response login(String email, String password) {
        Body body = new Body();
        String pass = userDAO.getPasswordFromEmail(email);

        if(pass == null){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_PASS_INVALID_COMBI, null);
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), pass);
        if(!result.validFormat || !result.verified){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_PASS_INVALID_COMBI, null);
        }

        User user = userDAO.getUserFromEmail(email);
        body.setContent(user);
        body.setMessage(MessageUtil.LOGIN_OK);
        user.setAuthToken(JwtHelper.createAuthToken(user.getId()));
        return body.build();
    }

    public Response register(String email, String password, String name){
        Body body = new Body();
        if(!checkIfEmailIsValid(email)){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_NOT_VALID, null);
        }

        if(!checkPasswordLength(password)){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.PASSWORD_LENGHT_TO_SHORT, null);
        }

        name = trimName(name);
        String hashedPass = BCrypt.withDefaults().hashToString(COST, password.toCharArray());

        return createUser(email, name, hashedPass, body);
    }

    private Response createUser(String email, String name, String password, Body body) {
        try {
            int id = userDAO.createUser(email, password, name);
            User user = userDAO.getUserFromId(id);
            return Body.createResponse(body, Response.Status.OK, MessageUtil.ACCOUNT_CREATED, user);
        } catch (UnableToExecuteStatementException e) {
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_ALREADY_EXISTS, null);
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

    public Response getUser(User authUser, int id){
        Body body = new Body();

        if(checkUserId(authUser, id)){
            return Body.createResponse(body, Response.Status.NOT_FOUND, MessageUtil.USER_NOT_FOUND, null);
        }

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.CHECK_USER_PROFILE)){
            return Body.createResponse(body, Response.Status.UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        User user = userDAO.getUserFromId(id);
        if(user == null){
            return Body.createResponse(body, Response.Status.NOT_FOUND, MessageUtil.USER_NOT_FOUND, null);
        }
        body.setStatus(Response.Status.OK);
        body.setContent(user);
        return body.build();
    }

    public Response changeUserPassword(User authUser, int id, String password, String oldPassword) {
        Body body = new Body();
        if(checkUserId(authUser, id)){
            return Body.createResponse(body, Response.Status.NOT_FOUND, MessageUtil.USER_NOT_FOUND, null);
        }

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.CHANGE_PASSWORD)){
            return Body.createResponse(body, Response.Status.UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        if(!checkUserOldPassword(authUser, oldPassword)){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.PASSWORD_DO_NOT_MATCH, null);
        }

        String hashedPass = BCrypt.withDefaults().hashToString(COST, password.toCharArray());
        userDAO.updateUserPassword(hashedPass, id);

        return Body.createResponse(body, Response.Status.OK, MessageUtil.PASSWORD_UPDATED, null);
    }

    private boolean checkUserOldPassword(User authUser, String oldPassword){
        String password = userDAO.getPasswordFromEmail(authUser.getEmail());

        BCrypt.Result result = BCrypt.verifyer().verify(oldPassword.toCharArray(), password);
        return result.validFormat || result.verified;
    }

    private boolean checkUserId(User authUser, int id){
        return authUser.getId() != id;
    }

    public Response editUser(User authUser, int id, String email, String name) {
        Body body = new Body();

        if(checkUserId(authUser, id)){
            return Body.createResponse(body, Response.Status.NOT_FOUND, MessageUtil.USER_NOT_FOUND, null);
        }

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.UPDATE_USER_INFO)){
            return Body.createResponse(body, Response.Status.UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        if(!checkIfEmailIsValid(email)){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.EMAIL_NOT_VALID, null);
        }
        name = trimName(name);

        userDAO.updateUserDetails(email, name);

        return Body.createResponse(body, Response.Status.OK, MessageUtil.USER_UPDATED, null);
    }
}
