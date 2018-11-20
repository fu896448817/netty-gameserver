package com.linkflywind.gameserver.logicserver.room.action;

import com.linkflywind.gameserver.core.annotation.RoomActionMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.core.room.message.CreateMessage;
import com.linkflywind.gameserver.core.room.message.DisbandedMessage;
import com.linkflywind.gameserver.logicserver.protocolData.A1012Response;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomContext;
import org.springframework.stereotype.Component;

@Component
@RoomActionMapper(DisbandedMessage.class)
public class DisbandedRoomAction implements RoomAction<DisbandedMessage, YingSanZhangRoomContext> {
    @Override
    public boolean action(DisbandedMessage message, YingSanZhangRoomContext context, Player player) {

        context.sendAll(new A1012Response(message.getName(), context.getRoomNumber()), 1012);
        int person = context.getPlayerList().size() / 2;
        if (context.getPlayerList().stream().filter(p -> ((Player) p).isDisbanded()).count() >= person) {
            context.clearRoom();
        }
        return false;
    }
}
