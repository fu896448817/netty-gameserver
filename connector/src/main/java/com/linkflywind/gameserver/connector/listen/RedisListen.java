package com.linkflywind.gameserver.connector.listen;

import akka.pattern.Patterns;
import com.linkflywind.gameserver.connector.config.ConnectorConfig;
import com.linkflywind.gameserver.connector.redisModel.ConnectorData;
import com.linkflywind.gameserver.connector.webSocketCache.WebSocketCacheActorManager;
import com.linkflywind.gameserver.connector.webSocketCache.message.PopChannel;
import com.linkflywind.gameserver.connector.webSocketCache.message.TellPopChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import scala.concurrent.Future;
import scala.reflect.ClassTag;
import scala.reflect.ClassTag$;

import javax.annotation.PostConstruct;

@Component
public class RedisListen {

    @Autowired
    private ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperations;

    @Autowired
    private ConnectorConfig connectorConfig;

    @Autowired
    private WebSocketCacheActorManager webSocketCacheActorManager;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostConstruct
    private void init() {
        reactiveRedisOperations.listenToChannel(connectorConfig.getName()).doOnNext(onNext -> {
            ClassTag<TellPopChannel> tag = ClassTag$.MODULE$.apply(TellPopChannel.class);
            Future<TellPopChannel> future = Patterns.ask(webSocketCacheActorManager.getTcpCacheActor(),
                    new PopChannel(onNext.getMessage().getSessionId(),
                            onNext.getMessage().getData()),
                    3000).mapTo(tag);

            future.map(p -> p.getUnicastProcessor()
                            .sink()
                            .next(new WebSocketMessage(WebSocketMessage.Type.BINARY, new DefaultDataBufferFactory().wrap(p.getMessage())))
                    , webSocketCacheActorManager.getActorSystem().dispatcher());
        }).subscribe();
    }

}
