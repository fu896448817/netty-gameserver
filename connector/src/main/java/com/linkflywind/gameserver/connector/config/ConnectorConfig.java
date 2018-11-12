package com.linkflywind.gameserver.connector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix="connector")
public class ConnectorConfig {

    private Map<String, String> routes = new HashMap<>(); //接收prop1里面的属性值


    public Map<String, String> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, String> routes) {
        this.routes = routes;
    }
}
