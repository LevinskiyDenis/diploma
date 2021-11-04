package com.example.filesharing.dto;

import lombok.*;

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
