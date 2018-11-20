package com.linkflywind.gameserver.logicserver.room.message;

import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class CreateMessage {
    YingSanZhangPlayer player;
}
