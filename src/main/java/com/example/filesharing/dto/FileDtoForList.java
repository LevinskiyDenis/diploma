package com.example.filesharing.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;

public class FileDtoForList {

    private String name;
    private long size;
    private String mimetype;
    private LocalDateTime lastEdited;

    public FileDtoForList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public LocalDateTime getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(LocalDateTime lastEdited) {
        this.lastEdited = lastEdited;
    }

    @Override
    public String toString() {
        return "FileDtoForList{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", mimetype='" + mimetype + '\'' +
                ", lastEdited=" + lastEdited +
                '}';
    }
}
