package com.example.filesharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class FileSharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileSharingApplication.class, args);
    }

}
