package com.daryl.resources;

import com.daryl.api.User;
import com.daryl.core.Body;
import com.daryl.service.UserService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
}
