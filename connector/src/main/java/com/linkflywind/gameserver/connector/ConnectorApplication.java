package com.linkflywind.gameserver.connector;


import com.linkflywind.gameserver.connector.redisModel.ConnectorData;
import com.linkflywind.gameserver.connector.webSocketCache.WebSocketCacheActorManager;
import com.linkflywind.gameserver.connector.config.ConnectorConfig;
import com.linkflywind.gameserver.connector.redisModel.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Configuration
public class ConnectorApplication {

    private final WebSocketCacheActorManager webSocketCacheActorManager;

    private final ReactiveRedisOperations<String, UserSession> userSessionOps;

    private final ConnectorConfig connectorConfig;

    private final ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperationsByConnectorData;

    @Autowired
    public ConnectorApplication(WebSocketCacheActorManager webSocketCacheActorManager, ReactiveRedisOperations<String, UserSession> userSessionOps, ConnectorConfig connectorConfig, ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperationsByConneecctorData) {
        this.webSocketCacheActorManager = webSocketCacheActorManager;
        this.userSessionOps = userSessionOps;
        this.connectorConfig = connectorConfig;
        this.reactiveRedisOperationsByConnectorData = reactiveRedisOperationsByConneecctorData;
    }


    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/connector", new ReactiveWebSocketHandler(webSocketCacheActorManager, userSessionOps, connectorConfig, reactiveRedisOperationsByConnectorData));

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        mapping.setOrder(-1); // before annotated controllers
        return mapping;
    }


    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
