package com.linkflywind.gameserver.logicserver.room;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkflywind.gameserver.cardlib.poker.YingSanZhang.YingSanZhangPoker;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.core.room.Room;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayerState;
import com.linkflywind.gameserver.logicserver.protocolData.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

public class YingSanZhangRoom extends Room {
    private YingSanZhangPoker yingSanZhangPoker;
    private final String connectorName;
    private int xiaZhuTop;
    private int zhuoMain;
    private int juShu;
    private int currentJuShuu;
    private YingSanZhangRoomActorManager roomManager;
    private String serverName;

    private final ValueOperations valueOperationsByPlayer;

    /**
     * 创建房间
     *
     * @param roomNumber              房间号
     * @param redisTemplate           redis
     * @param connectorName           大厅名称
     * @param xiaZhuTop               下注上限
     * @param juShu                   局数
     * @param yingSanZhangRoomManager 房间管理
     */
    YingSanZhangRoom(String roomNumber,
                     int playerLowerlimit,
                     int playerUpLimit,
                     RedisTemplate redisTemplate,
                     String connectorName,
                     int xiaZhuTop,
                     int juShu,
                     String serverName,
                     YingSanZhangRoomActorManager yingSanZhangRoomManager) {
        super(roomNumber, playerLowerlimit, playerUpLimit, redisTemplate);
        this.connectorName = connectorName;
        this.yingSanZhangPoker = new YingSanZhangPoker();
        this.xiaZhuTop = xiaZhuTop;
        this.juShu = juShu;
        this.roomManager = yingSanZhangRoomManager;
        this.valueOperationsByPlayer = redisTemplate.opsForValue();
        this.serverName = serverName;
    }

    @Override
    public void beginGame() {
        for (Object player : this.playerList) {
            ((YingSanZhangPlayer) player).setYingSanZhang(yingSanZhangPoker.getPocker());
        }
        YingSanZhangPlayer cuuentPlayer = ((YingSanZhangPlayer) this.playerList.element());
        cuuentPlayer.setOp(true);
        this.playerList.toArray(new YingSanZhangPlayer[0]);
        sendAll(new A1005Response(this.playerList.toArray(new YingSanZhangPlayer[0]), cuuentPlayer), 1005);
        this.currentJuShuu++;
    }


    Boolean join(YingSanZhangPlayer player) {
        Boolean result = super.join(player);
        if (result) {
            sendAll(new A1004Response(this.playerList.toArray(new YingSanZhangPlayer[0]), this.getRoomNumber()), 1004);
        }
        return result;
    }

    public Optional<Object> disbanded(String name) {
        Optional<Object> player = super.disbanded(name);

        player.ifPresent(p -> {
            YingSanZhangPlayer yingSanZhangPlayer = (YingSanZhangPlayer) p;
            sendAll(new A1012Response(name, this.getRoomNumber()), 1012);
        });
        return player;
    }

    @Override
    public Optional<Object> ready(String name) {

        Optional<Object> optionalPlayer = super.ready(name);

        optionalPlayer.ifPresent(p -> {
                    YingSanZhangPlayer player = ((YingSanZhangPlayer) p);
                    sendAll(new A1009Response(player), 1009);
                }
        );

        if (!this.playerList.stream().anyMatch(p -> !((YingSanZhangPlayer) p).isReady())) {
            reLoadGame();
        }

        return optionalPlayer;
    }

