/*
 * @author   作者: qugang
 * @E-mail   邮箱: qgass@163.com
 * @date     创建时间：2018/11/19
 * 类说明     加入房间
 */
package com.linkflywind.gameserver.logicserver.action;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.protocolData.A1004Request;
import com.linkflywind.gameserver.logicserver.protocolData.ErrorResponse;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.logicserver.room.message.AppendMessage;
import com.linkflywind.gameserver.logicserver.room.message.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Protocol(1003)
public class A1004Action extends BaseAction {

    @Autowired
    ActorSystem actorSystem;


    @Autowired
    private final YingSanZhangRoomActorManager roomActorManager;


    private final ValueOperations<String, YingSanZhangPlayer> valueOperationsByPlayer;


    protected A1004Action(RedisTemplate redisTemplate, YingSanZhangRoomActorManager roomActorManager) {
        super(redisTemplate);
        this.roomActorManager = roomActorManager;

        this.valueOperationsByPlayer = redisTemplate.opsForValue();
    }

    @Override
    public void action(TransferData optionalTransferData) throws IOException {
        A1004Request a1003Request = unPackJson(optionalTransferData.getData().get(), A1004Request.class);
        YingSanZhangPlayer p = new YingSanZhangPlayer(optionalTransferData.getGameWebSocketSession(), 1000, true);

        ActorRef actorRef = roomActorManager.getRoomActorRef(a1003Request.getRoomId());


        Patterns.ask(actorRef, new AppendMessage(a1003Request.getRoomId(), p), 3000).map(result->{
            if(((ResultMessage)result).isResult()){
                p.setRoomId(a1003Request.getRoomId());
                p.getGameWebSocketSession().setChannel(Optional.ofNullable(serverName));
                this.valueOperationsByPlayer.set(p.getGameWebSocketSession().getName(), p);
            }
            else{
                try {
                    send(new ErrorResponse("房间已满"), optionalTransferData, connectorName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            return true;
        },actorSystem.dispatcher());
    }
}
