package com.linkflywind.gameserver.hall.action;

import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.redisModel.ConnectorData;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Protocol(1001)
public class P1001Action extends BaseAction {
    protected P1001Action(ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperationsByConnectorData) {
        super(reactiveRedisOperationsByConnectorData);
    }

    @Override
    public void action(ConnectorData connectorData) {

    }
}
