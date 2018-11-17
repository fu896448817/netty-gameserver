package com.linkflywind.gameserver.logicserver.action;

import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.protocolData.A1003Request;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomManager;
import com.linkflywind.gameserver.logicserver.protocolData.A1003Response;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Protocol(1003)
public class A1003Action extends BaseAction {


    @Autowired
    private final YingSanZhangRoomManager roomManager;

    protected A1003Action(RedisTemplate redisTemplate, YingSanZhangRoomManager roomManager) {
        super(redisTemplate);
        this.roomManager = roomManager;
    }

    @Override
    public void action(TransferData optionalTransferData) throws IOException {
        A1003Request a1003Request = unPackJson(optionalTransferData.getData().get(), A1003Request.class);

        YingSanZhangPlayer p = new YingSanZhangPlayer(optionalTransferData.getGameWebSocketSession(), 1000);

        YingSanZhangRoom room = roomManager.createRoom(p, a1003Request.getPlayerLowerlimit(), a1003Request.getPlayerUpLimit(), redisTemplate);
        send(new A1003Response(room.getRoomNumber()), optionalTransferData, connectorName);
    }
}
