package com.linkflywind.gameserver.hallserver.listen;

import com.linkflywind.gameserver.core.TransferData;
import com.linkflywind.gameserver.hallserver.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RedisListen extends MessageListenerAdapter {


    @Autowired
    private RedisListenTask redisListenTask;



    @Override
    public void onMessage(Message message, byte[] bytes) {
        redisListenTask.doMessageTask(message,bytes);
    }


}
