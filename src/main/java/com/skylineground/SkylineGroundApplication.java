package com.skylineground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SkylineGroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkylineGroundApplication.class, args);
    }

}
