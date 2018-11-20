package com.linkflywind.gameserver.core.player;

import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import lombok.Data;

@Data
public class Player {
    private GameWebSocketSession gameWebSocketSession;
    private String name;
    public int chip;
    private boolean isReady;
    private String roomId;
    private boolean isDisConnection;
    private boolean isDisbanded;

    private boolean isOp;

    public Player(GameWebSocketSession gameWebSocketSession, int chip, boolean isReady) {
        this.gameWebSocketSession = gameWebSocketSession;
        this.chip = chip;
        this.isReady = isReady;
    }
}
