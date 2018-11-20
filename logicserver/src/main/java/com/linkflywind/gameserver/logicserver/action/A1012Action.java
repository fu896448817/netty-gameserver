package com.linkflywind.gameserver.logicserver.action;


import akka.actor.ActorRef;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.logicserver.protocolData.A1012Request;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.logicserver.room.message.DisbandedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Protocol(1012)
public class A1012Action extends BaseAction {

    private final YingSanZhangRoomActorManager roomActorManager;


    @Autowired
    protected A1012Action(RedisTemplate redisTemplate, YingSanZhangRoomActorManager roomActorManager) {
        super(redisTemplate);
        this.roomActorManager = roomActorManager;
    }

    @Override
    public void action(TransferData optionalTransferData) throws IOException {
        A1012Request a1009Request = unPackJson(optionalTransferData.getData().get(), A1012Request.class);

        ActorRef actorRef = roomActorManager.getRoomActorRef(a1009Request.getRoomId());
        actorRef.tell(new DisbandedMessage(a1009Request.getRoomId(), a1009Request.getName()), null);
    }
}
