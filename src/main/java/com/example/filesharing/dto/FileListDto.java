package com.example.filesharing.dto;


public class FileListDto {

    private String filename;
    private int size;

    public FileListDto() {
    }

    public FileListDto(String filename, int size) {
        this.filename = filename;
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
