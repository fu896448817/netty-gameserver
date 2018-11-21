package com.linkflywind.gameserver.yingsanzhangserver.player;

import com.linkflywind.gameserver.cardlib.poker.YingSanZhang.YingSanZhang;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.player.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class YingSanZhangPlayer extends Player implements Comparable<YingSanZhangPlayer> {

    private YingSanZhang yingSanZhang;
    private YingSanZhangPlayerState state;

    public YingSanZhangPlayer(int score,
                              Boolean isReady,
                              GameWebSocketSession session) {
        super(score, isReady, session);
        this.setState(YingSanZhangPlayerState.none);
        this.setReady(true);
    }

    private YingSanZhang getYingSanZhang() {
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
