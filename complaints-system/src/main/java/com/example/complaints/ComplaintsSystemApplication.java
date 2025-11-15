package com.example.complaints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ComplaintsSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComplaintsSystemApplication.class, args);
    }
}
