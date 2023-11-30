package com.farmted.passservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.farmted.passservice.repository"})
public class PassServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PassServiceApplication.class, args);
    }

}