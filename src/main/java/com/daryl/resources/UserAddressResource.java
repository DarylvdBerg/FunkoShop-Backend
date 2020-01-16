package com.daryl.resources;

import com.daryl.api.User;
import com.daryl.service.UserAddressSerivce;
import io.dropwizard.auth.Auth;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/address")
@Produces(MediaType.APPLICATION_JSON)
public class UserAddressResource {
    private UserAddressSerivce userAddressSerivce;

    public UserAddressResource() {
        this.userAddressSerivce = new UserAddressSerivce();
    }

    @Path("/create")
    @POST
    public Response createUserAddress(@FormParam("user_id") @NotNull int userId,
                                      @FormParam("street_address") @NotNull String streetName,
                                      @FormParam("zip_code") @NotNull String zipCode,
                                      @FormParam("district") @NotNull String district) {
        return this.userAddressSerivce.createUserAddress(userId, streetName, zipCode, district);
    }

    @Path("/update/{id}")
    @PUT
    public Response updateUserAddress(@Auth User authUser,
                                      @PathParam("id") int userId,
                                      @FormParam("street_name") String streetName,
                                      @FormParam("zip_code") String zipCode,
                                      @FormParam("district") String district){
        return this.userAddressSerivce.updateUserAddress(authUser, userId, streetName, zipCode, district);
    }

    @Path("/{id}")
    @GET
    public Response getUserAddress(@Auth User authUser, @PathParam("id") int userId) {
        return this.userAddressSerivce.getUserAddress(authUser, userId);
    }

}
