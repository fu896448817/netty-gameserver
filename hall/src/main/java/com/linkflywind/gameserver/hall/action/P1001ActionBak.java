package com.linkflywind.gameserver.hall.action;


import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.redisModel.ConnectorData;
import com.linkflywind.gameserver.core.room.RoomHallManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Protocol(1001)
public class P1001ActionBak extends BaseAction {

    private final RoomHallManager roomHallManager;


    @Autowired
    public P1001ActionBak(ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperationsByConnectorData,
                          RoomHallManager roomHallManager) {
        super(reactiveRedisOperationsByConnectorData);
        this.roomHallManager = roomHallManager;
    }

    @Override
    public void action(ConnectorData connectorData) throws IOException {
        byte[] array = packJson(roomHallManager.getRoomHallList());
        connectorData.setData(array);
        sendConnectorData(connectorData).doOnNext(next->{

        }).subscribe();
    }
}
