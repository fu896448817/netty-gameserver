package com.linkflywind.gameserver.yingsanzhangserver.action;


import akka.actor.ActorRef;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.TransferData;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.yingsanzhangserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.yingsanzhangserver.player.YingSanZhangPlayerState;
import com.linkflywind.gameserver.yingsanzhangserver.protocolData.request.A1008Request;
import com.linkflywind.gameserver.yingsanzhangserver.protocolData.response.A1008Response;
import com.linkflywind.gameserver.yingsanzhangserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.yingsanzhangserver.room.YingSanZhangRoomContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Protocol(1008)
public class A1008Action extends BaseAction implements RoomAction<A1008Request, YingSanZhangRoomContext> {

    private final YingSanZhangRoomActorManager roomActorManager;

    @Autowired
    protected A1008Action(RedisTemplate redisTemplate, YingSanZhangRoomActorManager roomActorManager) {
        super(redisTemplate);
        this.roomActorManager = roomActorManager;
    }

    @Override
    public void requestAction(TransferData optionalTransferData) throws IOException {

        A1008Request a1008Request = unPackJson(optionalTransferData.getData().get(), A1008Request.class);

        ActorRef actorRef = roomActorManager.getRoomActorRef(a1008Request.getRoomId());

        actorRef.tell(a1008Request,null);
    }

    @Override
    public boolean roomAction(A1008Request message, YingSanZhangRoomContext context) {
        YingSanZhangPlayer yingSanZhangPlayer = (YingSanZhangPlayer) context.getPlayerList().element();
        Optional<Player> optionalPlayer = context.getPlayer(message.getToName());
        optionalPlayer.ifPresent(p -> {
            YingSanZhangPlayer currentPlayer = (YingSanZhangPlayer) p;
            int result = yingSanZhangPlayer.compareTo(currentPlayer);
            if (result > 0) {
                yingSanZhangPlayer.setState(YingSanZhangPlayerState.shu);
                context.sendAll(new A1008Response(yingSanZhangPlayer, currentPlayer), 1008);
            } else if (result < 0) {
                yingSanZhangPlayer.setState(YingSanZhangPlayerState.shu);
                context.sendAll(new A1008Response(currentPlayer, yingSanZhangPlayer), 1008);
            } else {
            }
            context.getPlayerList().poll();
            context.next();
        });
        return false;
    }
}
