package com.linkflywind.gameserver.logicserver.room.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkflywind.gameserver.core.annotation.RoomActionMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.core.room.message.ConnectionMessage;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.protocolData.A1011Response;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RoomActionMapper(ConnectionMessage.class)
public class ConnectionRoomAction implements RoomAction<ConnectionMessage, YingSanZhangRoomContext> {
    @Override
    public boolean action(ConnectionMessage message, YingSanZhangRoomContext context, Player player) {
        try {
            context.send(new A1011Response(context.deskChip, context.getPlayerList().toArray(new YingSanZhangPlayer[0])),
                    new TransferData(player.getGameWebSocketSession(),
                            context.getServerName(), 1011, Optional.empty()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
