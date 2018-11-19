/*
 * @author   作者: qugang
 * @E-mail   邮箱: qgass@163.com
 * @date     创建时间：2018/11/19
 * 类说明     加入房间
 */
package com.linkflywind.gameserver.logicserver.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.protocolData.A1004Request;
import com.linkflywind.gameserver.logicserver.protocolData.A1004Response;
import com.linkflywind.gameserver.logicserver.protocolData.ErrorResponse;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoom;
import com.linkflywind.gameserver.logicserver.room.YingSanZhangRoomManager;
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
    private final YingSanZhangRoomManager roomManager;


    private final ValueOperations<String, YingSanZhangPlayer> valueOperationsByPlayer;


    protected A1004Action(RedisTemplate redisTemplate, YingSanZhangRoomManager roomManager) {
        super(redisTemplate);
        this.roomManager = roomManager;

        this.valueOperationsByPlayer = redisTemplate.opsForValue();
    }

    @Override
    public void action(TransferData optionalTransferData) throws IOException {
        A1004Request a1003Request = unPackJson(optionalTransferData.getData().get(), A1004Request.class);
        YingSanZhangPlayer p = new YingSanZhangPlayer(optionalTransferData.getGameWebSocketSession(), 1000, true);
        Optional<YingSanZhangRoom> optionalRoom = roomManager.appendRoom(a1003Request.getRoomId(), p);

        if(optionalRoom.isPresent())
        {
            YingSanZhangRoom room = optionalRoom.get();
            p.setRoomId(room.getRoomNumber());
            p.getGameWebSocketSession().setChannel(Optional.ofNullable(serverName));
            this.valueOperationsByPlayer.set(p.getGameWebSocketSession().getName(), p);

            for (Player player : room.getPlayerList()) {
                TransferData sendTransferData = new TransferData(player.getGameWebSocketSession(), optionalTransferData.getChannel(), optionalTransferData.getProtocol(), Optional.empty());
                try {
                    send(new A1004Response(room.getPlayerList().toArray(new YingSanZhangPlayer[room.getPlayerList().size()]), room.getRoomNumber()), sendTransferData, connectorName);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            send(new ErrorResponse("房间已满"), optionalTransferData, connectorName);
        }

    }
}
