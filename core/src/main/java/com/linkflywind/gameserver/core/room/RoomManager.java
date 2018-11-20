package com.linkflywind.gameserver.core.room;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.linkflywind.gameserver.core.action.BaseAction;
import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.annotation.RoomActionMapper;
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
@NoArgsConstructor
@AllArgsConstructor
@Component
public class RoomManager {
    protected ConcurrentHashMap<String, ActorRef> map = new ConcurrentHashMap<>();
    Map<Class, RoomAction> cacheMap = new HashMap<>();

    @Value("${logicserver.hallserver}")
    protected String connectorName;

    @Value("${logicserver.name}")
    protected String serverName;

    @Autowired
    protected RedisTemplate redisTemplate;

    @Autowired
    protected ActorSystem actorSystem;

    @Autowired
    protected
    ApplicationContext context;


    public ActorRef getRoomActorRef(String roomId){
        return map.get(roomId);
    }

    public void clearRoom(String roomId){
        ActorRef actorRef =  map.remove(roomId);
        actorSystem.stop(actorRef);
    }


    @PostConstruct
    public void init() {
        for (String bean : context.getBeanNamesForAnnotation(RoomActionMapper.class)) {
            Object o = context.getBean(bean);
            RoomActionMapper protocol =o.getClass().getAnnotation(RoomActionMapper.class);
            cacheMap.put(protocol.value(), (RoomAction) o);
        }
    }
}
