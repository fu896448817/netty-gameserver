package com.linkflywind.gameserver.core.player;

import lombok.Data;

@Data
public class Player {
    private String name;
    public int chip;
    private boolean isReady;
    private String roomId;
    private boolean isDisConnection;
    private boolean isDisbanded;

    private boolean isOp;

    public Player(int chip, boolean isReady,String name) {
        this.chip = chip;
        this.isReady = isReady;
        this.name = name;
    }
}
