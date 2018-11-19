package com.linkflywind.gameserver.core.player;

import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import lombok.Data;

@Data
public class Player {
    private GameWebSocketSession gameWebSocketSession;
    private String name;
    public int score;
    private boolean isReady;
    private String roomId;
    private boolean isDiaoXian;
    private boolean isOp;

    public Player(GameWebSocketSession gameWebSocketSession, int score, boolean isReady) {
        this.gameWebSocketSession = gameWebSocketSession;
        this.score = score;
        this.isReady = isReady;
    }
}
