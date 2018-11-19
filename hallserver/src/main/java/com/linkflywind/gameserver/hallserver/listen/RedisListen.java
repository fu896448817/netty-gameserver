package com.linkflywind.gameserver.hallserver.listen;

import com.linkflywind.gameserver.core.redisModel.TransferData;
import com.linkflywind.gameserver.hallserver.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisListen implements MessageListener {



    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RedisTemplate redisTemplate;

    @Autowired
    public RedisListen(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        TransferData transferData = (TransferData) this.redisTemplate.getDefaultSerializer().deserialize(message.getBody());
        transferData.getData().ifPresent(p-> GameServer.send(transferData.getGameWebSocketSession().getSessionId(),p));
    }
}
