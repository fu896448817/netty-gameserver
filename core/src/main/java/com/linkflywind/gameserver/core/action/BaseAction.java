package com.linkflywind.gameserver.core.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkflywind.gameserver.core.redisModel.ConnectorData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public abstract class BaseAction {

    @Value("${logicserver.hall}")
    private String connectorName;


    private final ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperationsByConnectorData;

    
    protected BaseAction(ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperationsByConnectorData) {
        this.reactiveRedisOperationsByConnectorData = reactiveRedisOperationsByConnectorData;
    }

    public abstract void action(ConnectorData connectorData) throws IOException;


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

    protected Mono<Long> sendConnectorData(ConnectorData connectorData) {
        return reactiveRedisOperationsByConnectorData.convertAndSend(connectorName, new ConnectorData());
    }

}
