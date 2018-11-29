package com.linkflywind.gameserver.yingsanzhangserver.listen;

import com.linkflywind.gameserver.core.TransferData;
import com.linkflywind.gameserver.core.action.DispatcherAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class RedisListenTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private DispatcherAction dispatcherAction;

    @Async
    public void doMessageTask(Message message, byte[] bytes){

        TransferData transferData = (TransferData) Objects.requireNonNull(this.redisTemplate.getDefaultSerializer()).deserialize(message.getBody());

        dispatcherAction.createAction(transferData.getProtocol()).ifPresent(p-> {
            try {
                logger.info("run action :" + transferData.getProtocol());
                p.requestAction(transferData);
            } catch (IOException e) {
                logger.error("action error",e);
            }
        });
    }
}
