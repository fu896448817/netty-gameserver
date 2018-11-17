package com.linkflywind.gameserver.core.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

public abstract class Room {
    private String roomNumber;
    private int playerUpLimit;
    private int playerLowerlimit;
    private Player yingSanZhangPoker;
    private RedisTemplate redisTemplate;


    public Room(String roomNumber, int playerLowerlimit, int playerUpLimit, RedisTemplate redisTemplate) {
        this.roomNumber = roomNumber;
        this.playerLowerlimit = playerLowerlimit;
        this.playerUpLimit = playerUpLimit;
        this.redisTemplate = redisTemplate;
    }


    public int getPlayerUpLimit() {
        return playerUpLimit;
    }

    public void setPlayerUpLimit(int playerUpLimit) {
        this.playerUpLimit = playerUpLimit;
    }

    public int getPlayerLowerlimit() {
        return playerLowerlimit;
    }

    public void setPlayerLowerlimit(int playerLowerlimit) {
        this.playerLowerlimit = playerLowerlimit;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    protected List<Player> playerList;

    public List< Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void create(Player player) {
        playerList.add(player);
    }

    public void join(Player player) {
        playerList.add(player);
        if (this.playerList.size() == this.playerLowerlimit) {
            beginGame();
        }
    }

    public void exit(Player player) {
        playerList.removeIf(player1 -> ((Player)player1).getGameWebSocketSession().getName().equals(player.getGameWebSocketSession().getName()));
    }

    public void exit(String name) {
        playerList.removeIf(player1 -> ((Player)player1).getGameWebSocketSession().getName().equals(name));
    }

    public abstract void beginGame();

    protected byte[] packJson(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(o);
    }

    protected void send(Object o, TransferData transferData, String connector) throws JsonProcessingException {
        byte[] data = packJson(o);
        transferData.setData(java.util.Optional.ofNullable(data));
        this.redisTemplate.convertAndSend(connector, transferData);
    }
}

