package com.daryl.service;

import com.daryl.FunkoShopApplication;
import com.daryl.api.Product;
import com.daryl.core.Body;
import com.daryl.db.ProductDAO;
import com.daryl.util.MessageUtil;

import javax.ws.rs.core.Response;

import java.util.List;

import static javax.ws.rs.core.Response.Status.OK;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService(){
        productDAO = FunkoShopApplication.jdbiCon.onDemand(ProductDAO.class);
        productDAO.createTable();
    }

    public Response getProduct(int id){
        Body body = new Body();
        Product product = productDAO.getProduct(id);

        if(!checkProductId(id, product.getId())){
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

    private boolean checkProductId(int idRecieved, int idDb){
        return idRecieved == idDb;
    }
}
