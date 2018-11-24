package com.linkflywind.gameserver.yingsanzhangserver.action;

import akka.actor.ActorRef;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.TransferData;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.yingsanzhangserver.protocolData.request.A1002Request;
import com.linkflywind.gameserver.yingsanzhangserver.protocolData.response.ConnectResponse;
import com.linkflywind.gameserver.yingsanzhangserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.yingsanzhangserver.room.YingSanZhangRoomContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        GameWebSocketSession gameWebSocketSession = this.valueOperationsByGameWebSocketSession.get(optionalTransferData.getGameWebSocketSession().getId());


        gameWebSocketSession.getRoomNumber().ifPresent(number -> {
                    ActorRef actorRef = roomActorManager.getRoomActorRef(number);

                    actorRef.tell(new A1002Request(gameWebSocketSession.getId(), gameWebSocketSession), null);

                }
        );
    }

    @Override
    public boolean roomAction(A1002Request message, YingSanZhangRoomContext context) {
        Optional<Player> p = context.getPlayer(message.getName());
        p.ifPresent(player -> {
            player.setDisConnection(true);
            player.setGameWebSocketSession(message.getSession());
            context.sendAll(new ConnectResponse(player.getGameWebSocketSession().getId()), 1001);
        });

        return false;
    }
}
