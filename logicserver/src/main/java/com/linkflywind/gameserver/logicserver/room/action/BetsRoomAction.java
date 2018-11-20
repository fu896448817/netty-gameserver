package com.linkflywind.gameserver.logicserver.room.action;

import com.linkflywind.gameserver.core.annotation.RoomActionMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.core.room.message.AppendMessage;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.protocolData.A1007Response;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomContext;
import com.linkflywind.gameserver.logicserver.room.message.BetsMessage;
import org.springframework.stereotype.Component;

@Component
@RoomActionMapper(BetsMessage.class)
public class BetsRoomAction implements RoomAction<BetsMessage, YingSanZhangRoomContext> {
    @Override
    public boolean action(BetsMessage message, YingSanZhangRoomContext context, Player player) {
        Player currentPlayer = (Player) context.getPlayerList().element();
        if (currentPlayer.chip >= message.getChip()) {
            context.deskChip += message.getChip();
            currentPlayer.chip -= message.getChip();
            context.sendAll(new A1007Response((YingSanZhangPlayer) player, message.getType(), message.getChip()), 1007);
            context.next();
        }
        context.getPlayerList().poll();
        return false;
    }
}
