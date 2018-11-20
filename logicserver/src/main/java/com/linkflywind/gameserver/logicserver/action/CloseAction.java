package com.linkflywind.gameserver.logicserver.action;

import akka.actor.ActorRef;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.core.room.message.DisConnectionMessage;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomActorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Protocol(1001)
public class CloseAction extends BaseAction {


    private final YingSanZhangRoomActorManager roomActorManager;

    @Autowired
    public CloseAction(RedisTemplate redisTemplate, YingSanZhangRoomActorManager roomActorManager) {
        super(redisTemplate);
        this.roomActorManager = roomActorManager;
    }


    @Override
    public void action(TransferData optionalTransferData) {
        GameWebSocketSession gameWebSocketSession = this.valueOperationsByGameWebSocketSession.get(optionalTransferData.getGameWebSocketSession().getName());


        gameWebSocketSession.getRoomNumber().ifPresent(number -> {
                    ActorRef actorRef = roomActorManager.getRoomActorRef(number);

                    actorRef.tell(new DisConnectionMessage(gameWebSocketSession.getName()), null);

                }
        );
    }
}
