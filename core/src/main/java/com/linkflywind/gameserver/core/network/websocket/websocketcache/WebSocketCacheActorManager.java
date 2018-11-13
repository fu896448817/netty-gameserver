package com.linkflywind.gameserver.core.network.websocket.websocketcache;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class WebSocketCacheActorManager {

    private ActorSystem actorSystem;
    private ActorRef tcpCacheActor;

    /**
     * 获取TCP Cache actor
     */
    public ActorRef getTcpCacheActor() { return tcpCacheActor; }

    /**
     * 获取 actor system
     */
    public ActorSystem getActorSystem() {
        return actorSystem;
    }

    /**
     * 初始化tcpCacheActor 对象
     */
    @PostConstruct
    public void initActor() {
        actorSystem = ActorSystem.create("system");
        tcpCacheActor = actorSystem.actorOf(
                new RoundRobinPool(1).props(Props.create(WebSocketCacheActor.class)),
                "WebSocketCacheActor");
    }
}
