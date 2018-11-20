package com.linkflywind.gameserver.logicserver.room;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
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
public class YingSanZhangRoomActorManager {

    private String name;
    private int littleChip;
    private int intoChip;
    ConcurrentHashMap<String, ActorRef> map = new ConcurrentHashMap<>();


    @Value("${logicserver.hallserver}")
    protected String connectorName;

    @Value("${logicserver.name}")
    protected String serverName;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActorSystem actorSystem;

    public YingSanZhangRoomActorManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public String createRoomActor(YingSanZhangPlayer player,
                                       int playerLowerlimit,
                                       int playerUpLimit,
                                       RedisTemplate redisTemplate,
                                       int xiaZhuTop,
                                       int juShu) {
        String roomNumber = RedisTool.inc(this.redisTemplate, "room", -1);
        ActorRef actorRef = actorSystem.actorOf(new RoundRobinPool(1).props(Props.create(YingSanZhangRoomActor.class,
                roomNumber,
                playerUpLimit,
                playerLowerlimit,
                redisTemplate,
                connectorName,
                xiaZhuTop,
                juShu,
                this.serverName,
                this
                )));
        map.put(roomNumber, actorRef);
        return roomNumber;
    }

    public ActorRef getRoomActorRef(String roomId){
        return map.get(roomId);
    }

    public void clearRoom(String roomId){
       ActorRef actorRef =  map.remove(roomId);
       actorSystem.stop(actorRef);
    }
}
