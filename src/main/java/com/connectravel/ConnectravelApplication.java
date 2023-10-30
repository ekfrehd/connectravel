package com.connectravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication (exclude = SecurityAutoConfiguration.class)
@EnableJpaAuditing//BaseEntity에 감시 기능을 사용하기 위해 감시 기능 활성화 명시
public class ConnectravelApplication {

    public static void main (String[] args) {
        SpringApplication.run (ConnectravelApplication.class, args);
    }

}
