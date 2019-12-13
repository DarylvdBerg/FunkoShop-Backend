package com.daryl.service;

import com.daryl.FunkoShopApplication;
import com.daryl.api.Product;
import com.daryl.api.User;
import com.daryl.core.Body;
import com.daryl.db.ProductDAO;
import com.daryl.util.MessageUtil;
import com.daryl.util.PrivilegeUtil;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.InputStream;
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
            return Body.createResponse(body, Response.Status.NOT_FOUND, MessageUtil.PRODUCT_NOT_FOUND, null);
        }

        return Body.createResponseWithHeader(body, OK, MessageUtil.PRODUCT_FOUND, product,
                HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", product.getImagePath()));
    }

    public Response getAllProducts(){
        Body body = new Body();
        List<Product> productList = productDAO.getAllProducts();
        return Body.createResponse(body, OK, MessageUtil.PRODUCT_FOUND, productList);
    }

    public Response updateProduct(User authUser, int id, String name, String description, String image, double price, int amount){
        Body body = new Body();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.UPDATE_PRODUCT)){
            return Body.createResponse(body, UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        try {
            boolean updated = productDAO.updateProduct(name, description, amount, image, price, id);
            return updated ? Body.createResponse(body, OK, MessageUtil.PRODUCT_UPDATED, null)
                    : Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
        } catch (UnableToExecuteStatementException e){
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_ALREADY_EXIST, null);
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
            boolean deleted = productDAO.deleteProduct(id);
            return deleted ? Body.createResponse(body, OK, MessageUtil.PRODUCT_DELETED, null)
                    : Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
        } catch (UnableToExecuteStatementException e){
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_NOT_FOUND, null);
        }
    }

    public Response addProduct(User authUser, String name, String description, InputStream image, double price, int amount, FormDataContentDisposition imageDetail) {
        Body body = new Body();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.ADD_PRODUCT)){
            return Body.createResponse(body, UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        try {
            String imagePath = handleFileUpload(image, imageDetail);
            if(imagePath != null){
                boolean added = productDAO.addProduct(name, description, imagePath, price, amount);
                return added ? Body.createResponse(body, OK, MessageUtil.PRODUCT_CREATED, null) :
                        Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
            } else {
                return Body.createResponse(body, INTERNAL_SERVER_ERROR, MessageUtil.SOMETHING_WENT_WRONG, null);
            }
        } catch (UnableToExecuteStatementException e){
            e.printStackTrace();
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_ALREADY_EXIST, null);
        }

    }

    private String handleFileUpload(InputStream file, FormDataContentDisposition imageDetail){
        return ImageService.getInstance().uploadImage(file, imageDetail);
    }
}
