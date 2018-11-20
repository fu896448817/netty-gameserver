package com.linkflywind.gameserver.core.room.message;

import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.room.RoomContext;
import com.linkflywind.gameserver.core.room.message.baseMessage.GameInitMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessage extends GameInitMessage {
    private Player player;
    private RoomContext roomContext;
}
