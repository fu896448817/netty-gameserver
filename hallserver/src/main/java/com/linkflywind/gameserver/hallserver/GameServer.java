package com.linkflywind.gameserver.hallserver;


import com.linkflywind.gameserver.core.action.DispatcherAction;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocket;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.TransferData;
import com.linkflywind.gameserver.core.security.JwtTokenUtil;
import com.linkflywind.gameserver.hallserver.config.HallConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.ServerEndpoint;

import java.util.Optional;

@Component
@ServerEndpoint(prefix = "netty-websocket")
public class GameServer extends GameWebSocket {

    @Autowired
    private  DispatcherAction dispatcherAction;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private  HallConfig hallConfig;

    @Override
    protected boolean receiveHandle(GameWebSocketSession session, int channel, int protocol, byte[] buffer) {

        
        String channelString = hallConfig.getRoutes().get(String.valueOf(channel));
        if (channelString.equals(hallConfig.getName())) {
            dispatcherAction.createAction(protocol);
        } else {
            this.redisTemplate.convertAndSend(channelString, new TransferData(session, channelString, protocol, buffer));
        }
        return true;
    }

    @Override
    protected void openHandle(GameWebSocketSession session) {

        if(session.getChannel() != null && !session.getChannel().isEmpty())
        {
            this.redisTemplate.convertAndSend(session.getChannel(), new TransferData(session, "", 1001, null));
        }
    }

    @Override
    protected void closeHandle(GameWebSocketSession session) {
        if(session.getChannel() != null && !session.getChannel().isEmpty()){
            this.redisTemplate.convertAndSend(session.getChannel(), new TransferData(session, "", 1002, null));
        }
    }
}
