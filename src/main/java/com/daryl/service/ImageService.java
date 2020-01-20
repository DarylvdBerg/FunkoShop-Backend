package com.daryl.service;

import com.daryl.FunkoShopApplication;
import com.daryl.api.Product;
import com.daryl.api.User;
import com.daryl.config.ImageConfig;
import com.daryl.core.Body;
import com.daryl.api.Image;
import com.daryl.db.ImageDAO;
import com.daryl.db.ProductDAO;
import com.daryl.util.MessageUtil;
import com.daryl.util.PrivilegeUtil;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageService {
    private ImageDAO imageDAO;
    private ProductDAO productDAO;
    private String saveFolder;
    private final ArrayList<String> allowedMimeTypes;

    public ImageService(ImageConfig config){
        this.imageDAO = FunkoShopApplication.jdbiCon.onDemand(ImageDAO.class);
        this.productDAO = FunkoShopApplication.jdbiCon.onDemand(ProductDAO.class);
        this.saveFolder = config.getSaveFolder();
        this.allowedMimeTypes = config.getAllowedMimeTypes();
        imageDAO.createTable();
    }

    public Response getImage(int imageId){
        Body body = new Body();

        // Check if image exists in DB
        Image image = imageDAO.getImage(imageId);
        if(image == null){
            body.setStatus(Response.Status.NOT_FOUND);
            body.setMessage(MessageUtil.IMAGE_NOT_FOUND);

            return body.build();
        }

        // Check if image exists on disk, if yes send it else send NOT FOUND
        File imageRequested = new File(geImageAbsoluteDiskPath(image, image.getProductId()));
        if(imageRequested.exists()){
            try {
                return Response.ok(new FileInputStream(imageRequested))
                        .header(HttpHeaders.CONTENT_TYPE, image.getType())
                        .build();
            } catch(FileNotFoundException ignored){}
        }

        body.setStatus(Response.Status.NOT_FOUND);
        body.setMessage(MessageUtil.IMAGE_NOT_FOUND);
        return body.build();
    }

    public Response uploadImage(User authUser, int productId, FormDataMultiPart multiPart){
        Body body = new Body();

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.ADD_PRODUCT)){
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        List<Image> images = new ArrayList<>();
        List<FormDataBodyPart> parts = multiPart.getFields("images");
        for(FormDataBodyPart part : parts) {
            InputStream fileInputStream = part.getValueAs(InputStream.class);
            FormDataContentDisposition imageContent = part.getFormDataContentDisposition();

            Image image;
            try {
                image = imageUploadHelper(productId, part, fileInputStream, imageContent);
            } catch(NotFoundException e){
                return Body.createResponse(body, Response.Status.NOT_FOUND, e.getMessage(), null);
            } catch(ServerErrorException e){
                return Body.createResponse(body, Response.Status.INTERNAL_SERVER_ERROR, e.getMessage(), null);
            } catch(ForbiddenException e){
                return Body.createResponse(body, Response.Status.FORBIDDEN, e.getMessage(), null);
            }

            images.add(image);
        }
        return Body.createResponse(body, Response.Status.OK, MessageUtil.IMAGE_UPLOADED, images);
    }

    public Response deleteImage(User user, int imageId){
        Body body = new Body();

        Image image = imageDAO.getImage(imageId);
        if(imageDAO.deleteImage(imageId)){
            deleteImageOnDisk(geImageAbsoluteDiskPath(image, image.getProductId()));
            return Body.createResponse(body, Response.Status.OK, MessageUtil.IMAGE_DELETED, null);
        }
        return Body.createResponse(body, Response.Status.NOT_FOUND, MessageUtil.IMAGE_NOT_FOUND, null);
    }

    private void deleteImageOnDisk(String filePath) {
        File diskFile = new File(filePath);
        if (diskFile.exists()) {
            diskFile.delete();
        }
    }

    private Image imageUploadHelper(int productId, FormDataBodyPart imageBody,
                              InputStream uploadInputStream, FormDataContentDisposition details) {
        Product product = productDAO.getProduct(productId);
        if(product == null){
            throw new NotFoundException(MessageUtil.IMAGE_NOT_FOUND);
        }
        if(!isMimeTypeAllowed(imageBody.getMediaType().toString())){
            throw new ForbiddenException((MessageUtil.IMAGE_NOT_ALLOWED));
        }

        String newFileName = UUID.randomUUID() + getImageExtension(details.getFileName());
        Image image = new Image();
        image.setProductId(productId);
        image.setFileName(newFileName);
        image.setType(imageBody.getMediaType().toString());

        String savePath = geImageAbsoluteDiskPath(image, productId);
        if(writeToFile(uploadInputStream, savePath)){
            int imageId = imageDAO.saveFile(productId, image.getType(), image.getFileName());
            if(imageId == 0){
                throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR,
                        new Exception(MessageUtil.IMAGE_CANT_BE_SAVED));
            }
            image.setId(imageId);
        } else {
            throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR,
                    new Exception(MessageUtil.IMAGE_CANT_BE_SAVED));
        }

        return image;
    }

    private boolean isMimeTypeAllowed(String mimetype){
        return allowedMimeTypes.contains(mimetype);
    }

    private boolean writeToFile(InputStream uploadedFile, String location){
       File saveLocation = new File(location);
       System.out.println(saveLocation);
       if(!saveLocation.getParentFile().exists()){
           if(!saveLocation.getParentFile().mkdirs()){
               return false;
           }
       }

       try {
           int read;
           byte[] bytes = new byte[1024];

           OutputStream out = new FileOutputStream(saveLocation);
           while((read = uploadedFile.read(bytes)) != -1){
               out.write(bytes, 0 ,read);
           }
           out.flush();
           out.close();

           return true;
       } catch (IOException e){
           return false;
       }
    }

    private String getImageExtension(String image){
        int lastIndexOf = image.lastIndexOf(".");
        // Returns -1 if there is not extension found;
        return (lastIndexOf == -1) ? "" : image.substring(lastIndexOf);
    }

    private String geImageAbsoluteDiskPath(Image image, int productId){
        return String.format("%s%s/%s", saveFolder, productId, image.getDiskPath());
    }
}
