package com.leanpay;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class LeanpayLoanApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeanpayLoanApplication.class, args);
    }

}
