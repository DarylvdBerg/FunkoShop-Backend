package com.daryl.service;

import com.daryl.FunkoShopApplication;
import com.daryl.api.User;
import com.daryl.api.UserAddress;
import com.daryl.core.Body;
import com.daryl.db.UserAddressDAO;
import com.daryl.util.MessageUtil;
import com.daryl.util.PrivilegeUtil;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import javax.ws.rs.core.Response;

public class UserAddressSerivce {
    private UserAddressDAO userAddressDAO;

    public UserAddressSerivce() {
        this.userAddressDAO = FunkoShopApplication.jdbiCon.onDemand(UserAddressDAO.class);
        this.userAddressDAO.createTable();
    }

    public Response createUserAddress(int userId, String streetAddress, String zipCode, String district) {
        Body body = new Body();
        try {
            boolean created = this.userAddressDAO.create(userId, streetAddress, zipCode, district);
            return created ? Body.createResponse(body, Response.Status.OK, MessageUtil.ADDRESS_CREATED, null) :
                    Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.ADDRESS_NOT_CREATED, null);
        } catch(UnableToExecuteStatementException e){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.SOMETHING_WENT_WRONG, null);
        }
    }

    public Response updateUserAddress(User authUser, int userId, String streetName, String zipCode, String district) {
        Body body = new Body();

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.UPDATE_USER_INFO)) {
            return Body.createResponse(body, Response.Status.UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        try {
            boolean updated = this.userAddressDAO.update(streetName, zipCode, district, userId);
            return updated ? Body.createResponse(body, Response.Status.OK, MessageUtil.ADDRESS_UPDATED, null) :
                    Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.ADDRESS_NOT_UPDATED, null);
        } catch(UnableToExecuteStatementException e) {
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.SOMETHING_WENT_WRONG, null);
        }
    }

    public Response getUserAddress(User authUser, int userId) {
        Body body = new Body();

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.CHECK_USER_PROFILE)) {
            return Body.createResponse(body, Response.Status.UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        try {
            UserAddress userAddress = this.userAddressDAO.getUserAddress(userId);
            return Body.createResponse(body, Response.Status.OK, MessageUtil.ADDRESS_FOUND, userAddress);
        } catch(UnableToExecuteStatementException e) {
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.SOMETHING_WENT_WRONG, null);
        }
    }
}
