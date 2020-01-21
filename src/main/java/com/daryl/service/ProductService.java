package com.daryl.service;

import com.daryl.FunkoShopApplication;
import com.daryl.api.Product;
import com.daryl.api.User;
import com.daryl.core.Body;
import com.daryl.db.ImageDAO;
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
    private ImageDAO imageDAO;

    public ProductService(){
        productDAO = FunkoShopApplication.jdbiCon.onDemand(ProductDAO.class);
        imageDAO = FunkoShopApplication.jdbiCon.onDemand(ImageDAO.class);
        productDAO.createTable();
    }

    public Response getProduct(int id){
        Body body = new Body();
        Product product = productDAO.getProduct(id);

        if(product == null){
            return Body.createResponse(body, Response.Status.NOT_FOUND, MessageUtil.PRODUCT_NOT_FOUND, null);
        }

        fillProductImages(product);

        return Body.createResponse(body, OK, MessageUtil.PRODUCT_FOUND, product);
    }

    public Response getAllProducts(){
        Body body = new Body();
        List<Product> productList = productDAO.getAllProducts();
        for(Product product : productList){
            fillProductImages(product);
        }
        return Body.createResponse(body, OK, MessageUtil.PRODUCT_FOUND, productList);
    }

    public Response updateProduct(User authUser, int id, String name, String description, double price){
        Body body = new Body();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.UPDATE_PRODUCT)){
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        try {
            boolean updated = productDAO.updateProduct(name, description, price, id);
            return updated ? Body.createResponse(body, OK, MessageUtil.PRODUCT_UPDATED, null)
                    : Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
        } catch (UnableToExecuteStatementException e){
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_ALREADY_EXIST, null);
        }
    }

    public Response deleteProduct(User authUser, int id) {
        Body body = new Body();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.DELETE_PRODUCT)){
            Body.createResponse(body, BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
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

    public Response addProduct(User authUser, String name, String description, double price) {
        Body body = new Body();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.ADD_PRODUCT)){
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        try {
            int productId = productDAO.addProduct(name, description, price);
            return (productId != -1) ? Body.createResponse(body, OK, MessageUtil.PRODUCT_CREATED, productId):
                    Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_OPERATION_FAILED, null);
        } catch (UnableToExecuteStatementException e){
            e.printStackTrace();
            return Body.createResponse(body, BAD_REQUEST, MessageUtil.PRODUCT_ALREADY_EXIST, null);
        }

    }

    private void fillProductImages(Product product) {
        product.setImages(imageDAO.getImagesForProduct(product.getId()));
    }
}
