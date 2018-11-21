package com.linkflywind.gameserver.logicserver.action;

import akka.actor.ActorRef;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.logicserver.protocolData.request.A1002Request;
import com.linkflywind.gameserver.logicserver.protocolData.response.ConnectResponse;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Protocol(1001)
public class CloseAction extends BaseAction implements RoomAction<A1002Request, YingSanZhangRoomContext> {


    private final YingSanZhangRoomActorManager roomActorManager;

    @Autowired
    public CloseAction(RedisTemplate redisTemplate, YingSanZhangRoomActorManager roomActorManager) {
        super(redisTemplate);
        this.roomActorManager = roomActorManager;
    }


    @Override
    public void requestAction(TransferData optionalTransferData) {
        GameWebSocketSession gameWebSocketSession = this.valueOperationsByGameWebSocketSession.get(optionalTransferData.getGameWebSocketSession().getName());


        gameWebSocketSession.getRoomNumber().ifPresent(number -> {
                    ActorRef actorRef = roomActorManager.getRoomActorRef(number);

                    actorRef.tell(new A1002Request(gameWebSocketSession.getName()), null);

                }
        );
    }

    @Override
    public boolean roomAction(A1002Request message, YingSanZhangRoomContext context) {
        context.sendAll(new ConnectResponse(message.getName()), 1001);

        return false;
    }
}
