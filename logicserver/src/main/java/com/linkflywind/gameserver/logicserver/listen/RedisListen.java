package com.linkflywind.gameserver.logicserver.listen;

import com.linkflywind.gameserver.core.action.DispatcherAction;
import com.linkflywind.gameserver.core.redisModel.ConnectorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class RedisListen {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DispatcherAction dispatcherAction;

    private final ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperations;


    @Value("${logicserver.name}")
    private String listenName;

    @Autowired
    public RedisListen(DispatcherAction dispatcherAction, ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperations) {
        this.dispatcherAction = dispatcherAction;
        this.reactiveRedisOperations = reactiveRedisOperations;
    }

    @PostConstruct
    private void init() {
        reactiveRedisOperations.listenToChannel(listenName).doOnNext(onNext ->
                dispatcherAction.createAction(onNext.getMessage().getProtocol())
                .ifPresent(action -> {
                    try {
                        action.action(onNext.getMessage());
                    } catch (IOException e) {
                        logger.error("action error",e);
                    }
                })).subscribe();
    }

}
