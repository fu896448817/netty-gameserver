/*
 * @author   作者: qugang
 * @E-mail   邮箱: qgass@163.com
 * @date     创建时间：2018/11/19
 * 类说明     准备
 */
package com.linkflywind.gameserver.logicserver.action;

import akka.actor.ActorRef;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.logicserver.protocolData.A1009Request;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoom;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.logicserver.room.message.BiPaiMessage;
import com.linkflywind.gameserver.logicserver.room.message.ReadyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Protocol(1009)
public class A1009Action extends BaseAction {
    @Autowired
    private final YingSanZhangRoomActorManager roomActorManager;

    protected A1009Action(RedisTemplate redisTemplate, YingSanZhangRoomActorManager roomActorManager) {
        super(redisTemplate);
        this.roomActorManager = roomActorManager;
    }

    @Override
    public void action(TransferData optionalTransferData) throws IOException {
        A1009Request a1009Request = unPackJson(optionalTransferData.getData().get(), A1009Request.class);


        ActorRef actorRef = roomActorManager.getRoomActorRef(a1009Request.getRoomId());

        actorRef.tell(new ReadyMessage(a1009Request.getName()), null);
    }
}
