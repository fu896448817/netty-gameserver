package com.linkflywind.gameserver.logicserver.room.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkflywind.gameserver.core.annotation.RoomActionMapper;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.core.room.message.AppendMessage;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.protocolData.A1004Response;
import com.linkflywind.gameserver.logicserver.protocolData.ErrorResponse;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RoomActionMapper(AppendMessage.class)
public class AppendRoomAction implements RoomAction<AppendMessage, YingSanZhangRoomContext> {

    private final ValueOperations<String, GameWebSocketSession> valueOperationsByPlayer;
    private final RedisTemplate redisTemplate;

    public AppendRoomAction(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperationsByPlayer = redisTemplate.opsForValue();
    }

    @Override
    public boolean action(AppendMessage message, YingSanZhangRoomContext context, Player player) {
        if (context.getPlayerList().size() <= context.getPlayerUpLimit()) {
            context.getPlayerList().add(message.getPlayer());

            player.getGameWebSocketSession().setRoomNumber(java.util.Optional.ofNullable(context.getRoomNumber()));

            player.getGameWebSocketSession().setChannel(java.util.Optional.ofNullable(context.getServerName()));

            this.valueOperationsByPlayer.set(player.getName(),player.getGameWebSocketSession());

            context.sendAll(new A1004Response(context.getPlayerList().toArray(new YingSanZhangPlayer[0]), context.getRoomNumber()), 1004);

            if (context.getPlayerList().size() >= context.getPlayerLowerlimit()) {
                context.beginGame();
                return true;
            }
        } else {

            try {
                context.send(new ErrorResponse("房间已满"), new TransferData(player.getGameWebSocketSession(),context.getServerName(),1003,null));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
