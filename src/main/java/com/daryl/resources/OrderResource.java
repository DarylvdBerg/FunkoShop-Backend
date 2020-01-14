package com.daryl.resources;

import com.daryl.api.User;
import com.daryl.service.OrderService;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    private OrderService orderService;

    public OrderResource() {
        this.orderService = new OrderService();
    }

    @Path("/create")
    @POST
    public Response createOrder(@Auth User authUser,
                                @FormParam("product_id") int product_id,
                                @FormParam("user_id") int user_id) {
       return orderService.createOrder(product_id, user_id);
    }

    @Path("/all")
    @GET
    public Response getAllOrders(@Auth User authUser) {
        return orderService.getAllOrders(authUser);
    }

    @Path("/{id}")
    @GET
    public Response getUserOrders(@Auth User authUser, @PathParam("id") int id) {
        return orderService.getUserOrders(authUser, id);
    }
}
