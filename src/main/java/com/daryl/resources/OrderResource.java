package com.daryl.resources;

import com.daryl.api.Cart;
import com.daryl.api.User;
import com.daryl.service.OrderService;
import io.dropwizard.auth.Auth;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    private OrderService orderService;

    public OrderResource() {
        this.orderService = new OrderService();
    }

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrder(@Auth User authUser, @Valid @NotNull List<Cart> cartItems) {
       return orderService.createOrder(authUser, cartItems);
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
