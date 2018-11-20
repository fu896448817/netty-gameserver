package com.linkflywind.gameserver.core.room;

import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.room.message.*;
import com.linkflywind.gameserver.core.room.message.baseMessage.GameInitMessage;
import com.linkflywind.gameserver.core.room.message.baseMessage.GameRunMessage;
import com.linkflywind.gameserver.core.room.message.baseMessage.UnhandledMessage;

import java.util.LinkedList;
import java.util.Optional;

public abstract class Room extends RoomStateMachine {

    protected Room(RoomContext roomContext) {
        super(roomContext);
    }

    @Override
    public State<RoomState, RoomContext> InitEvent(GameInitMessage message, RoomContext roomContext) {
        if (message instanceof CreateMessage) {
            roomContext.playerList.add(((CreateMessage) message).getPlayer());
            roomContext.getRoomManager().getCacheMap().get(message.getClass()).action(message, roomContext, ((CreateMessage) message).getPlayer());
            return stay().using(roomContext);
        }
        if (message instanceof AppendMessage) {

            Boolean result = roomContext.getRoomManager().getCacheMap().get(message.getClass()).action(message, roomContext, ((CreateMessage) message).getPlayer());

            if (result) {
                return goTo(RoomState.RUN).using(roomContext);
            }
        }
        if (message instanceof ReadyMessage) {
            Optional<Player> player = getPlayer(((ReadyMessage) message).getName(), roomContext.playerList);
            if (player.isPresent()) {
                boolean result = roomContext.getRoomManager().getCacheMap().get(message.getClass()).action(message, roomContext, ((CreateMessage) message).getPlayer());
                if (result) {
                    return goTo(RoomState.RUN).using(roomContext);
                }
            }
        }
        return stay().using(roomContext);
    }

    @Override
    public State<RoomState, RoomContext> RunEvent(GameRunMessage message, RoomContext roomContext) {

        Boolean result = roomContext.getRoomManager().getCacheMap().get(message.getClass()).action(message, roomContext, null);
        if (result) {
            return goTo(RoomState.INIT);
        }

        return stay().using(roomContext);
    }

    @Override
    public State<RoomState, RoomContext> UnhandledEvent(UnhandledMessage message, RoomContext roomContext) {
        if (message instanceof ConnectionMessage) {
            Optional<Player> player = this.getPlayer(((ConnectionMessage) message).getName(), roomContext.getPlayerList());
            if (player.isPresent()) {
                Player currentPlayer = player.get();
                currentPlayer.setDisConnection(false);


              roomContext.getRoomManager().getCacheMap().get(message.getClass()).action(message, roomContext, currentPlayer);
            }
        }

        if (message instanceof DisConnectionMessage) {
            Optional<Player> player = this.getPlayer(((DisConnectionMessage) message).getName(), roomContext.getPlayerList());
            if (player.isPresent()) {
                Player currentPlayer = player.get();
                currentPlayer.setDisConnection(false);
                roomContext.getRoomManager().getCacheMap().get(message.getClass()).action(message, roomContext, currentPlayer);
            }
        }

        if (message instanceof DisbandedMessage) {
            Optional<Player> player = this.getPlayer(((DisConnectionMessage) message).getName(), roomContext.getPlayerList());
            if (player.isPresent()) {
                Player currentPlayer = player.get();
                currentPlayer.setDisbanded(true);
                roomContext.getRoomManager().getCacheMap().get(message.getClass()).action(message, roomContext, currentPlayer);
            }
        }

        return stay().using(roomContext);
    }


    protected Optional<Player> getPlayer(String name, LinkedList<? super Player> playerList) {
        for (Object player : playerList) {
            if (((Player) player).getName().equals(name))
                return Optional.of(((Player) player));
        }
        return Optional.empty();
    }
}
