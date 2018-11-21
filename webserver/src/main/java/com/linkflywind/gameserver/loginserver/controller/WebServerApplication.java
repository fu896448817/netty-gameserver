package com.linkflywind.gameserver.loginserver.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication(scanBasePackages =
        {"com.linkflywind.gameserver.loginserver",
                "com.linkflywind.gameserver.data"
        })
@EnableMongoRepositories("com.linkflywind.gameserver.data")
public class WebServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class);
    }
}
