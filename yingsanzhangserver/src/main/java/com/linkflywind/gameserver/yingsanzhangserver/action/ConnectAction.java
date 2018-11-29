package com.linkflywind.gameserver.yingsanzhangserver.action;

import akka.actor.ActorRef;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.TransferData;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.yingsanzhangserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.yingsanzhangserver.protocolData.request.A1001Request;
import com.linkflywind.gameserver.yingsanzhangserver.protocolData.response.A1011Response;
import com.linkflywind.gameserver.yingsanzhangserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.yingsanzhangserver.room.YingSanZhangRoomContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Protocol(1002)
public class ConnectAction extends BaseAction implements RoomAction<A1001Request, YingSanZhangRoomContext> {

    @Autowired
    private YingSanZhangRoomActorManager roomActorManager;


    @Override
    public void requestAction(TransferData optionalTransferData) {


        GameWebSocketSession session = optionalTransferData.getGameWebSocketSession();

        ActorRef actorRef = roomActorManager.getRoomActorRef(session.getRoomNumber());
        actorRef.tell(new A1001Request(session, session.getRoomNumber()), null);
    }

    @Override
    public void roomAction(A1001Request message, YingSanZhangRoomContext context) {

        Optional<Player> optionalPlayer = context.getPlayer(message.getSession().getId());
        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            player.setGameWebSocketSession(player.getGameWebSocketSession());
            player.setDisConnection(false);
            context.send(new A1011Response(context.deskChip, context.getPlayerList().toArray(new YingSanZhangPlayer[0])),
                    new TransferData(message.getSession(),
                            context.getServerName(), 1011, null));
        }
    }
}
