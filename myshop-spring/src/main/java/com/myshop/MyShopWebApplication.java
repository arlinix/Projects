package com.myshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.myshop")
public class MyShopWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyShopWebApplication.class, args);
    }
}
