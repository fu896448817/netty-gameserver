package com.linkflywind.gameserver.logicserver.room;


import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import com.linkflywind.gameserver.core.redisTool.RedisTool;
import com.linkflywind.gameserver.core.room.Room;
import com.linkflywind.gameserver.core.room.RoomManager;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class YingSanZhangRoomActorManager extends RoomManager {


    public String createRoomActor(YingSanZhangPlayer player,
                                       int playerLowerlimit,
                                       int playerUpLimit,
                                       RedisTemplate redisTemplate,
                                       int xiaZhuTop,
                                       int juShu) {
        String roomNumber = RedisTool.inc(this.redisTemplate, "room", -1);

        YingSanZhangRoomContext yingSanZhangRoomContext = new YingSanZhangRoomContext(
                roomNumber,
                playerUpLimit,
                playerLowerlimit,
                redisTemplate,
                player,
                serverName,
                connectorName,
                this
        );

        ActorRef actorRef = actorSystem.actorOf(new RoundRobinPool(1).props(Props.create(Room.class,
                yingSanZhangRoomContext
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
