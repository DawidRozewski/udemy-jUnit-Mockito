package com.example.udemyjunitmockito;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class UdemyJUnitMockitoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(UdemyJUnitMockitoApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}

