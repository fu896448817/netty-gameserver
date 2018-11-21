package com.linkflywind.gameserver.logicserver.action;

import akka.actor.ActorRef;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.TransferData;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.protocolData.request.A1007Request;
import com.linkflywind.gameserver.logicserver.protocolData.response.A1007Response;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Protocol(1007)
public class A1007Action extends BaseAction implements RoomAction<A1007Request, YingSanZhangRoomContext> {


    private final YingSanZhangRoomActorManager roomActorManager;

    @Autowired
    protected A1007Action(RedisTemplate redisTemplate, YingSanZhangRoomActorManager roomActorManager) {
        super(redisTemplate);
        this.roomActorManager = roomActorManager;
    }

    @Override
    public void requestAction(TransferData optionalTransferData) throws IOException {
        A1007Request a1007Request = unPackJson(optionalTransferData.getData().get(), A1007Request.class);
        ActorRef actorRef = roomActorManager.getRoomActorRef(a1007Request.getRoomId());

        actorRef.tell(a1007Request, null);
    }

    @Override
    public boolean roomAction(A1007Request message, YingSanZhangRoomContext context) {

        Player currentPlayer = (Player) context.getPlayerList().element();
        if (currentPlayer.chip >= message.getChip()) {
            context.deskChip += message.getChip();
            currentPlayer.chip -= message.getChip();
            context.sendAll(new A1007Response((YingSanZhangPlayer) currentPlayer, message.getType(), message.getChip()), 1007);
            context.next();
        }
        context.getPlayerList().poll();
        return false;
    }
}
