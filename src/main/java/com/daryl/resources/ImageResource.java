package com.daryl.resources;

import com.daryl.api.User;
import com.daryl.service.ImageService;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/images")
@Produces(MediaType.APPLICATION_JSON)
public class ImageResource {

    private ImageService service;

    public ImageResource(ImageService imageService){
        this.service = imageService;
    }

    @GET
    @Path("/{id}")
    public Response getImage(@Auth Optional<User> optionalUser, @PathParam("id") int imageId) {
        return service.getImage(optionalUser, imageId);
    }

    @POST
    @Path("/upload/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImage(@Auth User authUser, @PathParam("id") int productId,
                                FormDataMultiPart multiPart) {
        return service.uploadImage(authUser, productId, multiPart);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteFile(@Auth User authUser, @PathParam("id") int imageId){
        return service.deleteImage(authUser, imageId);
    }
}
