package com.linkflywind.gameserver.logicserver.action;


import akka.actor.ActorRef;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.logicserver.protocolData.A1008Request;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.logicserver.room.message.ComparisonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Protocol(1008)
public class A1008Action extends BaseAction {

    private final YingSanZhangRoomActorManager roomActorManager;

    @Autowired
    protected A1008Action(RedisTemplate redisTemplate, YingSanZhangRoomActorManager roomActorManager) {
        super(redisTemplate);
        this.roomActorManager = roomActorManager;
    }

    @Override
    public void action(TransferData optionalTransferData) throws IOException {

        A1008Request a1008Request = unPackJson(optionalTransferData.getData().get(), A1008Request.class);

        ActorRef actorRef = roomActorManager.getRoomActorRef(a1008Request.getRoomId());

        actorRef.tell(new ComparisonMessage(a1008Request.getToName()),null);
    }
}
