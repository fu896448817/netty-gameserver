package com.linkflywind.gameserver.logicserver.player;

import com.linkflywind.gameserver.cardlib.poker.YingSanZhang.YingSanZhang;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.player.Player;

public class YingSanZhangPlayer extends Player {


    private YingSanZhang yingSanZhang;


    private boolean isWin;

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    private boolean isOp;

    public boolean isOp() {
        return isOp;
    }

    public void setOp(boolean op) {
        isOp = op;
    }

    public YingSanZhangPlayer(GameWebSocketSession gameWebSocketSession,
                              int score) {
        super(gameWebSocketSession, score);
    }

    public YingSanZhang getYingSanZhang() {
        return yingSanZhang;
    }

    public void setYingSanZhang(YingSanZhang yingSanZhang) {
        this.yingSanZhang = yingSanZhang;
    }
}
