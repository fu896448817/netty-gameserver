package com.linkflywind.gameserver.hallserver;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages="com.linkflywind.gameserver")
@Configuration
public class HallServerApplication {

    @Value("${hallserver.name}")
    private String serverName;
}
