package com.linkflywind.gameserver.logicserver.room.action;

import com.linkflywind.gameserver.core.annotation.RoomActionMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.room.RoomAction;
import com.linkflywind.gameserver.core.room.RoomContext;
import com.linkflywind.gameserver.core.room.message.CreateMessage;
import org.springframework.stereotype.Component;

@Component
@RoomActionMapper(CreateMessage.class)
public class CreateRoomAction implements RoomAction<CreateMessage, RoomContext> {
    @Override
    public boolean action(CreateMessage message, RoomContext context, Player player) {

        return false;
    }
}
