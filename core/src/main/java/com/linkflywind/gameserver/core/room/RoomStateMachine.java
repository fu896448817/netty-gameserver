package com.linkflywind.gameserver.core.room;

import akka.actor.AbstractFSM;
import com.linkflywind.gameserver.core.room.message.baseMessage.GameInitMessage;
import com.linkflywind.gameserver.core.room.message.baseMessage.GameRunMessage;
import com.linkflywind.gameserver.core.room.message.baseMessage.UnhandledMessage;

public abstract class RoomStateMachine extends AbstractFSM<RoomState, RoomContext> {


    RoomStateMachine(RoomContext roomContext) {
        startWith(RoomState.INIT, roomContext);

        when(RoomState.INIT, matchEvent(GameInitMessage.class, RoomContext.class, this::InitEvent));

        when(RoomState.RUN, matchEvent(GameRunMessage.class, RoomContext.class, this::RunEvent));

        whenUnhandled(
                matchEvent(UnhandledMessage.class,
                        this::UnhandledEvent));
    }

    public abstract State<RoomState, RoomContext> InitEvent(GameInitMessage message, RoomContext roomContext);

    public abstract State<RoomState, RoomContext> RunEvent(GameRunMessage message, RoomContext roomContext);

    public abstract State<RoomState, RoomContext> UnhandledEvent(UnhandledMessage message, RoomContext roomContext);

}
