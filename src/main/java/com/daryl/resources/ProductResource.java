package com.daryl.resources;

import com.daryl.api.User;
import com.daryl.service.ProductService;
import io.dropwizard.auth.Auth;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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
    @GET
    public Response getProduct(@PathParam("id") int id){
        return productService.getProduct(id);
    }

    @Path("/all")
    @GET
    public Response getAllProducts(){
        return productService.getAllProducts();
    }

    @Path("/update/{id}")
    @PUT
    public Response updateProduct(@Auth User authUser, @PathParam("id") int id,
                                  @FormParam("name") @NotNull String name, @FormParam("description") @NotNull String description,
                                  @FormParam("amount") @NotNull int amount){
        return productService.updateProduct(authUser, id, name, description, amount);
    }

    @Path("/delete/{id}")
    @DELETE
    public Response deleteProduct(@Auth User authUser, @PathParam("id") int id){
        return productService.deleteProduct(authUser, id);
    }
}
