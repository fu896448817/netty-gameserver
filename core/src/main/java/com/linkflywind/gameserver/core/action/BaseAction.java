package com.linkflywind.gameserver.core.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public abstract class BaseAction {

    @Value("${logicserver.hallserver}")
    private String connectorName;


    protected final   RedisTemplate redisTemplate;
    protected final  ValueOperations<String,GameWebSocketSession> valueOperationsByGameWebSocketSession;
    protected final ValueOperations<String,TransferData> valueOperationsByTransferData;

    
    protected BaseAction(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperationsByGameWebSocketSession = redisTemplate.opsForValue();
        this.valueOperationsByTransferData = redisTemplate.opsForValue();
    }

    public abstract void action(TransferData optionalTransferData) throws IOException;


    protected <T> T unPackJson(byte[] array, Class<T> tClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(array, tClass);
    }

    protected Map<String, String> unPackJson(byte[] array) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(array, new HashMap<String, String>().getClass());
    }


    protected byte[] packJson(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(o);
    }

//    protected Mono<Long> sendTransferData(TransferData transferData) {
//        return reactiveRedisOperationsByConnectorData.convertAndSend(connectorName, transferData);
//    }

}
