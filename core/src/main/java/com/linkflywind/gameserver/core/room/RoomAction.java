package com.linkflywind.gameserver.core.room;

import com.linkflywind.gameserver.core.player.Player;

public interface RoomAction<T,D> {
    boolean action(T message, D context, Player player);
}
