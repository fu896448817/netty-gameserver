package com.linkflywind.gameserver.logicserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication(scanBasePackages="com.linkflywind.gameserver")
@EnableAsync
public class YingSanZhangServerApplication {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static void main(String[] args) {
        SpringApplication.run(YingSanZhangServerApplication.class,args);
    }
}
