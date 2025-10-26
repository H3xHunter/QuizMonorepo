package com.condocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MedianosServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedianosServiceApplication.class, args);
    }
}