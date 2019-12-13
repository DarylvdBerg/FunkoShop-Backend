package com.daryl.service;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.*;
import java.util.UUID;

public class ImageService {

    public static ImageService instance;
    private String dir;


    public static ImageService getInstance(){
        return instance;
    }

    public ImageService(String dir){
        this.dir = dir;
        instance = this;
    }

    public String uploadImage(InputStream file, FormDataContentDisposition fileDetail){
        String extension = getExtension(fileDetail.getFileName());
        if(extension.equals(".png") || extension.equals(".jpg")){
            String filePath = dir+"product_"+UUID.randomUUID().toString()+"/";
            File saveLocation = new File(filePath);
            if(!saveLocation.exists()){
                if(!saveLocation.mkdirs()){
                    return null;
                }
            }
            String savePath = filePath + fileDetail.getFileName();
            boolean uploaded = saveFile(file, savePath);
            return uploaded ? savePath : null;
        } else {
            return null;
        }
    }

    private boolean saveFile(InputStream uploadedFile, String location){
        try {
            int read;
            byte[] bytes = new byte[1024];

            OutputStream out = new FileOutputStream(new File(location));
            while ((read = uploadedFile.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i);
        }

        return extension;
    }
}
