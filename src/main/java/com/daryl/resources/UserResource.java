package com.daryl.resources;

import com.daryl.api.User;
import com.daryl.service.UserService;
import io.dropwizard.auth.Auth;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserService userService;

    public UserResource(){
        userService = new UserService();
    }

    @Path("/login")
    @POST
    public Response login(@FormParam("email") @NotNull String email,
                          @FormParam("password") @NotNull String password){

        return userService.login(email, password);
    }

    @Path("/register")
    @POST
    public Response register(@FormParam("email") @NotNull String email, @FormParam("name") @NotNull String name,
                             @FormParam("password") @NotNull String password){
        return userService.register(email, password, name);
    }

    @Path("/profile/{id}")
    @GET
    public Response getUserProfile(@Auth User authUser, @PathParam("id") int id){
        return userService.getUser(authUser, id);
    }

    @Path("/changePassword/{id}")
    @PUT
    public Response changeUserPassword(@Auth User authUser, @PathParam("id") int id, @FormParam("oldPassword") @NotNull String oldPassword,
                                       @FormParam("password") @NotNull String password){
        return userService.changeUserPassword(authUser, id, password, oldPassword);
    }

    @Path("/editUser/{id}")
    @PUT
    public Response editUser(@Auth User authUser, @PathParam("id") int id,
                             @FormParam("email") @NotNull String email, @FormParam("name") @NotNull String name){
        return userService.editUser(authUser, id, email, name);
    }
}
