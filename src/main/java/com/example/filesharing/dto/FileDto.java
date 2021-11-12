package com.example.filesharing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class FileDto {

    private Long id;
    private String name;
    private long size;
    private String mimetype;
    private LocalDateTime lastedited;

}
