package com.example.sirmafinalexam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SirmaFinalExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SirmaFinalExamApplication.class, args);
    }
}
