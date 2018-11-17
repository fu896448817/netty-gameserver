package com.linkflywind.gameserver.logicserver.room;


import com.linkflywind.gameserver.cardlib.poker.YingSanZhang.YingSanZhangPoker;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.room.Room;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;

public class YingSanZhangRoom extends Room {
    YingSanZhangPoker yingSanZhangPoker;

    public YingSanZhangRoom(String roomNumber, int playerLowerlimit, int playerUpLimit, RedisTemplate redisTemplate) {
        super(roomNumber, playerLowerlimit, playerUpLimit, redisTemplate);
        this.playerList = new ArrayList<>();
        this.yingSanZhangPoker = new YingSanZhangPoker();
    }

    @Override
    public void beginGame() {
        for (Player player: this.playerList) {
            YingSanZhangPlayer yingSanZhangPlayer = ((YingSanZhangPlayer)player);
            yingSanZhangPlayer.setYingSanZhang(yingSanZhangPoker.getPocker());
        }
        ((YingSanZhangPlayer)this.playerList.get(0)).setOp(true);
    }

    public void xiaZhu(){

    }

    public void jiaZhu(){

    }

    public void biPai(){

    }

    public void result(){

    }

    public void reLoadGame(){

    }
}
