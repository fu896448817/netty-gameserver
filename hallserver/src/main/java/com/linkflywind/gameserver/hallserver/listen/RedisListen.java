package com.linkflywind.gameserver.hallserver.listen;

import akka.pattern.Patterns;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.WebSocketCacheActorManager;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.message.PopChannel;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.message.TellPopChannel;
import com.linkflywind.gameserver.core.redisModel.TransferData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import scala.concurrent.Future;
import scala.reflect.ClassTag;
import scala.reflect.ClassTag$;

@Component
public class RedisListen implements MessageListener {


    private final WebSocketCacheActorManager webSocketCacheActorManager;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RedisTemplate redisTemplate;

    @Autowired
    public RedisListen(WebSocketCacheActorManager webSocketCacheActorManager, RedisTemplate redisTemplate) {
        this.webSocketCacheActorManager = webSocketCacheActorManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        TransferData transferData = (TransferData) this.redisTemplate.getDefaultSerializer().deserialize(message.getBody());
        ClassTag<TellPopChannel> tag = ClassTag$.MODULE$.apply(TellPopChannel.class);
        Future<TellPopChannel> future = Patterns.ask(webSocketCacheActorManager.getTcpCacheActor(),
                new PopChannel(transferData.getGameWebSocketSession().getName(),
                        transferData.getData()),
                3000).mapTo(tag);

        future.map(p -> p.getMessage().map(messageByte -> p.getGameWebSocketSession()
                            .getSession()
                            .sendBinary(messageByte))
                , webSocketCacheActorManager.getActorSystem().dispatcher());
    }
}
