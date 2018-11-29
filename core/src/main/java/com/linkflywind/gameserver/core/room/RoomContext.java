package com.linkflywind.gameserver.core.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkflywind.gameserver.core.player.Player;
import com.linkflywind.gameserver.core.TransferData;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;

@Data
@Component
@Scope(value = "prototype")
@NoArgsConstructor
public abstract class RoomContext {
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
    protected volatile LinkedList<? super Player> playerList;
    protected RoomManager roomManager;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public RoomContext(String roomNumber,
                       int playerUpLimit,
                       int playerLowerlimit,
                       RedisTemplate redisTemplate,
                       Player master,
                       String serverName,
                       String connectorName,
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
                Player player = (Player) playerObject;
                send(o, new TransferData(player.getGameWebSocketSession(),
                        this.serverName, protocol, null));
        }
    }

    private byte[] packJson(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(o);
    }


    @Async
    public void send(Object o, TransferData transferData) {
        try {
            byte[] data = packJson(o);
            transferData.setData(data);
            this.redisTemplate.convertAndSend(this.connectorName, transferData);
        } catch (JsonProcessingException e) {
            logger.error("JsonProcessingException ",e);
        }
    }


    public Optional<Player> getPlayer(String id) {
        for (Object player : this.playerList) {
            if (((Player) player).getGameWebSocketSession().getId().equals(id))
                return Optional.of(((Player) player));
        }
        return Optional.empty();
    }
}
