package com.linkflywind.gameserver.logicserver.player;

import com.linkflywind.gameserver.cardlib.poker.YingSanZhang.YingSanZhang;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.player.Player;
import lombok.Data;

@Data
public class YingSanZhangPlayer extends Player implements Comparable<YingSanZhangPlayer> {

    private YingSanZhang yingSanZhang;
    private YingSanZhangPlayerState state;

    public YingSanZhangPlayer(GameWebSocketSession gameWebSocketSession,
                              int score,
                              Boolean isReady) {
        super(gameWebSocketSession, score,isReady);
        this.setState(YingSanZhangPlayerState.none);
        this.setReady(true);
    }

    public YingSanZhang getYingSanZhang() {
        return yingSanZhang;
    }

    public void setYingSanZhang(YingSanZhang yingSanZhang) {
        this.yingSanZhang = yingSanZhang;
    }


    @Override
    public int compareTo(YingSanZhangPlayer o) {
        return this.getYingSanZhang().compareTo(o.getYingSanZhang());
    }
}
