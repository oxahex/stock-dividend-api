package com.oxahex.stockdividendapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockDividendApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockDividendApiApplication.class, args);
    }
}