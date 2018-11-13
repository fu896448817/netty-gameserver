package com.linkflywind.gameserver.hall;

import com.linkflywind.gameserver.core.action.DispatcherAction;
import com.linkflywind.gameserver.hall.config.HallConfig;
import com.linkflywind.gameserver.core.network.websocket.AbsReactiveWebSocketHandler;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.WebSocketCacheActorManager;
import com.linkflywind.gameserver.core.redisModel.ConnectorData;
import com.linkflywind.gameserver.core.redisModel.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;

import java.io.IOException;

public class ReactiveWebSocketHandler extends AbsReactiveWebSocketHandler {

    private HallConfig hallConfig;

    private String serverName;

    private DispatcherAction dispatcherAction;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ReactiveWebSocketHandler(WebSocketCacheActorManager webSocketCacheActorManager,
                                    ReactiveRedisOperations<String, UserSession> reactiveRedisOperations,
                                    HallConfig hallConfig,
                                    ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperationsByConneecctorData,
                                    String serverName,
                                    DispatcherAction dispatcherAction) {
        super(webSocketCacheActorManager, reactiveRedisOperations, reactiveRedisOperationsByConneecctorData);
        this.hallConfig = hallConfig;
        this.serverName = serverName;
        this.dispatcherAction = dispatcherAction;
    }

    @Override
    protected void doHandle(ConnectorData connectorData) {
        String channelName = this.hallConfig.getRoutes().get(connectorData.getChannel());

        if(channelName.equals(serverName)){
            this.dispatcherAction.createAction(connectorData.getProtocol()).ifPresent(p-> {
                try {
                    p.action(connectorData);
                } catch (IOException e) {
                    logger.error("action error ",e);
                }
            });
        }
    }
}
