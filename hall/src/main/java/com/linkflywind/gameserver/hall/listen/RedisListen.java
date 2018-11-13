package com.linkflywind.gameserver.hall.listen;

import akka.pattern.Patterns;
import com.linkflywind.gameserver.hall.config.HallConfig;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.WebSocketCacheActorManager;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.message.PopChannel;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.message.TellPopChannel;
import com.linkflywind.gameserver.core.redisModel.ConnectorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperations;

    private final HallConfig connectorConfig;

    private final WebSocketCacheActorManager webSocketCacheActorManager;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RedisListen(ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperations, HallConfig connectorConfig, WebSocketCacheActorManager webSocketCacheActorManager) {
        this.reactiveRedisOperations = reactiveRedisOperations;
        this.connectorConfig = connectorConfig;
        this.webSocketCacheActorManager = webSocketCacheActorManager;
    }

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
