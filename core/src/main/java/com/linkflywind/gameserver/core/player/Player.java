package com.linkflywind.gameserver.core.player;

import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player {
    private GameWebSocketSession gameWebSocketSession;
    private int score;
}
