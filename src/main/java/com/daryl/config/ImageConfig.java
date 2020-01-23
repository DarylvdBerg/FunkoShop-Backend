package com.daryl.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class ImageConfig {
    @NotNull
    private String saveFolder;

    @NotNull
    private ArrayList<String> allowedMimeTypes;

    @JsonProperty("saveFolder")
    public String getSaveFolder() {
        return saveFolder;
    }

    @JsonProperty("saveFolder")
    public void setSaveFolder(String saveFolder){
        this.saveFolder = saveFolder;
    }

    @JsonProperty("allowedMimeTypes")
    public ArrayList<String> getAllowedMimeTypes() {
        return this.allowedMimeTypes;
    }

    @JsonProperty("allowedMimeTypes")
    public void setAllowedMimeTypes(ArrayList<String> allowedMimeTypes){
        this.allowedMimeTypes = allowedMimeTypes;
    }
}
