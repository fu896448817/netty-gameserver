package com.linkflywind.gameserver.connector;

import com.linkflywind.gameserver.connector.WebSocketCache.WebSocketCacheActorManager;
import com.linkflywind.gameserver.connector.WebSocketCache.message.PutChannel;
import com.linkflywind.gameserver.connector.WebSocketCache.message.RemoveChannel;
import com.linkflywind.gameserver.connector.config.ConnectorConfig;
import com.linkflywind.gameserver.connector.redisModel.UserSession;
import com.linkflywind.gameserver.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;


import java.security.Principal;
import java.util.Date;
import java.util.Objects;

public class ReactiveWebSocketHandler implements WebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WebSocketCacheActorManager webSocketCacheActorManager;
    private final ReactiveRedisOperations<String, UserSession> reactiveRedisOperations;
    private final ConnectorConfig connectorConfig;


    ReactiveWebSocketHandler(WebSocketCacheActorManager webSocketCacheActorManager, ReactiveRedisOperations<String, UserSession> reactiveRedisOperations, ConnectorConfig connectorConfig) {
        this.webSocketCacheActorManager = webSocketCacheActorManager;
        this.reactiveRedisOperations = reactiveRedisOperations;
        this.connectorConfig = connectorConfig;
    }

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession.getHandshakeInfo()
                .getPrincipal()
                .filterWhen(principal -> isAuthorized(principal,
                        webSocketSession.getId(),
                        webSocketSession.getHandshakeInfo().getHeaders()))
                .then(doHandle(webSocketSession));
    }

    private Mono<Boolean> isAuthorized(Principal principal, String sessionId, HttpHeaders headers) {
        String token = principal.toString();
        String user = Objects.requireNonNull(headers.get("user")).get(0);
        if (JwtTokenUtil.validateToken(token, user)) {
            return loginRedis(user, sessionId,token);
        }
        return Mono.just(false);
    }

    private Mono<Boolean> loginRedis(String user,
                                     String sessionId,
                                     String token) {

        return reactiveRedisOperations.opsForValue()
                .set(user, new UserSession(user, sessionId, token, new Date().getTime(), 0L, "0"));
    }

    private Mono<Boolean> logoutRedis(HttpHeaders headers){
        String user = Objects.requireNonNull(headers.get("user")).get(0);
        return reactiveRedisOperations.opsForValue().get(user).flatMap(p->{
            p.setLastLogoutTime(new Date().getTime());
            return reactiveRedisOperations.opsForValue().set(user,p);
        });
    }

    private Mono<Void> doHandle(WebSocketSession session) {
        UnicastProcessor<WebSocketMessage> unicastProcessor = UnicastProcessor.create();
        webSocketCacheActorManager.getTcpCacheActor().tell(new PutChannel(session.getId(), unicastProcessor), null);

        session.receive().doOnNext(p -> {
            byte[] array = p.getPayload().asByteBuffer().array();
            int channel = (int) array[0];
            connectorConfig.getRoutes().get(channel);


        }).doFinally(sig -> {
            logger.info("Terminating WebSocket Session (client side) sig: [{}], [{}]", sig.name(), session.getId());
            session.close();
            webSocketCacheActorManager.getTcpCacheActor().tell(new RemoveChannel(session.getId()), null);
            logoutRedis(session.getHandshakeInfo().getHeaders()).subscribe();
        });
        return session.send(unicastProcessor);
    }
}
