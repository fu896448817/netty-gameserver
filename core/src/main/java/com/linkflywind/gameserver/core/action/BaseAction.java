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


public abstract class BaseAction {

    @Value("${logicserver.hallserver}")
    protected String connectorName;

    @Value("${logicserver.name}")
    protected String serverName;



    protected final   RedisTemplate redisTemplate;
    protected final  ValueOperations<String,GameWebSocketSession> valueOperationsByGameWebSocketSession;
    protected final ValueOperations<String,TransferData> valueOperationsByTransferData;

    
    protected BaseAction(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperationsByGameWebSocketSession = redisTemplate.opsForValue();
        this.valueOperationsByTransferData = redisTemplate.opsForValue();
    }

    /**
     * 请求消息处理
     * @param optionalTransferData 请求报文
     */
    public abstract void requestAction(TransferData optionalTransferData) throws IOException;


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

    protected void send(Object o,TransferData transferData,String connector) throws JsonProcessingException {
        byte[] data = packJson(o);
        transferData.setData(java.util.Optional.ofNullable(data));
        this.redisTemplate.convertAndSend(connector, transferData);
    }

//    protected Mono<Long> sendTransferData(TransferData transferData) {
//        return reactiveRedisOperationsByConnectorData.convertAndSend(connectorName, transferData);
//    }

}
