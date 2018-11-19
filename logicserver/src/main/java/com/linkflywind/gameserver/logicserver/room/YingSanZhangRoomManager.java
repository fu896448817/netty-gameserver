package com.linkflywind.gameserver.logicserver.room;


import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.redisTool.RedisTool;
import com.linkflywind.gameserver.data.monoModel.UserModel;
import com.linkflywind.gameserver.data.monoRepository.UserRepository;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
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


    @Value("${logicserver.hallserver}")
    protected String connectorName;

    @Value("${logicserver.name}")
    protected String serverName;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserRepository userRepository;

    public YingSanZhangRoomManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public YingSanZhangRoom createRoom(YingSanZhangPlayer player,
                                       int playerLowerlimit,
                                       int playerUpLimit,
                                       RedisTemplate redisTemplate,
                                       int xiaZhuTop,
                                       int juShu) {
        String roomNumber = RedisTool.inc(this.redisTemplate, "room", -1);
        YingSanZhangRoom room = new YingSanZhangRoom(roomNumber,
                playerUpLimit,
                playerLowerlimit,
                redisTemplate,
                connectorName,
                xiaZhuTop,
                juShu,
                this.serverName,
                this);
        room.create(player);
        map.put(roomNumber, room);
        return room;
    }

    public Optional<YingSanZhangRoom> appendRoom(String roomId, YingSanZhangPlayer player) {
        YingSanZhangRoom room = map.get(roomId);

        if(room.getPlayerUpLimit() < room.getPlayerList().size()) {
            room.join(player);
            return Optional.ofNullable(room);
        }
        return Optional.empty();
    }

    public YingSanZhangRoom getRoom(String roomId){
        return  map.get(roomId);
    }

    public void exitRoom(String roomId, GameWebSocketSession player) {
        map.get(roomId).exit(player.getName());
    }

    public void clerRoom(String roomId){
        map.remove(roomId);
    }
}
