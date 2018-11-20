package com.linkflywind.gameserver.logicserver.listen;

import com.linkflywind.gameserver.core.action.DispatcherAction;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RedisListen implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DispatcherAction dispatcherAction;

    private final RedisTemplate redisTemplate;


    @Value("${logicserver.name}")
    private String listenName;

    public RedisListen(DispatcherAction dispatcherAction, RedisTemplate redisTemplate) {
        this.dispatcherAction = dispatcherAction;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void onMessage(Message message, byte[] bytes) {
        doMessageTask(message,bytes);
    }

    @Async
    public void doMessageTask(Message message, byte[] bytes){
        TransferData transferData = (TransferData) this.redisTemplate.getDefaultSerializer().deserialize(message.getBody());
        dispatcherAction.createAction(transferData.getProtocol()).ifPresent(p-> {
            try {
                p.action(transferData);
            } catch (IOException e) {
                logger.error("action error",e);
            }
        });
    }
}