    @Override
    public Optional<Object> disConnection(String name) {

        Optional<Object> optionalObject = super.disConnection(name);

        optionalObject.ifPresent(p -> {
            YingSanZhangPlayer currentPlayer = (YingSanZhangPlayer) p;
            try {
                send(new A1011Response(this.zhuoMain, this.playerList.toArray(new YingSanZhangPlayer[0])),
                        new TransferData(currentPlayer.getGameWebSocketSession(),
                                this.serverName, 1011, Optional.empty()), connectorName);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return optionalObject;
    }

    @Override
    public Optional<Object> reConnection(String name) {

        Optional<Object> optionalObject = super.reConnection(name);

        optionalObject.ifPresent(p -> {
            YingSanZhangPlayer currentPlayer = (YingSanZhangPlayer) p;
            sendAll(new ConnectResponse(name), 1001);
        });

        return optionalObject;
    }

    private void next() {
        if (checkResult()) {
            return;
        }

        int size = this.playerList.size();
        for (int i = 0; i < size; i++) {
            YingSanZhangPlayer player = (YingSanZhangPlayer) this.playerList.poll();
            if (player.getState() != YingSanZhangPlayerState.shu &&
                    player.getState() != YingSanZhangPlayerState.qiquan) {
                sendAll(new A1006Response(player), 1006);
                break;
            }
        }
    }

    void xiazhu(int chouma, String type) {
        if (chouma <= xiaZhuTop) {
            zhuoMain += chouma;
            YingSanZhangPlayer player = (YingSanZhangPlayer) this.playerList.element();
            player.score -= chouma;
            sendAll(new A1007Response(player, type, chouma), 1007);
        }
        next();
    }

    void biPai(String name) {
        YingSanZhangPlayer yingSanZhangPlayer = (YingSanZhangPlayer) this.playerList.element();
        Optional<Object> optionalPlayer = getPlayer(name);
        optionalPlayer.ifPresent(p -> {
            YingSanZhangPlayer player = (YingSanZhangPlayer) p;
            int result = yingSanZhangPlayer.compareTo(player);
            if (result > 0) {
                player.setState(YingSanZhangPlayerState.shu);
                sendAll(new A1008Response(yingSanZhangPlayer, player), 1008);
            } else if (result < 1) {
                yingSanZhangPlayer.setState(YingSanZhangPlayerState.shu);
                sendAll(new A1008Response(player, yingSanZhangPlayer), 1008);
            } else {
            }
            next();
        });
    }

    private boolean checkResult() {

        long count = this.playerList.stream().filter(p -> ((YingSanZhangPlayer) p).getState() == YingSanZhangPlayerState.none).count();
        if (count == 1) {
            YingSanZhangPlayer yingSanZhangPlayer = (YingSanZhangPlayer) this.playerList.stream().filter(p -> ((YingSanZhangPlayer) p).getState() == YingSanZhangPlayerState.win).findFirst().get();
            yingSanZhangPlayer.score += zhuoMain;
            sendAll(yingSanZhangPlayer, 1009);
            return true;
        }
        return false;
    }

    private void reLoadGame() {
        if (this.currentJuShuu >= this.juShu) {
            sendAll(new A1010Response(this.getRoomNumber()), 1010);
        }

        yingSanZhangPoker.shuffle();

        YingSanZhangPlayer yingSanZhangPlayer = (YingSanZhangPlayer) this.playerList.stream().filter(p -> ((YingSanZhangPlayer) p).getState() == YingSanZhangPlayerState.win).findFirst().get();
        for (Object object : this.playerList) {
            YingSanZhangPlayer player = (YingSanZhangPlayer) object;
            player.setState(YingSanZhangPlayerState.none);
            player.setOp(false);
            player.setYingSanZhang(yingSanZhangPoker.getPocker());
        }
        yingSanZhangPlayer.setOp(true);
        sendAll(new A1005Response(
                this.playerList.toArray(new YingSanZhangPlayer[0])
                , yingSanZhangPlayer), 1005);

        this.currentJuShuu++;
    }

    public void clearRoom() {
        roomManager.clearRoom(this.getRoomNumber());
        for (Object o : this.playerList) {
            YingSanZhangPlayer player = (YingSanZhangPlayer) o;
            player.getGameWebSocketSession().setChannel(Optional.empty());
            valueOperationsByPlayer.getOperations().delete(player.getGameWebSocketSession().getName());
        }
    }

    private void sendAll(Object o, int protocol) {
        for (Object playerObject : this.playerList) {
            try {
                YingSanZhangPlayer player = (YingSanZhangPlayer) playerObject;
                send(o, new TransferData(player.getGameWebSocketSession(),
                        this.serverName, protocol, Optional.empty()), connectorName);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
