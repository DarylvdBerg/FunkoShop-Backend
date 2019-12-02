package com.daryl.resources;

import com.daryl.service.ProductService;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    private ProductService productService;

    public ProductResource(){
        this.productService = new ProductService();
    }

    @Path("/{id}")
    public Response getProduct(@PathParam("id") int id){
        return productService.getProduct(id);
    }

    @Path("/all")
    public Response getAllProducts(){
        return productService.getAllProducts();
    }


}
