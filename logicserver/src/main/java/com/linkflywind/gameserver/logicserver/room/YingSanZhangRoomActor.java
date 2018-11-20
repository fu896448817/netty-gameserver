package com.linkflywind.gameserver.logicserver.room;

import akka.actor.AbstractActor;
import com.linkflywind.gameserver.logicserver.room.message.*;
import org.springframework.data.redis.core.RedisTemplate;

public class YingSanZhangRoomActor extends AbstractActor {

    YingSanZhangRoom yingSanZhangRoom;

    YingSanZhangRoomActor(String roomNumber,
                          int playerLowerlimit,
                          int playerUpLimit,
                          RedisTemplate redisTemplate,
                          String connectorName,
                          int xiaZhuTop,
                          int juShu,
                          String serverName,
                          YingSanZhangRoomActorManager yingSanZhangRoomManager) {
        yingSanZhangRoom = new YingSanZhangRoom(roomNumber,
                playerLowerlimit,
                playerUpLimit,
                redisTemplate,
                connectorName,
                xiaZhuTop,
                juShu,
                serverName,
                yingSanZhangRoomManager);
    }

    @Override
    public Receive createReceive() {

        context().stop(getSelf());

        return receiveBuilder().match(BiPaiMessage.class,biPaiMessage -> {
            yingSanZhangRoom.biPai(biPaiMessage.getName());
        }).match(XiaZhuMessage.class,xiaZhuMessage -> {
            yingSanZhangRoom.xiazhu(xiaZhuMessage.getChouma(),xiaZhuMessage.getType());
        }).match(ReadyMessage.class,readyMessage -> {
            yingSanZhangRoom.ready(readyMessage.getName());
        }).match(ConnectionMessage.class,connectionMessage -> {
            yingSanZhangRoom.reConnection(connectionMessage.getName());
        }).match(CloseMessage.class,closeMessage -> {
            yingSanZhangRoom.disConnection(closeMessage.getName());
        }).match(CreateMessage.class,createMessage -> {
            yingSanZhangRoom.create(createMessage.getPlayer());
        }).match(AppendMessage.class,appendMessage -> {
            boolean result = yingSanZhangRoom.join(appendMessage.getYingSanZhangPlayer());
            getSender().tell(new ResultMessage(result),getSelf());
        }).build();
    }
}
