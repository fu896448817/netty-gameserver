package com.linkflywind.gameserver.logicserver.room.action;

import com.linkflywind.gameserver.core.annotation.RoomActionMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayerState;
import com.linkflywind.gameserver.logicserver.protocolData.A1008Response;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomContext;
import com.linkflywind.gameserver.logicserver.room.message.BetsMessage;
import com.linkflywind.gameserver.logicserver.room.message.ComparisonMessage;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RoomActionMapper(ComparisonMessage.class)
public class ComparisonCardRoomAction implements RoomAction<ComparisonMessage, YingSanZhangRoomContext> {
    @Override
    public boolean action(ComparisonMessage message, YingSanZhangRoomContext context, Player player) {

        YingSanZhangPlayer yingSanZhangPlayer = (YingSanZhangPlayer) context.getPlayerList().element();
        Optional<Player> optionalPlayer = context.getPlayer(message.getName());
        optionalPlayer.ifPresent(p -> {
            YingSanZhangPlayer currentPlayer = (YingSanZhangPlayer) p;
            int result = yingSanZhangPlayer.compareTo(currentPlayer);
            if (result > 0) {
                yingSanZhangPlayer.setState(YingSanZhangPlayerState.shu);
                context.sendAll(new A1008Response(yingSanZhangPlayer, currentPlayer), 1008);
            } else if (result < 1) {
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
