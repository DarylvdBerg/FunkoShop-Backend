package com.daryl.service;

import com.daryl.FunkoShopApplication;
import com.daryl.api.Product;
import com.daryl.api.User;
import com.daryl.core.Body;
import com.daryl.db.ProductDAO;
import com.daryl.util.MessageUtil;
import com.daryl.util.PrivilegeUtil;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import javax.ws.rs.core.Response;

import java.util.List;

import static javax.ws.rs.core.Response.Status.*;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService(){
        productDAO = FunkoShopApplication.jdbiCon.onDemand(ProductDAO.class);
        productDAO.createTable();
    }

    public Response getProduct(int id){
        Body body = new Body();
        Product product = productDAO.getProduct(id);

        if(product == null){
            Body.createResponse(body, Response.Status.NOT_FOUND, MessageUtil.PRODUCT_NOT_FOUND, null);
        }

        body.setStatus(OK);
        body.setMessage(MessageUtil.PRODUCT_FOUND);
        body.setContent(product);

        return body.build();
    }

    public Response getAllProducts(){
        Body body = new Body();
        List<Product> productList = productDAO.getAllProducts();

        body.setStatus(OK);
        body.setContent(productList);

        return body.build();
    }

    public Response updateProduct(User authUser, int id, String name, String description, int amount){
        Body body = new Body();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.UPDATE_PRODUCT)){
            return Body.createResponse(body, UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        try {
            productDAO.updateProduct(name, description, amount, id);
            body.setStatus(OK);
            body.setMessage(MessageUtil.PRODUCT_UPDATED);
            return body.build();
        } catch (UnableToExecuteStatementException e){
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
        }
    }

    public Response deleteProduct(User authUser, int id) {
        Body body = new Body();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.DELETE_PRODUCT)){
            Body.createResponse(body, UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        Product product = productDAO.getProduct(id);
        if(product == null){
            Body.createResponse(body, NOT_FOUND, MessageUtil.PRODUCT_NOT_FOUND, null);
        }

        try {
            productDAO.deleteProduct(id);
            body.setStatus(OK);
            body.setMessage(MessageUtil.PRODUCT_DELETED);
            return body.build();
        } catch (UnableToExecuteStatementException e){
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
        }
    }

    public Response addProduct(User authUser, String name, String description, int amount) {
        Body body = new Body();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.ADD_PRODUCT)){
            return Body.createResponse(body, UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        try {
            boolean added = productDAO.addProduct(name, description, amount);
            return added ? Body.createResponse(body, OK, MessageUtil.PRODUCT_CREATED, null) :
                    Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
        } catch (UnableToExecuteStatementException e){
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_ALREADY_EXIST, null);
        }

    }
}
