package com.linkflywind.gameserver.core.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.LinkedList;
import java.util.Optional;

@Data
public abstract class Room {
    private String roomNumber;
    private int playerUpLimit;
    private int playerLowerlimit;
    private Player yingSanZhangPoker;
    private RedisTemplate redisTemplate;
    private Player master;


    public Room(String roomNumber, int playerLowerlimit, int playerUpLimit, RedisTemplate redisTemplate) {
        this.roomNumber = roomNumber;
        this.playerLowerlimit = playerLowerlimit;
        this.playerUpLimit = playerUpLimit;
        this.redisTemplate = redisTemplate;
        this.playerList = new LinkedList<>();
    }

    protected LinkedList<? super Player> playerList;

    public void create(Player player) {
        playerList.add(player);
    }

    public boolean join(Player player) {
        if(this.playerList.size() <= playerUpLimit) {
            playerList.add(player);
            if (this.playerList.size() == this.playerLowerlimit) {
                beginGame();
            }
            return true;
        }
        return false;
    }

    public void exit(Player player) {
        playerList.remove(player);
    }

    public void exit(String name) {
        playerList.removeIf(p -> ((Player) p).getName().equals(name));
    }

    public abstract void beginGame();

    public Optional<Object> ready(String name) {
        Optional<Object> player = this.getPlayer(name);
        this.getPlayer(name).ifPresent(p -> {
            ((Player) p).setReady(true);
        });
        return player;
    }

    public Optional<Object> getPlayer(String name) {
        for (Object player : this.playerList) {
            if (((Player) player).getName().equals(name))
                return Optional.ofNullable(player);
        }
        return Optional.empty();
    }

    public Optional<Object> disConnection(String name) {
        Optional<Object> player = this.getPlayer(name);
        this.getPlayer(name).ifPresent(p -> {
            ((Player) p).setDisConnection(true);
        });
        return player;
    }

    public Optional<Object> reConnection(String name) {
        Optional<Object> player = this.getPlayer(name);
        this.getPlayer(name).ifPresent(p -> {
            ((Player) p).setDisConnection(false);
        });
        return player;
    }

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

