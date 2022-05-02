package com.github.mwierzchowski.weather.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Config {
    public static void main(String[] args) {
        SpringApplication.run(Config.class, args);
    }
}
