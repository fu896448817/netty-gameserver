/*
 * @author   作者: qugang
 * @E-mail   邮箱: qgass@163.com
 * @date     创建时间：2018/11/19
 * 类说明     加入房间
 */
package com.linkflywind.gameserver.logicserver.action;

import akka.actor.ActorRef;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.TransferData;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.protocolData.request.A1004Request;
import com.linkflywind.gameserver.logicserver.protocolData.response.A1004Response;
import com.linkflywind.gameserver.logicserver.protocolData.response.ErrorResponse;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Protocol(1003)
public class A1004Action extends BaseAction implements RoomAction<A1004Request, YingSanZhangRoomContext> {


    @Autowired
    private final YingSanZhangRoomActorManager roomActorManager;


    private final ValueOperations<String, GameWebSocketSession> valueOperationsByPlayer;


    protected A1004Action(RedisTemplate redisTemplate, YingSanZhangRoomActorManager roomActorManager) {
        super(redisTemplate);
        this.roomActorManager = roomActorManager;

        this.valueOperationsByPlayer = redisTemplate.opsForValue();
    }

    @Override
    public void requestAction(TransferData optionalTransferData) throws IOException {
        A1004Request a1004Request = unPackJson(optionalTransferData.getData().get(), A1004Request.class);
        ActorRef actorRef = roomActorManager.getRoomActorRef(a1004Request.getRoomId());


        actorRef.tell(a1004Request, null);
    }

    @Override
    public boolean roomAction(A1004Request message, YingSanZhangRoomContext context) {

        GameWebSocketSession session = this.valueOperationsByPlayer.get(message.getName());
        YingSanZhangPlayer player = new YingSanZhangPlayer(1000, true, message.getName());
        player.setGameWebSocketSession(session);
        if (context.getPlayerList().size() <= context.getPlayerUpLimit()) {
            context.getPlayerList().add(player);


            session.setRoomNumber(java.util.Optional.ofNullable(context.getRoomNumber()));

            session.setChannel(java.util.Optional.ofNullable(context.getServerName()));

            this.valueOperationsByPlayer.set(player.getGameWebSocketSession().getName(), session);

            context.sendAll(new A1004Response(context.getPlayerList().toArray(new YingSanZhangPlayer[0]), context.getRoomNumber()), 1004);

            if (context.getPlayerList().size() >= context.getPlayerLowerlimit()) {
                context.beginGame();
                return true;
            }
        } else {

            try {
                context.send(new ErrorResponse("房间已满"), new TransferData(session, context.getServerName(), 1003, null));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
