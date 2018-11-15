package com.linkflywind.gameserver.hallserver.action;

import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Protocol(1001)
public class CloseAction extends BaseAction {

    @Autowired
    public CloseAction(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }


    @Override
    public void action(TransferData optionalTransferData) throws IOException {
        GameWebSocketSession gameWebSocketSession = this.valueOperationsByGameWebSocketSession.get(optionalTransferData.getGameWebSocketSession().getName());

        if (gameWebSocketSession != null) {
            gameWebSocketSession.setState("1");
            this.valueOperationsByGameWebSocketSession.set(gameWebSocketSession.getName(),gameWebSocketSession);

            gameWebSocketSession.getChannel().ifPresent(channel->{
                this.redisTemplate.convertAndSend(channel,
                        new TransferData(gameWebSocketSession,channel,1001,Optional.empty()));
            });
        } else {
            optionalTransferData.getGameWebSocketSession().setState("1");
            this.valueOperationsByGameWebSocketSession.set(optionalTransferData.getGameWebSocketSession().getName(), optionalTransferData.getGameWebSocketSession());
        }

    }
}
