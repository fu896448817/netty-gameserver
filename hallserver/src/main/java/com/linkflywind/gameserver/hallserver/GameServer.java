package com.linkflywind.gameserver.hallserver;


import com.linkflywind.gameserver.core.action.DispatcherAction;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocket;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.WebSocketCacheActorManager;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.hallserver.config.HallConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.ServerEndpoint;

import java.io.IOException;
import java.util.Optional;

@Component
@ServerEndpoint
public class GameServer extends GameWebSocket {

    private final DispatcherAction dispatcherAction;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HallConfig hallConfig;

    private final RedisTemplate redisTemplate;

    @Autowired
    public GameServer(WebSocketCacheActorManager webSocketCacheActorManager, DispatcherAction dispatcherAction, HallConfig hallConfig, RedisTemplate redisTemplate) {
        super(webSocketCacheActorManager);
        this.dispatcherAction = dispatcherAction;
        this.hallConfig = hallConfig;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected boolean receiveHandle(GameWebSocketSession session, int channel, int protocol, Optional<byte[]> buffer) {
        String channelString = hallConfig.getRoutes().get(String.valueOf(channel));
        if(channelString.equals(hallConfig.getName())){
            dispatcherAction.createAction(protocol);
        }
        else
        {
            this.redisTemplate.convertAndSend(channelString,new TransferData(session,channelString,protocol,buffer));
        }
        return true;
    }

    @Override
    protected void openHandle(GameWebSocketSession session) {
        dispatcherAction.createAction(1001).ifPresent(p-> {
            try {
                p.action(new TransferData(session,"",1001,Optional.empty()));
            } catch (IOException e) {
                logger.error("action error ",e);
            }
        });
    }

    @Override
    protected boolean closeHandle(GameWebSocketSession session) {
        dispatcherAction.createAction(1002).ifPresent(p-> {
            try {
                p.action(new TransferData(session,"",1002,Optional.empty()));
            } catch (IOException e) {
                logger.error("action error ",e);
            }
        });
        return true;
    }
}
