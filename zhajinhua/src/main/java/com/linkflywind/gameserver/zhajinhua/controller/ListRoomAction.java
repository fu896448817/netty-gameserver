package com.linkflywind.gameserver.zhajinhua.controller;


import com.linkflywind.gameserver.zhajinhua.redisModel.ConnectorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;

@Component
public class ListRoomAction extends BaseAction{


    private final ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperationsByConnectorData;
    @Autowired
    ListRoomAction(ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperationsByConnectorData) {
        super(0);
        this.reactiveRedisOperationsByConnectorData = reactiveRedisOperationsByConnectorData;
    }



    @Override
    public void proccess(ConnectorData connectorData) {

    }
}
