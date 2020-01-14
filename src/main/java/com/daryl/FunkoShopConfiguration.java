package com.daryl;

import com.daryl.config.ImageConfig;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.nio.charset.StandardCharsets;

public class FunkoShopConfiguration extends Configuration {

    @NotNull
    private String jwtSecret;

    @Valid
    @NotNull
    public ImageConfig imageConfig;

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

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

    @JsonProperty("images")
    public ImageConfig getImageConfig() {
        return this.imageConfig;
    }

    @JsonProperty("images")
    public void setImageConfig(ImageConfig imageConfig) {
        this.imageConfig = imageConfig;
    }

}
