package com.example.filesharing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EditNameRequest {

    @JsonProperty("filename")
    private String newFilename;

    public String getNewFilename() {
        return newFilename;
    }

    public void setNewFilename(String newFilename) {
        this.newFilename = newFilename;
    }
}
