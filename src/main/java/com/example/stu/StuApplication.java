package com.example.stu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StuApplication {
    public static void main(String[] args) {
        SpringApplication.run(StuApplication.class, args);
    }

}
