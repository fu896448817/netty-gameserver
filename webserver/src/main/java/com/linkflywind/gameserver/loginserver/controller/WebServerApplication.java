package com.linkflywind.gameserver.loginserver.controller;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication(scanBasePackages="com.linkflywind.gameserver")
public class WebServerApplication {
    public static void main(String[] args){
        new SpringApplicationBuilder(WebServerApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
