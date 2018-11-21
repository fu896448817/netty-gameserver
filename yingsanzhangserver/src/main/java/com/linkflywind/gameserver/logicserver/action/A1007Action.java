package com.linkflywind.gameserver.logicserver.action;

import akka.actor.ActorRef;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.logicserver.protocolData.A1007Request;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.logicserver.room.message.BetsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Protocol(1007)
public class A1007Action extends BaseAction {


    private final YingSanZhangRoomActorManager roomActorManager;

    @Autowired
    protected A1007Action(RedisTemplate redisTemplate, YingSanZhangRoomActorManager roomActorManager) {
        super(redisTemplate);
        this.roomActorManager = roomActorManager;
    }

    @Override
    public void action(TransferData optionalTransferData) throws IOException {
        A1007Request a1007Request = unPackJson(optionalTransferData.getData().get(), A1007Request.class);
        ActorRef actorRef = roomActorManager.getRoomActorRef(a1007Request.getRoomId());

        actorRef.tell(new BetsMessage(a1007Request.getChouma(), a1007Request.getType()), null);
    }
}
