/*
 * @author   作者: qugang
 * @E-mail   邮箱: qgass@163.com
 * @date     创建时间：2018/11/19
 * 类说明     创建房间
 */
package com.linkflywind.gameserver.logicserver.action;

import akka.actor.ActorRef;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.data.monoModel.UserModel;
import com.linkflywind.gameserver.data.monoRepository.UserRepository;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.protocolData.A1003Request;
import com.linkflywind.gameserver.logicserver.protocolData.ErrorResponse;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoom;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomActorManager;
import com.linkflywind.gameserver.logicserver.protocolData.A1003Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Protocol(1003)

public class A1003Action extends BaseAction {


    @Autowired
    private final YingSanZhangRoomActorManager roomActorManager;
    private final UserRepository userRepository;

    private final ValueOperations<String, GameWebSocketSession> valueOperationsByPlayer;

    protected A1003Action(RedisTemplate redisTemplate, YingSanZhangRoomActorManager roomActorManager, UserRepository userRepository) {
        super(redisTemplate);
        this.roomActorManager = roomActorManager;
        this.valueOperationsByPlayer = redisTemplate.opsForValue();
        this.userRepository = userRepository;
    }

    @Override
    public void action(TransferData optionalTransferData) throws IOException {
        A1003Request a1003Request = unPackJson(optionalTransferData.getData().get(), A1003Request.class);
        UserModel userModel = this.userRepository.findByName(optionalTransferData.getGameWebSocketSession().getName());

        if (userModel.getCardNumber() > 0) {
            YingSanZhangPlayer p = new YingSanZhangPlayer(optionalTransferData.getGameWebSocketSession(), 1000, true);
            p.getGameWebSocketSession().setChannel(Optional.ofNullable(serverName));
            String roomNumber = roomActorManager.createRoomActor(p,
                    a1003Request.getPlayerLowerlimit(),
                    a1003Request.getPlayerUpLimit(),
                    redisTemplate,
                    a1003Request.getXiaZhuTop(),
                    a1003Request.getJuShu());

            p.getGameWebSocketSession().setRoomNumber(Optional.ofNullable(roomNumber));
            this.valueOperationsByPlayer.set(p.getGameWebSocketSession().getName(), p.getGameWebSocketSession());

            send(new A1003Response(roomNumber), optionalTransferData, connectorName);
        } else {
            send(new ErrorResponse("房卡不足"), optionalTransferData, connectorName);
        }
    }
}
