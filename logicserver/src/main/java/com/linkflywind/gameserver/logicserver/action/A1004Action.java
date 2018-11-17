package com.linkflywind.gameserver.logicserver.action;

import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.protocolData.A1004Request;
import com.linkflywind.gameserver.logicserver.protocolData.A1004Response;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoom;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Protocol(1003)
public class A1004Action extends BaseAction {

    @Autowired
    private final YingSanZhangRoomManager roomManager;

    protected A1004Action(RedisTemplate redisTemplate, YingSanZhangRoomManager roomManager) {
        super(redisTemplate);
        this.roomManager = roomManager;
    }

    @Override
    public void action(TransferData optionalTransferData) throws IOException {
        A1004Request a1003Request = unPackJson(optionalTransferData.getData().get(), A1004Request.class);
        YingSanZhangPlayer p = new YingSanZhangPlayer(optionalTransferData.getGameWebSocketSession(), 1000);
        YingSanZhangRoom room = roomManager.appendRoom(a1003Request.getRoomId(), p);

        for (Player player : room.getPlayerList()) {
            TransferData sendTransferData = new TransferData(player.getGameWebSocketSession(), optionalTransferData.getChannel(), optionalTransferData.getProtocol(), Optional.empty());
            send(new A1004Response(room.getPlayerList()
                    .stream()
                    .map(player1 -> (YingSanZhangPlayer) player)
                    .collect(Collectors.toList()), room.getRoomNumber()), sendTransferData, connectorName);
        }

    }
}
