package com.linkflywind.gameserver.logicserver.room;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkflywind.gameserver.cardlib.poker.YingSanZhang.YingSanZhangPoker;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.core.room.Room;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayerState;
import com.linkflywind.gameserver.logicserver.protocolData.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

public class YingSanZhangRoom extends Room<YingSanZhangPlayer> {
    private YingSanZhangPoker yingSanZhangPoker;
    private final String connectorName;
    private int xiaZhuTop;
    private int zhuoMain;
    private int juShu;
    private int currentJuShuu;
    private YingSanZhangRoomManager roomManager;
    private String serverName;

    private final ValueOperations<String, GameWebSocketSession> valueOperationsByPlayer;

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
    public YingSanZhangRoom(String roomNumber,
                            int playerLowerlimit,
                            int playerUpLimit,
                            RedisTemplate redisTemplate,
                            String connectorName,
                            int xiaZhuTop,
                            int juShu,
                            String serverName,
                            YingSanZhangRoomManager yingSanZhangRoomManager) {
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
        for (YingSanZhangPlayer player : this.playerList) {
            player.setYingSanZhang(yingSanZhangPoker.getPocker());
        }
        this.playerList.peek().setOp(true);
        sendAll(new A1005Response(this.playerList.toArray(new YingSanZhangPlayer[this.playerList.size()]), this.playerList.peek()), 1005);
        this.currentJuShuu++;
    }

    @Override
    public void ready(String name) {
        YingSanZhangPlayer player = getPlayer(name);
        player.setReady(true);
        sendAll(new A1009Response(player), 1009);

        if (!this.playerList.stream().filter(p -> !p.isReady()).findFirst().isPresent()) {
            reLoadGame();
        }
    }

    @Override
    public YingSanZhangPlayer getPlayer(String name) {
        Optional<YingSanZhangPlayer> optionalYingSanZhangPlayer = this.playerList.stream()
                .filter(p -> p.getGameWebSocketSession().getName().equals(name))
                .findFirst();

        return optionalYingSanZhangPlayer.get();

    }

    @Override
    public void exit(String name) {
        this.playerList.removeIf(p -> p.getGameWebSocketSession().getName().equals(name));
    }


    @Override
    public void disConnection(String name) {
        YingSanZhangPlayer yingSanZhangPlayer = getPlayer(name);
        yingSanZhangPlayer.setDiaoXian(true);
        sendAll(new CloseResponse(name), 1001);
    }

    @Override
    public void reConnection(String name) {
        YingSanZhangPlayer yingSanZhangPlayer = getPlayer(name);
        yingSanZhangPlayer.setDiaoXian(false);
        sendAll(new ConnectResponse(name), 1001);
    }

    public void next() {
        if (checkResult()) {
            return;
        }

        int size = this.playerList.size();
        for (int i = 0; i < size; i++) {
            YingSanZhangPlayer player = this.playerList.poll();
            if (player.getState() != YingSanZhangPlayerState.shu &&
                    player.getState() != YingSanZhangPlayerState.qiquan) {
                sendAll(new A1006Response(player), 1006);
                break;
            }
        }
    }

    public void xiazhu(int chouma, String type) {
        if (chouma <= xiaZhuTop) {
            zhuoMain += chouma;
            YingSanZhangPlayer player = this.playerList.element();
            player.score -= chouma;
            sendAll(new A1007Response(player, type, chouma), 1007);
        }
        next();
    }

    public void biPai(String name) {
        YingSanZhangPlayer yingSanZhangPlayer = this.playerList.element();
        YingSanZhangPlayer player = getPlayer(name);
        int result = yingSanZhangPlayer.compareTo(player);
        if (result > 1) {
            player.setState(YingSanZhangPlayerState.shu);
            sendAll(new A1008Response(yingSanZhangPlayer, player), 1008);
        } else if (result < 1) {
            yingSanZhangPlayer.setState(YingSanZhangPlayerState.shu);
            sendAll(new A1008Response(player, yingSanZhangPlayer), 1008);
        } else {
        }
        next();
    }

    public boolean checkResult() {
        long count = this.playerList.stream().filter(p -> p.getState() == YingSanZhangPlayerState.none).count();
        if (count == 1) {
            YingSanZhangPlayer yingSanZhangPlayer = this.playerList.stream().filter(p -> p.getState() == YingSanZhangPlayerState.win).findFirst().get();
            yingSanZhangPlayer.score += zhuoMain;
            sendAll(yingSanZhangPlayer, 1009);
            return true;
        }
        return false;
    }

    public void reLoadGame() {
        if (this.currentJuShuu >= this.juShu) {
            sendAll(new A1010Response(this.getRoomNumber()),1010);
        }

        yingSanZhangPoker.shuffle();

        YingSanZhangPlayer yingSanZhangPlayer = this.playerList.stream().filter(p -> p.getState() == YingSanZhangPlayerState.win).findFirst().get();
        for (YingSanZhangPlayer player : this.playerList) {
            player.setState(YingSanZhangPlayerState.none);
            player.setOp(false);
            player.setYingSanZhang(yingSanZhangPoker.getPocker());
        }
        yingSanZhangPlayer.setOp(true);
        sendAll(new A1005Response(
                this.playerList.toArray(new YingSanZhangPlayer[this.playerList.size()])
                , yingSanZhangPlayer), 1005);

        this.currentJuShuu++;
    }

    public void clearRoom() {
        roomManager.clerRoom(this.getRoomNumber());
        for (YingSanZhangPlayer player : this.playerList) {
            player.getGameWebSocketSession().setChannel(Optional.empty());
            valueOperationsByPlayer.getOperations().delete(player.getGameWebSocketSession().getName());
        }
    }

    public void endGame() {
    }


    private void sendAll(Object o, int protocol) {
        for (YingSanZhangPlayer player : this.playerList) {
            try {
                send(o, new TransferData(player.getGameWebSocketSession(),
                        this.serverName, protocol, Optional.empty()), connectorName);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
