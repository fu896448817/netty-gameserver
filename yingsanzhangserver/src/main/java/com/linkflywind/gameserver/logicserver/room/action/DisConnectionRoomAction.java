package com.linkflywind.gameserver.logicserver.room.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkflywind.gameserver.core.annotation.RoomActionMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.core.room.message.DisConnectionMessage;
import com.linkflywind.gameserver.logicserver.protocolData.ConnectResponse;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomContext;
import org.springframework.stereotype.Component;


@Component
@RoomActionMapper(DisConnectionMessage.class)
public class DisConnectionRoomAction implements RoomAction<DisConnectionMessage, YingSanZhangRoomContext> {
    @Override
    public boolean action(DisConnectionMessage message, YingSanZhangRoomContext context, Player player) {

        context.sendAll(new ConnectResponse(message.getName()), 1001);

        return false;
    }
}
