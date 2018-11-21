package com.linkflywind.gameserver.core.room;

import akka.actor.AbstractFSM;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.room.message.baseMessage.GameInitMessage;
import com.linkflywind.gameserver.core.room.message.baseMessage.GameRunMessage;
import com.linkflywind.gameserver.core.room.message.baseMessage.UnhandledMessage;

public abstract class Room extends AbstractFSM<RoomState, RoomContext> {


    Room(RoomContext roomContext) {
        startWith(RoomState.INIT, roomContext);

        when(RoomState.INIT, matchEvent(GameInitMessage.class, RoomContext.class, this::InitEvent));

        when(RoomState.RUN, matchEvent(GameRunMessage.class, RoomContext.class, this::RunEvent));

        whenUnhandled(
                matchEvent(UnhandledMessage.class,
                        this::UnhandledEvent));
    }

    public State<RoomState, RoomContext> InitEvent(GameInitMessage message, RoomContext roomContext) {

        Integer protocol = message.getClass().getAnnotation(Protocol.class).value();

        if (roomContext.getRoomManager().getCacheMap().get(protocol).roomAction(message, roomContext)) {
            return goTo(RoomState.RUN).using(roomContext);
        }
        return stay().using(roomContext);
    }

    public State<RoomState, RoomContext> RunEvent(GameRunMessage message, RoomContext roomContext) {

        Integer protocol = message.getClass().getAnnotation(Protocol.class).value();

        if (roomContext.getRoomManager().getCacheMap().get(protocol).roomAction(message, roomContext)) {
            return goTo(RoomState.INIT);
        }

        return stay().using(roomContext);
    }

    public State<RoomState, RoomContext> UnhandledEvent(UnhandledMessage message, RoomContext roomContext) {

        Integer protocol = message.getClass().getAnnotation(Protocol.class).value();
        roomContext.getRoomManager().getCacheMap().get(protocol).roomAction(message, roomContext);
        return stay().using(roomContext);
    }

}
