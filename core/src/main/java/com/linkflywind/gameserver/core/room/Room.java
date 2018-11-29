package com.linkflywind.gameserver.core.room;

import akka.actor.AbstractActor;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.room.message.baseMessage.RoomMessage;

public class Room extends AbstractActor {

    private RoomContext roomContext;

    Room(RoomContext roomContext) {
        this.roomContext = roomContext;
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder().match(RoomMessage.class, message -> {
            Integer protocol = message.getClass().getAnnotation(Protocol.class).value();
            roomContext.getRoomManager().getCacheMap().get(protocol).roomAction(message, roomContext);
        }).build();
    }
}
