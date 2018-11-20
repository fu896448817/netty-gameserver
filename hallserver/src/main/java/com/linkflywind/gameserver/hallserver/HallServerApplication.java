package com.linkflywind.gameserver.hallserver;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages="com.linkflywind.gameserver")
@Configuration
@EnableAsync
public class HallServerApplication {

    @Value("${hallserver.name}")
    private String serverName;
}
