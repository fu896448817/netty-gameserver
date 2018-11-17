package com.linkflywind.gameserver.logicserver.room;


import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.redisTool.RedisTool;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class YingSanZhangRoomManager {

    private String name;
    private int littleChip;
    private int intoChip;
    ConcurrentHashMap<String, YingSanZhangRoom> map = new ConcurrentHashMap<>();

    @Autowired
    private RedisTemplate redisTemplate;

    public YingSanZhangRoomManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public YingSanZhangRoom createRoom(YingSanZhangPlayer player, int playerLowerlimit, int playerUpLimit, RedisTemplate redisTemplate) {
        String roomNumber = RedisTool.inc(this.redisTemplate, "room", -1);
        YingSanZhangRoom room = new YingSanZhangRoom(roomNumber, playerUpLimit, playerLowerlimit, redisTemplate);
        room.create(player);
        map.put(roomNumber, room);
        return room;
    }

    public YingSanZhangRoom appendRoom(String roomId, YingSanZhangPlayer player) {
        YingSanZhangRoom room = map.get(roomId);
        room.join(player);
        return room;
    }

    public void exitRoom(String roomId, GameWebSocketSession player) {
        map.get(roomId).exit(player.getName());
    }
}
