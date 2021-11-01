package com.example.filesharing.dto;


public class FileDtoGet {

    private byte[] file;

    public FileDtoGet(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
