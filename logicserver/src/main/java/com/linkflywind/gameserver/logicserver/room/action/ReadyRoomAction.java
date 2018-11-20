package com.linkflywind.gameserver.logicserver.room.action;

import com.linkflywind.gameserver.core.annotation.RoomActionMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.core.room.message.DisConnectionMessage;
import com.linkflywind.gameserver.core.room.message.ReadyMessage;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.protocolData.A1009Response;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomContext;
import org.springframework.stereotype.Component;


@Component
@RoomActionMapper(ReadyMessage.class)
public class ReadyRoomAction implements RoomAction<ReadyMessage, YingSanZhangRoomContext> {
    @Override
    public boolean action(ReadyMessage message, YingSanZhangRoomContext context, Player player) {
        player.setReady(true);
        YingSanZhangPlayer currentPlayer = ((YingSanZhangPlayer) player);
        context.sendAll(new A1009Response(currentPlayer), 1009);
        if (context.getPlayerList().stream().anyMatch(p -> ((Player) p).isReady())) {
            context.beginGame();
            return true;
        }
        return false;
    }
}
