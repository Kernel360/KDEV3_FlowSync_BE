package com.checkping.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
                "com.checkping.api",
                "com.checkping.common",
                "com.checkping.service",
                "com.checkping.infra",
                "com.checkping.domain",
        }
        , exclude = {SecurityAutoConfiguration.class}
)
@EntityScan("com.checkping.domain")
@EnableJpaRepositories(basePackages = "com.checkping.infra.repository")
@EnableJpaAuditing
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
