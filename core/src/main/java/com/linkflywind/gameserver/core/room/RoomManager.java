package com.linkflywind.gameserver.core.room;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.linkflywind.gameserver.core.annotation.Protocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public abstract class RoomManager {
    protected ConcurrentHashMap<String, ActorRef> map = new ConcurrentHashMap<>();
    Map<Integer, RoomAction> cacheMap = new HashMap<>();


    protected String connectorName;
    protected String serverName;
    protected RedisTemplate redisTemplate;
    protected ActorSystem actorSystem;
    protected ApplicationContext context;


    public RoomManager( RedisTemplate redisTemplate, ActorSystem actorSystem, ApplicationContext context) {
        this.redisTemplate = redisTemplate;
        this.actorSystem = actorSystem;
        this.context = context;
    }

    public ActorRef getRoomActorRef(String roomId){
        return map.get(roomId);
    }

    public void clearRoom(String roomId){
        ActorRef actorRef =  map.remove(roomId);
        actorSystem.stop(actorRef);
    }


    @PostConstruct
    public void init() {
        for (String bean : context.getBeanNamesForAnnotation(Protocol.class)) {
            Object o = context.getBean(bean);
            Protocol protocol = o.getClass().getAnnotation(Protocol.class);

            if(o.getClass().getSuperclass().equals(RoomAction.class)) {
                cacheMap.put(protocol.value(), (RoomAction) o);
            }
        }
    }
}
