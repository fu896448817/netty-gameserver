package com.linkflywind.gameserver.core.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

@Data
public abstract class Room<T> {
    private String roomNumber;
    private int playerUpLimit;
    private int playerLowerlimit;
    private Player yingSanZhangPoker;
    private RedisTemplate redisTemplate;
    private T master;


    public Room(String roomNumber, int playerLowerlimit, int playerUpLimit, RedisTemplate redisTemplate) {
        this.roomNumber = roomNumber;
        this.playerLowerlimit = playerLowerlimit;
        this.playerUpLimit = playerUpLimit;
        this.redisTemplate = redisTemplate;
        this.playerList = new ConcurrentLinkedQueue<>();
    }

    protected ConcurrentLinkedQueue<T> playerList;

    public void create(T player) {
        playerList.add(player);
    }

    public void join(T player) {
        playerList.add(player);
        if (this.playerList.size() == this.playerLowerlimit) {
            beginGame();
        }
    }

    public void exit(T player) {
        playerList.remove(player);
    }

    public abstract void beginGame();

    public abstract void ready(String name);

    public abstract T getPlayer(String name);

    public abstract void exit(String name);

    public abstract void disConnection(String name);

    public abstract void reConnection(String name);

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

