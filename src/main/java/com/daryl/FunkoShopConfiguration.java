package com.daryl;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.nio.charset.StandardCharsets;

public class FunkoShopConfiguration extends Configuration {

    @NotNull
    private String jwtSecret;

    @NotNull
    private String uploadDir;

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("uploadDirectory")
    public String getUploadDir(){
        return uploadDir;
    }

    @JsonProperty("uploadDirectory")
    public void setUploadDir(String uploadDir){
        this.uploadDir = uploadDir;
    }

    @JsonProperty("jwtSecret")
    public byte[] getJwtSecret() {
        return jwtSecret.getBytes(StandardCharsets.UTF_8);
    }

    @JsonProperty("jwtSecret")
    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @JsonProperty("database")
    public DataSourceFactory getDatabase() {
        return database;
    }

    @JsonProperty("database")
    public void setDatabase(DataSourceFactory database) {
        this.database = database;
    }
}
