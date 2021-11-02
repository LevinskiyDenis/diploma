package com.example.filesharing.dto;


public class FileDtoGet {

    private String mimetype;
    private byte[] file;

    public FileDtoGet() {
    }

    public FileDtoGet(byte[] file) {
        this.file = file;
    }

    public FileDtoGet(String mimetype, byte[] file) {
        this.mimetype = mimetype;
        this.file = file;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
