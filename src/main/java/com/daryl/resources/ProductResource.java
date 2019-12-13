package com.daryl.resources;

import com.daryl.api.User;
import com.daryl.service.ProductService;
import io.dropwizard.auth.Auth;
import jdk.internal.util.xml.impl.Input;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.NotEmpty;

import javax.print.attribute.standard.Media;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateProduct(@Auth User authUser, @PathParam("id") int id,
                                  @FormParam("name") @NotNull String name,
                                  @FormParam("description") @NotNull String description,
                                  @FormParam("price") @NotNull double price,
                                  @FormParam("amount") @NotNull int amount,
                                  @FormParam("image") @NotNull String image){
        return productService.updateProduct(authUser, id, name, description, image, price, amount);
    }

    @Path("/delete/{id}")
    @DELETE
    public Response deleteProduct(@Auth User authUser, @PathParam("id") int id){
        return productService.deleteProduct(authUser, id);
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addProduct(@Auth User authUser, @NotNull @FormDataParam("name") String name,
                               @NotNull @FormDataParam("description") String description,
                               @NotNull @FormDataParam("price") double price,
                               @NotNull @FormDataParam("amount") int amount,
                               @NotNull @FormDataParam("image") InputStream image,
                                @FormDataParam("image") FormDataContentDisposition imageDetail){
        return productService.addProduct(authUser, name, description, image, price, amount, imageDetail);
    }
}
