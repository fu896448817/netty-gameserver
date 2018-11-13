package com.linkflywind.gameserver.hall;


import com.linkflywind.gameserver.core.action.DispatcherAction;
import com.linkflywind.gameserver.hall.config.HallConfig;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.WebSocketCacheActorManager;
import com.linkflywind.gameserver.core.redisModel.ConnectorData;
import com.linkflywind.gameserver.core.redisModel.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@SpringBootApplication(scanBasePackages="com.linkflywind.gameserver")
@Configuration
public class HallApplication {

    private final WebSocketCacheActorManager webSocketCacheActorManager;

    private final ReactiveRedisOperations<String, UserSession> userSessionOps;

    private final HallConfig hallConfig;

    private final ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperationsByConnectorData;

    private final DispatcherAction dispatcherAction;

    @Value("${hall.name}")
    private String serverName;


    @Autowired
    public HallApplication(WebSocketCacheActorManager webSocketCacheActorManager, ReactiveRedisOperations<String, UserSession> userSessionOps, HallConfig hallConfig, ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperationsByConneecctorData, DispatcherAction dispatcherAction) {
        this.webSocketCacheActorManager = webSocketCacheActorManager;
        this.userSessionOps = userSessionOps;
        this.hallConfig = hallConfig;
        this.reactiveRedisOperationsByConnectorData = reactiveRedisOperationsByConneecctorData;
        this.dispatcherAction = dispatcherAction;
    }


    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/hall", new ReactiveWebSocketHandler(webSocketCacheActorManager,
                userSessionOps,
                hallConfig,
                reactiveRedisOperationsByConnectorData,
                serverName,
                dispatcherAction));

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
