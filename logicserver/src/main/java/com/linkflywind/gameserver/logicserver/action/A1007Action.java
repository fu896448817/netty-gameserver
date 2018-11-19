package com.linkflywind.gameserver.logicserver.action;

import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.logicserver.protocolData.A1007Request;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoom;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Protocol(1007)
public class A1007Action extends BaseAction {

    @Autowired
    private final YingSanZhangRoomManager roomManager;

    protected A1007Action(RedisTemplate redisTemplate, YingSanZhangRoomManager roomManager) {
        super(redisTemplate);
        this.roomManager = roomManager;
    }

    @Override
    public void action(TransferData optionalTransferData) throws IOException {
        A1007Request a1007Request = unPackJson(optionalTransferData.getData().get(), A1007Request.class);
        YingSanZhangRoom yingSanZhangRoom = roomManager.getRoom(a1007Request.getRoomId());
        yingSanZhangRoom.xiazhu(a1007Request.getChouma(),a1007Request.getType());
    }
}
