package com.linkflywind.gameserver.core.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.LinkedList;
import java.util.Optional;

@Data
public class RoomContext {
    protected String roomNumber;
    protected int playerUpLimit;
    protected int playerLowerlimit;
    protected RedisTemplate redisTemplate;
    protected Player master;
    protected boolean isDisbanded;
    protected int inningsNumber;
    protected int currentInningsNUmber;
    protected String serverName;
    protected String connectorName;
    protected LinkedList<? super Player> playerList;
    protected RoomManager roomManager;


    public RoomContext(String roomNumber, int playerUpLimit,
                       int playerLowerlimit, RedisTemplate redisTemplate, Player master,
                       String serverName, String connectorName,
                       RoomManager roomManager) {
        this.roomNumber = roomNumber;
        this.playerUpLimit = playerUpLimit;
        this.playerLowerlimit = playerLowerlimit;
        this.redisTemplate = redisTemplate;
        this.master = master;
        this.serverName = serverName;
        this.connectorName = connectorName;
        this.playerList = new LinkedList<>();
        this.roomManager = roomManager;
    }

    public void sendAll(Object o, int protocol) {
        for (Object playerObject : this.playerList) {
            try {
                Player player = (Player) playerObject;
                send(o, new TransferData(player.getGameWebSocketSession(),
                        this.serverName, protocol, Optional.empty()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] packJson(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(o);
    }

    public void send(Object o, TransferData transferData) throws JsonProcessingException {
        byte[] data = packJson(o);
        transferData.setData(java.util.Optional.ofNullable(data));
        this.redisTemplate.convertAndSend(this.connectorName, transferData);
    }


    public Optional<Player> getPlayer(String name) {
        for (Object player : this.playerList) {
            if (((Player) player).getGameWebSocketSession().getName().equals(name))
                return Optional.of(((Player) player));
        }
        return Optional.empty();
    }
}
