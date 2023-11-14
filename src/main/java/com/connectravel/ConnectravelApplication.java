package com.connectravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.connectravel.domain.entity")
@EnableJpaRepositories(basePackages = "com.connectravel.repository")
public class ConnectravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectravelApplication.class, args);
    }

}